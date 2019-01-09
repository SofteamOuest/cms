package com.softeam.cms.bean

import javax.persistence.*


@Entity
@Table(name="training")
data class TrainingEntity private constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        var id: Long,
        @Lob
        @Column(name = "description_file")
        val descriptionFile : ByteArray,
        @Column(name = "training_name",length = 50)
        override var trainingName: String,
        @Column(name = "resume",length = 4000)
        override var resume: String
) : Training {
    protected constructor() : this(-1L, byteArrayOf(),"","")

    constructor(trainingName:String,descriptionFile: ByteArray,resume:String) : this(-1L, descriptionFile,trainingName,resume)
}

interface Training{
    companion object{
        const val TRAINING_NAME_MAX_LENGTH = 50
        const val DESCRIPTION_MAX_LENGTH = 4000
    }
    val trainingName: String
    val resume: String
}