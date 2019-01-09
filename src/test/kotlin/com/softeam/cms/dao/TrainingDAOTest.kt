package com.softeam.cms.dao

import com.softeam.cms.TestConfig
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameterValue
import org.springframework.jdbc.core.support.SqlLobValue
import org.springframework.jdbc.support.lob.DefaultLobHandler
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.io.FileInputStream
import java.sql.Types

@RunWith(SpringRunner::class)
@SpringBootTest
@Import(TestConfig::class)
class TrainingDAOTest{

    @Autowired
    lateinit var dao: ITrainingDAO
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

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

    @After
    fun tearDown() {
        this.jdbcTemplate.update("delete from training where id=?", SqlParameterValue(Types.NUMERIC,  idTraining))
    }

    @Test
    fun shouldReturnTraining() {
        var result = dao.findById(idTraining)
        Assertions.assertThat(result.isPresent).isTrue()
        var training = result.get()
        Assertions.assertThat(training.trainingName).isEqualTo(trainingName)
        Assertions.assertThat(training.resume).isEqualTo(descriptionTraining)
        Assertions.assertThat(training.descriptionFile).isNotEmpty()
    }

}