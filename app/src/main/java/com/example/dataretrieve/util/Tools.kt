package com.example.dataretrieve.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Tools {

    fun getDate2String(time: Long): String? {
        val pattern : String = "yyyy年 MM月 dd日 HH:mm:ss"
        val date = Date(time)
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }

    fun getDuration2String(time: Long): String{
        val totalSeconds  = time / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        //val seconds = totalSeconds % 60
        var seconds : Double = Math.ceil((time / 1000.0) % 60)
        return String.format("%02d小时，%02d分钟，%02d秒", hours, minutes, seconds.toLong())
    }

}