package com.tushar.wallvista.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class ImageEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val img:String)

