package com.softeam.cms.service.impl

import com.softeam.cms.bean.dto.TrainingDTO
import com.softeam.cms.dao.ITrainingDAO
import com.softeam.cms.exception.TrainingNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import java.util.stream.IntStream


@RunWith(MockitoJUnitRunner::class)
class TrainingServiceTest {

    @InjectMocks
    private lateinit var trainingService: TrainingService

    @Mock
    private lateinit var dao: ITrainingDAO


    @Test
    fun shouldReturnEmptyTraining() {
        var training = trainingService.findTrainingById(-2)
        assertThat(training.trainingName).isEqualTo("")
        assertThat(training.resume).isEqualTo("")
    }

    @Test
    fun shouldThrowExceptionWhenNoTrainingPlanFound() {
        // execution and validation
        assertThatThrownBy {   trainingService.findTrainingPlanning(-6) }
                .isInstanceOf(TrainingNotFoundException::class.java)
                .hasMessage("Training formation wasn't found")
    }

    @Test
    fun shouldThrowExceptionWhenNoFileAdded() {
        // execution and validation
        assertThatThrownBy {  trainingService.addTraining(TrainingDTO("first training formation","a resume"), MockMultipartFile("contentName","contentName", MediaType.TEXT_MARKDOWN_VALUE,null)) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("stream cannot be null")
    }

    @Test
    fun shouldThrowExceptionWhenWrongContentType() {
        // execution and validation
        assertThatThrownBy {  trainingService.addTraining(TrainingDTO("first training formation","a resume"), MockMultipartFile("contentName","contentName", MediaType.APPLICATION_JSON_VALUE,"{}".byteInputStream())) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("content type must be md")
    }

    @Test
    fun shouldThrowExceptionWhenDescriptionTooLong() {
        //preparing objects
        var buildDescription=StringBuilder()
        IntStream.range(0,4001).forEach{buildDescription.append("t")}

        // execution and validation
        assertThatThrownBy {  trainingService.addTraining(TrainingDTO("first training formation",buildDescription.toString()), MockMultipartFile("contentName","contentName", MediaType.TEXT_MARKDOWN_VALUE,"#125".byteInputStream())) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Training resume should be less than 4000 characters")
    }

    @Test
    fun whenNameTooLongThenThrowException() {

        // preparing objects
        var buildName=StringBuilder()
        IntStream.range(0,51).forEach{buildName.append("t")}

        // execution and validation
        assertThatThrownBy {  trainingService.addTraining(TrainingDTO(buildName.toString(),"description"), MockMultipartFile("contentName","contentName", MediaType.TEXT_MARKDOWN_VALUE,"#125".byteInputStream())) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Training name should be less than 50 characters")
    }

}