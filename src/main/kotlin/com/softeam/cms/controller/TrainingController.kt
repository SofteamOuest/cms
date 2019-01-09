package com.softeam.cms.controller

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.softeam.cms.bean.dto.TrainingDTO
import com.softeam.cms.exception.TechnicalException
import com.softeam.cms.service.ITrainingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("trainings")
class TrainingController {

    @Autowired
    private lateinit var trainingService: ITrainingService

    @Autowired
    private lateinit var mapper : ObjectMapper

    @GetMapping("/{id}")
    fun downloadTrainingFormation(@PathVariable (value = "id") id : Long,response: HttpServletResponse){
        response.contentType=MediaType.TEXT_MARKDOWN_VALUE
        response.outputStream.use { it.write(trainingService.findTrainingPlanning(id)) }
    }

    @PostMapping("/")
    fun addTrainging(@RequestParam(value = "training") training : String,@RequestParam(value = "description") description: MultipartFile){
        try{
            trainingService.addTraining(mapper.readValue(training, TrainingDTO::class.java),description)
        }catch(e: JsonMappingException){
            throw TechnicalException("Error while parsing training formation",e)
        }
    }

}