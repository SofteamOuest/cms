package com.softeam.cms.dao

import com.softeam.cms.bean.Training
import com.softeam.cms.bean.TrainingEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.Optional

interface ITrainingDAO : CrudRepository<TrainingEntity, Long> {

    @Query("select training from TrainingEntity as training where training.trainingName=:propTrainingName")
    fun findTrainingByName(@Param("propTrainingName") trainingName: String): Optional<Training>
}