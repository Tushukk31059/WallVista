package com.tushar.wallvista.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tushar.wallvista.databases.ImageDatabase
import com.tushar.wallvista.domain.ImageEntity
import com.tushar.wallvista.domain.LockImageEntity
import com.tushar.wallvista.repositiory.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LockImageVM(application: Application) : AndroidViewModel(application) {
    private val dao = ImageDatabase.createDatabase(application).imageDAO()
    private val repository: Repository = Repository(dao)
    val allLockImages: LiveData<List<LockImageEntity>> = repository.allLockImages

    fun addLockImage(lockImageEntity: LockImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLockImage(lockImageEntity)
        }
    }
    fun deleteImg(image: LockImageEntity){
        viewModelScope.launch {
            repository.deleteLockImage(image)
        }
    }
}