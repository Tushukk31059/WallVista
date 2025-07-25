package com.tushar.wallvista.ui.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.tushar.wallvista.adapter.ImageRecycler
import com.tushar.wallvista.databinding.FragmentHomeBinding
import com.tushar.wallvista.domain.ImageEntity
import com.tushar.wallvista.viewmodels.ImageVM
import com.tushar.wallvista.workers.WallWorker
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ImageRecycler
    private lateinit var viewModel: ImageVM
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val file=saveImage(it)
                file?.let{

                    viewModel.addImage(ImageEntity(img = it.absolutePath))
                }
            }

        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ImageVM::class.java]
        adapter = ImageRecycler(emptyList())
        binding.pickBtn.setOnClickListener {
            pickImageFromGallery()
        }
        viewModel.allImages.observe(viewLifecycleOwner) {

            binding.rec.layoutManager = GridLayoutManager(requireContext(),3)
            binding.rec.adapter = adapter

        }
        viewModel.allImages.observe(viewLifecycleOwner) { images ->
            adapter.updateList(images)
        }
        val request= PeriodicWorkRequestBuilder<WallWorker>(
            15,TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "wallpaper_work",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }


    private fun saveImage(uri: Uri): File?{
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, "IMG_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun pickImageFromGallery() {
        imagePicker.launch("image/*")
    }

}
