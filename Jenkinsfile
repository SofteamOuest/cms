#!groovy
import java.text.*

// pod utilisé pour la compilation du projet
podTemplate(label: 'cms', nodeSelector: 'medium', containers: [

        // le slave jenkins
        containerTemplate(name: 'jnlp', image: 'jenkinsci/jnlp-slave:alpine'),

        // un conteneur pour construire les images docker
        containerTemplate(name: 'docker', image: 'docker:18.09', command: 'cat', ttyEnabled: true)],

        // montage nécessaire pour que le conteneur docker fonction (Docker In Docker)
        volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]
) {

    node('books-api-pod') {

        properties([
                buildDiscarder(
                        logRotator(
                                artifactDaysToKeepStr: '1',
                                artifactNumToKeepStr: '1',
                                daysToKeepStr: '3',
                                numToKeepStr: '3'
                        )
                )
        ])

        def TAG = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())

        def URL = "registry.k8.wildwidewest.xyz"

        def IMAGE = "opus/books-api"

        stage('CHECKOUT') {
            checkout scm
        }

        container('docker') {

            stage('BUILD') {

                withCredentials([string(credentialsId: 'sonarqube_token', variable: 'sonarqube_tok'),
                                 string(credentialsId: 'registry_url', variable: 'registry_url')]) {

                    withDockerRegistry(credentialsId: 'nexus_user', url: "${registry_url}") {
                        sh "docker build . -f Dockerfile.ic --build-arg SONAR_TOKEN=${sonarqube_tok} --tag ${URL}/repository/docker-repository/${IMAGE}:$TAG"

                        sh "docker push ${URL}/repository/docker-repository/${IMAGE}:$TAG"
                    }
                }
            }
        }

        stage('RUN') {

            build job: "/SofteamOuest-Opus/chart-run/master",
                    wait: false,
                    parameters: [string(name: 'image', value: "$TAG"),
                                 string(name: 'chart', value: "books-api")]
        }

    }
}