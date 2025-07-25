package com.tushar.wallvista.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lock_table")
data class LockImageEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val img:String)