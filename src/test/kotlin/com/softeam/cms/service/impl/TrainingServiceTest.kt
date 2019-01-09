package com.softeam.cms.service.impl

import com.softeam.cms.CmsApplication
import com.softeam.cms.bean.dto.TrainingDTO
import com.softeam.cms.exception.TrainingNotFoundException
import com.softeam.cms.service.ITrainingService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameterValue
import org.springframework.jdbc.core.support.SqlLobValue
import org.springframework.jdbc.support.lob.DefaultLobHandler
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileInputStream
import java.lang.IllegalStateException
import java.sql.Types
import java.util.stream.IntStream

@RunWith(SpringRunner::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(classes = arrayOf(CmsApplication::class))
@Transactional
class TrainingServiceTest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var trainingService: ITrainingService

    private val idTraining=-1L

    private val descriptionTraining="a testing training"

    private val trainingName="training test"

    @Before
    fun setUp() {
        var readme = File(this.javaClass.classLoader.getResource("README.md").file)
        var streamReadme = FileInputStream(readme)
        this.jdbcTemplate.update("insert into training (id,training_name,resume,description_file) values (?,?,?,?)",
                SqlParameterValue(Types.NUMERIC,  idTraining),
                SqlParameterValue(Types.VARCHAR,  trainingName),
                SqlParameterValue(Types.VARCHAR,  descriptionTraining),
                 SqlParameterValue(Types.BLOB,  SqlLobValue(streamReadme,readme.length().toInt(), DefaultLobHandler())))
    }

    @Test
    fun shouldReturnTraining() {
        var training = trainingService.findTrainingById(idTraining)
        assertNotNull(training)
        assertEquals(trainingName,training.trainingName)
        assertEquals(descriptionTraining,training.resume)
    }

    @Test
    fun shouldReturnEmptyTraining() {
        var training = trainingService.findTrainingById(-2)
        assertNotNull(training)
        assertEquals("",training.trainingName)
        assertEquals("",training.resume)
    }

    @Test
    fun shouldReturnFormationPlanning() {
        var trainingPlanning = trainingService.findTrainingPlanning(idTraining)
        assertNotNull(trainingPlanning)
    }

    @Test(expected = TrainingNotFoundException::class)
    fun shouldThrowExceptionWhenNoTrainingPlanFound() {
        trainingService.findTrainingPlanning(-6)
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowExceptionWhenNoFileAdded() {
        trainingService.addTraining(TrainingDTO("first training formation","a resume"), MockMultipartFile("contentName","contentName", MediaType.TEXT_MARKDOWN_VALUE,null))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowExceptionWhenWrongContentType() {
        trainingService.addTraining(TrainingDTO("first training formation","a resume"), MockMultipartFile("contentName","contentName", MediaType.APPLICATION_JSON_VALUE,"{}".byteInputStream()))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowExceptionDescriptionTooLong() {
        var buildDescription=StringBuilder()
        IntStream.range(0,4001).forEach{buildDescription.append("t")}
        trainingService.addTraining(TrainingDTO("first training formation",buildDescription.toString()), MockMultipartFile("contentName","contentName", MediaType.TEXT_MARKDOWN_VALUE,"#125".byteInputStream()))
    }

    @Test
    fun whenNameTooLongThenThrowException() {

        // given
        var buildName=StringBuilder()
        IntStream.range(0,51).forEach{buildName.append("t")}

        // when
        trainingService.addTraining(TrainingDTO(buildName.toString(),"description"), MockMultipartFile("contentName","contentName", MediaType.TEXT_MARKDOWN_VALUE,"#125".byteInputStream()))

        // then
        // asserts ici
    }

}