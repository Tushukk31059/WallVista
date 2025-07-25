package com.tushar.wallvista.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tushar.wallvista.databases.ImageDatabase
import com.tushar.wallvista.domain.ImageEntity
import com.tushar.wallvista.repositiory.Repository
import kotlinx.coroutines.launch

import kotlinx.coroutines.Dispatchers

class ImageVM(application: Application) : AndroidViewModel(application) {
    private val dao = ImageDatabase.createDatabase(application).imageDAO()
    private val repository:Repository = Repository(dao)

    val allImages: LiveData<List<ImageEntity>> = repository.allImages

    fun addImage(imageEntity: ImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addImage(imageEntity)
        }
    }

}