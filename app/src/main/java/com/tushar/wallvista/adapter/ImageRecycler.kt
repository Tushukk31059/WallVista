package com.tushar.wallvista.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tushar.wallvista.databinding.SingleWallpaperItemBinding
import com.tushar.wallvista.domain.ImageEntity
import androidx.core.net.toUri
import com.tushar.wallvista.ui.FullScreenView
import com.tushar.wallvista.viewmodels.ImageVM
import java.io.File
import androidx.core.content.edit

class ImageRecycler(private var list: List<ImageEntity>, private val viewmodel: ImageVM) :
    RecyclerView.Adapter<ImageRecycler.ViewHolder>() {
    class ViewHolder(val binding: SingleWallpaperItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SingleWallpaperItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val uri = File(list[position].img)
        Glide.with(holder.itemView.context)
            .load(uri.toUri())
            .into(holder.binding.img)
        holder.binding.img.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, FullScreenView::class.java)

            intent.putExtra("position", position)
            intent.putExtra("uri",uri.absolutePath)
            context.startActivity(intent)
        }
        holder.binding.img.setOnLongClickListener {
            if (position!= RecyclerView.NO_POSITION){
                val item=list[position]
                AlertDialog.Builder(holder.binding.root.context)
                    .setTitle("DELETE")
                    .setMessage("Do You Want To Delete This Image?")
                    .setPositiveButton("YES"){ _, _ ->
                        viewmodel.deleteImg(item)
                    }
                    .setNegativeButton("NO",null)
                    .show()
            }
            true
        }
    }



    fun updateList(newList: List<ImageEntity>) {
        val diffUtilCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = list.size
            override fun getNewListSize(): Int = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition].id == newList[newItemPosition].id
            }
            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return list[oldItemPosition] == newList[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

}