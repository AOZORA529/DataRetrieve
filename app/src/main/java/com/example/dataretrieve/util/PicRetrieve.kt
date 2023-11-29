package com.example.dataretrieve.util

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter


object PicRetrieve {
    // 获取所有已安装应用的图片
    fun getAppPic (context: Context){
        val appInfoList = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        appInfoList.forEach {
            val icon: Drawable = context.packageManager.getApplicationIcon(it.packageName)

            var bitmap = Bitmap.createBitmap(icon.intrinsicWidth, icon.intrinsicHeight, Bitmap.Config.ARGB_8888);
//            var canvas = Canvas(bitmap);
//            icon.setBounds(0, 0, canvas.width, canvas.height);
//            icon.draw(canvas);


            val file = File(context.filesDir, "/pic/${it.packageName}.png")


            // 保存到文件
//            val file = File("/icon", "${it.packageName}.png")
//            val outStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
//            outStream.flush()
//            outStream.close()
        }
    }
}