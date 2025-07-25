package com.tushar.wallvista.repositiory

import androidx.lifecycle.LiveData
import com.tushar.wallvista.dao.ImageDAO
import com.tushar.wallvista.domain.ImageEntity
import com.tushar.wallvista.domain.LockImageEntity


class Repository(private val dao: ImageDAO) {
    val allImages: LiveData<List<ImageEntity>> = dao.getImage()
    val allLockImages: LiveData<List<LockImageEntity>> = dao.getLockImage()
    fun getImages():List<ImageEntity>{
        return dao.getImages()
    }
    fun getLockImages():List<LockImageEntity>{
        return dao.getLockImages()
    }
    suspend fun addImage(imageEntity: ImageEntity) {
            println("RoomInsert Inserting URI: $imageEntity")
            dao.insertImage(imageEntity)
        }
    suspend fun addLockImage(lockImageEntity: LockImageEntity) {
        println("RoomInsert Inserting URI: $lockImageEntity")
        dao.insertLockImage(lockImageEntity)
    }
    }
