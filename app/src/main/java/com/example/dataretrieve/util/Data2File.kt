package com.example.dataretrieve.util

import android.content.Context
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object Data2File {

    fun Data2txt(context:Context, message: String, fileName: String){
        try {
            val file : File= File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS),
                "$fileName.txt"
            )
            if (!file.exists()) {
                file.createNewFile()
            }

            val fos = FileOutputStream(file, true)
            fos.write(message.toByteArray())
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }




}