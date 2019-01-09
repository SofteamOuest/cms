package com.softeam.cms.bean.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.softeam.cms.bean.Training

data class TrainingDTO @JsonCreator  constructor(override val trainingName: String, override val resume: String) : Training