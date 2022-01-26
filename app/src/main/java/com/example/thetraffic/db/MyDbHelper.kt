package com.example.thetraffic.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.thetraffic.models.CameraModel
import com.example.thetraffic.utils.Constant

class MyDbHelper(context: Context) :
    SQLiteOpenHelper(context, Constant.DB_NAME, null, Constant.DB_VERSION), DatabaseService{
    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "create table ${Constant.TABLE_NAME} (${Constant.ID} integer not null primary key autoincrement unique, ${Constant.PHOTO} text not null, ${Constant.NAME} text not null, ${Constant.DESCRIPTION} text not null, ${Constant.CATEGORY} text not null, ${Constant.LIKED} text not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exists ${Constant.TABLE_NAME}")
        onCreate(db)
    }

    override fun insertCamera(cameraModel: CameraModel) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.PHOTO, cameraModel.rasm)
        contentValues.put(Constant.NAME, cameraModel.nomi)
        contentValues.put(Constant.DESCRIPTION, cameraModel.malumot)
        contentValues.put(Constant.CATEGORY, cameraModel.kategoriya)
        contentValues.put(Constant.LIKED, cameraModel.like)
        database.insert(Constant.TABLE_NAME, null, contentValues)
        database.close()
    }

    override fun deleteCamera(cameraModel: CameraModel) {
        val database = this.writableDatabase
        database.delete(Constant.TABLE_NAME, "${Constant.ID} = ?", arrayOf("${cameraModel.id}"))
        database.close()
    }

    override fun updateCamera(cameraModel: CameraModel): Int {
        val database = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Constant.ID, cameraModel.id)
        contentValues.put(Constant.PHOTO, cameraModel.rasm)
        contentValues.put(Constant.NAME, cameraModel.nomi)
        contentValues.put(Constant.CATEGORY, cameraModel.kategoriya)
        contentValues.put(Constant.LIKED, cameraModel.like)
        return database.update(
            Constant.TABLE_NAME,
            contentValues,
            "${Constant.ID} = ?",
            arrayOf(cameraModel.id.toString())
        )
    }

    override fun getAllCamera(): ArrayList<CameraModel> {
        val list = ArrayList<CameraModel>()
        val query = "select * from ${Constant.TABLE_NAME}"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val photo = cursor.getString(1)
                val name = cursor.getString(2)
                val description = cursor.getString(3)
                val category = cursor.getString(4)
                val like = cursor.getString(5)
                val camera = CameraModel(id, photo, name, description, category,like)
                list.add(camera)
            } while (cursor.moveToNext())
        }
        return list
    }
}