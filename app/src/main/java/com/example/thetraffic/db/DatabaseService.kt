package com.example.thetraffic.db

import com.example.thetraffic.models.CameraModel

interface DatabaseService {

    fun insertCamera(cameraModel: CameraModel)

    fun deleteCamera(cameraModel: CameraModel)

    fun updateCamera(cameraModel: CameraModel):Int

    fun getAllCamera(): List<CameraModel>
}