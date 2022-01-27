package com.example.thetraffic.adapters

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.example.thetraffic.R
import com.example.thetraffic.databinding.CamerLayoutBinding
import com.example.thetraffic.db.MyDbHelper
import com.example.thetraffic.models.CameraModel

lateinit var myDbHelper: MyDbHelper

class Camera_adapter(var list: List<CameraModel>,var onItemClickListener: OnItemClickListener) :RecyclerView.Adapter<Camera_adapter.Vh>() {


    inner class Vh(var cameralayoutBinding: CamerLayoutBinding) : RecyclerView.ViewHolder(cameralayoutBinding.root){

        fun onBind(cameraModel: CameraModel) {
            myDbHelper = MyDbHelper(cameralayoutBinding.root.context)



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

            cameralayoutBinding.bookmarkBtn.setOnClickListener {
                onItemClickListener.onFavouriteClick(cameralayoutBinding, cameraModel, adapterPosition)
            }
            cameralayoutBinding.bookmarkBtn.setImageResource(cameraModel.like!!)

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

        fun onFavouriteClick(cameralayoutBinding: CamerLayoutBinding, cameraModel: CameraModel,position: Int)
    }



}