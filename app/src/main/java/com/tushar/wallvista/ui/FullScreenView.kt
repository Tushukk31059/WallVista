package com.tushar.wallvista.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tushar.wallvista.R
import com.tushar.wallvista.databinding.ActivityFullScreenViewBinding
import java.io.File

class FullScreenView : AppCompatActivity() {
    private lateinit var binding:ActivityFullScreenViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityFullScreenViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val uri=intent.getStringExtra("uri")
        val file= File(uri?:"")
        if(file.exists()){
            Glide.with(this)
                .load(uri)
                .into(binding.img)

        }
    }
}