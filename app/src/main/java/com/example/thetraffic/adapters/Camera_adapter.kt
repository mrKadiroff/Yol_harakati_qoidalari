package com.example.thetraffic.adapters

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.thetraffic.databinding.CamerLayoutBinding
import com.example.thetraffic.models.CameraModel
import java.io.File

class Camera_adapter(var list: List<CameraModel>,var onItemClickListener: OnItemClickListener) :RecyclerView.Adapter<Camera_adapter.Vh>() {

    inner class Vh(var cameralayoutBinding: CamerLayoutBinding) : RecyclerView.ViewHolder(cameralayoutBinding.root){

        fun onBind(cameraModel: CameraModel) {
            cameralayoutBinding.rasm.setImageURI(Uri.parse(cameraModel.rasm))
            cameralayoutBinding.name.text = cameraModel.nomi

            cameralayoutBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(cameraModel)
            }
            cameralayoutBinding.tahrirla.setOnClickListener {
                onItemClickListener.onEditClick(cameraModel, position)
            }
            cameralayoutBinding.delete.setOnClickListener {
                onItemClickListener.onDeleteClick(cameraModel, position)
            }
            cameralayoutBinding.favourite.setOnClickListener {
                onItemClickListener.onFavouriteClick(cameraModel, position,cameralayoutBinding.favourite)
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Camera_adapter.Vh {
        return Vh (CamerLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Camera_adapter.Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
    interface OnItemClickListener{
        fun onItemClick(cameraModel: CameraModel)
        fun onEditClick(cameraModel: CameraModel, position: Int)
        fun onDeleteClick(cameraModel: CameraModel, position: Int)
        fun onFavouriteClick(cameraModel: CameraModel, position: Int,checkBox: CheckBox)
    }

}