# cms

Serveur Rest permettant d'upload et dowload des fichiers markdown


## Prérequis

- Java 8
- Maven
- Docker

## Lancement

- Démarrer le serveur à la racine du projet avec la commande **docker-compose up**

## Utilisation

1. Ajout d'un document mardown
   * Appel POST http://localhost:8090/trainings/ avec en paramètres:
       * **description** fichier markdown
       * **training** objet *JSON* :
```js
{"trainingName": "nom de la formation", "resume": "description de la formation"}
```
[![img postman](https://nsa39.casimages.com/img/2019/01/09/190109021746823043.png)](https://nsa39.casimages.com/img/2019/01/09/190109021746823043.png/)
    
2. Téléchargement d'une formation en fichier markdown
   * Appel GET http://localhost:8090/trainings/{id} id correspond à l'identifiant technique de la formation
