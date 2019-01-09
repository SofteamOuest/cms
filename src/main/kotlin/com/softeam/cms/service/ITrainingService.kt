package com.softeam.cms.service

import com.softeam.cms.bean.Training
import com.softeam.cms.bean.dto.TrainingDTO
import org.springframework.web.multipart.MultipartFile

interface ITrainingService {

    /****
     * Find a training description by a training name #Training
     * @param name name of training formation
     * @return #Training
     */
    fun findTrainingByName(training: Training) : Training

    /****
     * Add training formation in markdown file with description #com.softeam.cms.bean.dto.TrainingDTO
     * @param descriptionFile upload markdown file
     * @param training description of training formation
     *
     * @return true if well inserted
     */
    fun addTraining(training: Training, descriptionFile : MultipartFile) : Boolean

    /****
     * Find a training description by a training Id #TrainingDTO
     * @param id technical id of training formation
     * @return #TrainingDTO
     */
    fun findTrainingById(id: Long): TrainingDTO

    /****
     * Find a training planning a markdown file by a training Id
     * @param id technical id of training formation
     * @return markdown file to download
     * @throws #com.softeam.cms.exception.TrainingNotFoundException
     */
    fun findTrainingPlanning(id: Long): ByteArray
}