package com.tushar.wallvista.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tushar.wallvista.dao.ImageDAO
import com.tushar.wallvista.domain.ImageEntity
import com.tushar.wallvista.domain.LockImageEntity

@Database(entities = [ImageEntity::class,LockImageEntity::class], version = 1, exportSchema = false)
abstract class ImageDatabase:RoomDatabase() {
    abstract fun imageDAO():ImageDAO
companion object{
    @Volatile
    var INSTANCE:ImageDatabase?=null

    fun createDatabase(context: Context):ImageDatabase{
        if (INSTANCE==null){
            synchronized(this){
                INSTANCE= Room.databaseBuilder(context.applicationContext,ImageDatabase::class.java,"image_database").build()
            }
        }
        return INSTANCE!!
    }

}
}
