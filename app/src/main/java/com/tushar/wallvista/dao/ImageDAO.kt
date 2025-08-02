package com.tushar.wallvista.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tushar.wallvista.domain.ImageEntity
import com.tushar.wallvista.domain.LockImageEntity

@Dao
interface ImageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(image: ImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleImage(obj:ImageEntity)
    @Query("SELECT * FROM IMAGE_TABLE")
    fun getImage():LiveData<List<ImageEntity>>

    @Query("SELECT * FROM IMAGE_TABLE")
    fun getImages():List<ImageEntity>

    @Delete
    suspend fun delete(photo: ImageEntity)

    @Query("SELECT COUNT(*) FROM IMAGE_TABLE")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLockImage(image: LockImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleLockImage(obj:LockImageEntity)

    @Delete
    suspend fun deleteLockImage(photo: LockImageEntity)
    @Query("SELECT * FROM LOCK_TABLE")
    fun getLockImage():LiveData<List<LockImageEntity>>

    @Query("SELECT * FROM LOCK_TABLE")
    fun getLockImages():List<LockImageEntity>

    @Query("SELECT COUNT(*) FROM LOCK_TABLE")
    suspend fun getLockCount(): Int
}
