package com.softeam.cms.service.impl

import com.softeam.cms.bean.Training
import com.softeam.cms.bean.TrainingEntity
import com.softeam.cms.bean.dto.TrainingDTO
import com.softeam.cms.dao.ITrainingDAO
import com.softeam.cms.exception.TrainingNotFoundException
import com.softeam.cms.service.ITrainingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert
import org.springframework.web.multipart.MultipartFile

@Service
class TrainingService : ITrainingService {

    @Autowired
    private lateinit var trainingDao: ITrainingDAO

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    override fun findTrainingByName(training: Training): Training {
        Assert.notNull(training,"training object cannot be null")
        return trainingDao.findTrainingByName(training.trainingName).orElse(TrainingDTO("",""))
    }

    @Transactional(propagation = Propagation.REQUIRED)
    override fun addTraining(training: Training, descriptionFile : MultipartFile): Boolean {
        Assert.notNull(training,"training object cannot be null")
        validateTrainingDescription(training)
        descriptionFile.validateMarkdown()
        return trainingDao.save(TrainingEntity(training.trainingName,descriptionFile.bytes,training.resume))!=null
    }

    private fun validateTrainingDescription(training: Training){
        Assert.state(training.resume.length<=Training.DESCRIPTION_MAX_LENGTH,"Training resume should be less than 4000 characters")
        Assert.state(training.trainingName.length<=Training.TRAINING_NAME_MAX_LENGTH,"Training name should be less than 50 characters")
    }

    private fun MultipartFile.validateMarkdown(){
        Assert.state(MediaType.TEXT_MARKDOWN_VALUE == this.contentType,"content type must be md")
        Assert.state(!this.isEmpty,"stream cannot be null")
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    override fun findTrainingById(id: Long): TrainingDTO {
        Assert.notNull(id,"id cannot be null")
        return trainingDao.findById(id).map { formation-> TrainingDTO(formation.trainingName,formation.resume)}.orElse(TrainingDTO("",""))
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    override fun findTrainingPlanning(id: Long): ByteArray {
        Assert.notNull(id,"id cannot be null")
        return trainingDao.findById(id).map { formation-> formation.descriptionFile}.orElseThrow{ TrainingNotFoundException("Training formation wasn't found") }
    }

}