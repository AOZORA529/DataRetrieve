package com.example.dataretrieve.util

import android.app.usage.UsageEvents
import android.app.usage.UsageEvents.Event
import android.app.usage.UsageStatsManager
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.dataretrieve.bean.AppUsageInfo
import com.example.dataretrieve.bean.DatabaseHelper
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import kotlin.collections.*


object DataRetrieve {


    // 获取每个应用的前台使用时间以及最后一次的使用时间
    // 通过 getSystemService() 提供的接口获得数据
    fun getQueryAndAggregateUsageStatsTest(context: Context) {

        // Get the current time in milliseconds
        val currentTime = System.currentTimeMillis()

        // Set the start time to one day ago
        val startTime = currentTime - 24 * 60 * 60 * 1000 * 15

        // Set the end time to the current time
        val endTime = currentTime

        // Get the UsageStatsManager instance from the context
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // Query the usage stats for the given time interval
        val usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startTime, endTime)
        val usageStatsList = usageStatsMap.values.sortedByDescending { it.totalTimeInForeground }

        // Loop through the usage stats list
        for (usageStats in usageStatsList) {
            // Get the app package name, total time in foreground, and last time used
            val packageName = usageStats.packageName
            val totalTimeInForeground = usageStats.totalTimeInForeground
            val lastTimeUsed = usageStats.lastTimeUsed


            // Format the output message
            val message =
                "App: $packageName, \n" +
                        "Total time in foreground: ${Tools.getDuration2String(totalTimeInForeground)} ms, \n" +
                        "Last time used: ${Tools.getDate2String(lastTimeUsed)}"



            // 输出应用使用信息到文件
            try {
                val output =
                    context.openFileOutput("queryAndAggregateUsageStats", Context.MODE_APPEND)
                val writer = BufferedWriter(OutputStreamWriter(output))
                writer.use {
                    it.append(message + "\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // Output the message into log with tag "AppsUsageInfo"
            Log.i("AppsUsageInfo", message)
        }
    }


    fun getQueryUsageStatsTest(context: Context) {
        // Get the current time in milliseconds
        val currentTime = System.currentTimeMillis()

        // Set the start time to one day ago
        val startTime = currentTime - 24 * 60 * 60 * 1000 * 15

        // Set the end time to the current time
        val endTime = currentTime

        // Get the UsageStatsManager instance from the context
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // Query the usage stats for the given time interval
        val usageStatsList =
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)

        // Loop through the usage stats list
        for (usageStats in usageStatsList) {
            // Get the app package name, total time in foreground, and last time used
            val packageName = usageStats.packageName
            val totalTimeInForeground = usageStats.totalTimeInForeground
            val lastTimeUsed = usageStats.lastTimeUsed
            val firstTimeStamp = usageStats.firstTimeStamp
            val lastTimeStamp = usageStats.lastTimeStamp
            val totalTimeForegroundServiceUsed = usageStats.totalTimeForegroundServiceUsed
            val totalTimeVisible =  usageStats.totalTimeVisible

            // Format the output message
            val message =
                "App: $packageName: \n" +
                        "   lastTimeUsed: ${Tools.getDate2String(lastTimeUsed)} \n" +
                        "   firstTimeStamp: ${Tools.getDate2String(firstTimeStamp)} \n" +
                        "   lastTimeStamp: ${Tools.getDate2String(lastTimeStamp)} \n" +
                        "   totalTimeInForeground: ${Tools.getDuration2String(totalTimeInForeground)} \n" +
                        "   totalTimeForegroundServiceUsed: ${Tools.getDuration2String(totalTimeForegroundServiceUsed)} \n" +
                        "   totalTimeVisible:  ${Tools.getDuration2String(totalTimeVisible)}"



            // 输出应用使用信息到文件
            try {
                val output = context.openFileOutput("queryUsageStats", Context.MODE_APPEND)
                val writer = BufferedWriter(OutputStreamWriter(output))
                writer.use {
                    it.append(message + "\n")

                }
            } catch (e: IOException) {
                e.printStackTrace()
            }


            // Output the message into log with tag "AppsUsageInfo"
            Log.i("AppsUsageInfo", message)
        }
    }

    fun getQueryEventsRow(context: Context) {
        // Get the current time in milliseconds
        val currentTime = System.currentTimeMillis()

        // Set the start time to one day ago
        val startTime = currentTime - 24 * 60 * 60 * 1000 * 15

        // Set the end time to the current time
        val endTime = currentTime

        // Get the UsageStatsManager instance from the context
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // Query the usage stats for the given time interval
        val events = usageStatsManager.queryEvents(startTime, endTime)

        // Loop through the usage stats list
        while (events.hasNextEvent()) {

            var event : Event = Event()
            events.getNextEvent(event)


            // Format the output message
            val message =
                "App: ${event.packageName}: \n" +
                        "   eventType: ${event.eventType} \n" +
                        "   timeStamp: ${Tools.getDate2String(event.timeStamp)} \n "


            // 输出应用使用信息到文件
            Data2File.Data2txt(context, message, "EventsRow")
//            try {
//                val output = context.openFileOutput("queryEventsTest", Context.MODE_APPEND)
//                val writer = BufferedWriter(OutputStreamWriter(output))
//                writer.use {
//                    it.write(message + "\n")
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }

            // Output the message into log with tag "AppsUsageInfo"
            //Log.i("AppsUsageInfo", message)
        }
    }

    // 查询全部时间的 Events（从 1970 到现在），利用这些 Events 统计 app 使用情况
    fun getQueryEvents(context: Context){

        // 当前时间（区间结束时间）
        val currentTime = System.currentTimeMillis()

        // 开始时间（区间开始时间）
        val startTime = currentTime - 24 * 60 * 60 * 1000 * 15

        // 根据 package 名称聚合 Event
        var aggregatedEventsMap = mutableMapOf<String, MutableList<Event>>()

        // 获取指定时间区间内的 UsageStatsManager
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // 获取指定 UsageStatsManager 中的 Events 列表
        val rawEventsList = usageStatsManager.queryEvents(startTime, currentTime)

        // 遍历 rawEvents 并将所有的 Events 归类
        while (rawEventsList.hasNextEvent()){

            var tempEvent = Event()

            // 获取 rawEvents 里的下一个 Event 并将其赋值给 tempEvent
            rawEventsList.getNextEvent(tempEvent)

            // 获取指定包名的事件列表，如果不存在，则创建一个新的列表
            val tempEventsList = aggregatedEventsMap[tempEvent.packageName] ?: mutableListOf()

            // 把 tempEvent 装入 tempEvents 列表中
            tempEventsList.add(tempEvent)

            // 把 tempEvents 装入 aggregatedEvents 映射中
            aggregatedEventsMap[tempEvent.packageName] = tempEventsList
        }

        // 根据 Event 的 type 进行数据整理和统计
        // ACTIVITY_RESUMED → 1： 一个 Activity 切换到前台
        // ACTIVITY_PAUSED  → 2： 一个 Activity 切换到后台
        // ACTIVITY_STOPED  → 23: 一个 Activity 被终止
        var statsEventsMap = mutableMapOf<String, AppUsageInfo>()

        // 数据库存储准备
        val dbHelper = DatabaseHelper(context, "BookStore.db", 1)
        val dbDatabase = dbHelper.writableDatabase

        //遍历每一个 app
        aggregatedEventsMap.forEach { app, tempEventsList ->

            // 前台运行总时间
            var timeForeground = 0L

            // 存储每一次打开的时间点
            var timestampStart = mutableListOf<Long>()

            // 存储每一次打开的持续时间
            var timeDurationAggregated = mutableListOf<Long>()

            // 计算每次打开后的持续时间
            var start = 0L
            var end = 0L

            // 打开次数
            var countStart = 0

            // 遍历每一个 app 对应的 Events 列表
            tempEventsList.forEach {

                // 当前 Event 代表应用启动
                if(it.eventType == UsageEvents.Event.ACTIVITY_RESUMED){
                    start = it.timeStamp
                }
                // 当前 Event 代表应用暂停、终止
//                else if(it.eventType == UsageEvents.Event.ACTIVITY_STOPPED){
//                    end = it.timeStamp
//                }
                else if((it.eventType == UsageEvents.Event.ACTIVITY_STOPPED
                            || it.eventType == UsageEvents.Event.ACTIVITY_PAUSED)
                    && start != 0L){
                    end = it.timeStamp
                }

                // 当 end 不等于 0 的时候开始进行数据统计
                if(end != 0L){
                    // 特殊情况①，该 app 的 Events 列表以 PAUSED 或者 STOPPED 开头
//                    if(start == 0L){
//                        start = startTime
//
//                    }
                    // 数据处理
                    var duration = end - start
                    timeForeground += duration
                    countStart++
                    timestampStart.add(start)
                    timeDurationAggregated.add(duration)

//                    Log.i("event", "duration: ${duration}\n" +
//                                            "countOpen: $countOpen \n" +
//                                            "start: ${Tools.getDate2String(start)}\n" +
//                                            "end: ${Tools.getDate2String(end)}\n" +
//                                            "foreground: ${timeForeground}")

                    // 数据存储
//                    val data = ContentValues().apply {
//                        put("app", it.packageName)
//                        put("time_stamp", start)
//                        put("time_duration", duration)
//                    }
//                    dbDatabase.insert("app_usage_info", null, data)
//                    "id integer primary key autoincrement," +
//                            "app text," +
//                            "time_stamp text," +
//                            "time_duration integer"

                    // 为下一次遍历做准备
                    start = 0L
                    end = 0L
                }
            }
            // 特殊情况②，Events 列表的最后一个是 START
//            if(start != 0L){
//                end = currentTime
//                countOpen++
//                timeForeground += end - start
//                timestampStart.add(start)
//                timeDurationAggregated.add(end - start)
//            }
            if(start != 0L && countStart != 0){
                end = currentTime
                countStart++
                timeForeground += end - start
                timestampStart.add(start)
                timeDurationAggregated.add(end - start)
            }


            // 该应用的 Events 已经处理完毕，可以存入 statsEventsMap
            val appUsageInfo = AppUsageInfo(timeForeground,
                                            timestampStart,
                                            timeDurationAggregated,
                                            countStart)

            // 输出应用使用信息到文件
            var message = "app: $app \n" +
                            "  timeForeground: ${Tools.getDuration2String(timeForeground)} \n" +
                            "  countOpen: $countStart \n"

            appUsageInfo.timestampOpen.forEachIndexed { index, timestamp ->
                var duration = appUsageInfo.timeDurationAggregated[index]
                message += "    timestamp: ${Tools.getDate2String(timestamp)} \n" +
                           "    duration: ${Tools.getDuration2String(duration)} \n" +
                           "    duration: $duration \n"
            }

            Data2File.Data2txt(context, message, "EventsStats")


//            try {
//                val output = context.openFileOutput("queryEvents", Context.MODE_APPEND)
//                val writer = BufferedWriter(OutputStreamWriter(output))
//                writer.use {
//                    it.write(message + "\n")
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }

//            try {
//                val file = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "EventsStats.txt")
//                if (!file.exists()) {
//                    file.createNewFile()
//                }
//
//                val fos = FileOutputStream(file, true)
//                fos.write(message.toByteArray())
//                fos.close()
//
//
////                var path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() +
////                        File.separatorChar +
////                        "EventsStats.txt"
////
////                val output = context.openFileOutput(path, Context.MODE_APPEND)
////                val writer = BufferedWriter(OutputStreamWriter(output))
////                writer.use {
////                    it.write(message + "\n")
////                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }

            // 把本次循环中处理完成这个 app 放入 statsEventsMap 中
            statsEventsMap[app] = appUsageInfo

        }
    }

    fun getQueryEvents2DB(context: Context){

        // 当前时间（区间结束时间）
        val currentTime = System.currentTimeMillis()

        // 开始时间（区间开始时间）
        val startTime = currentTime - 24 * 60 * 60 * 1000 * 15

        // 根据 package 名称聚合 Event
        var aggregatedEventsMap = mutableMapOf<String, MutableList<Event>>()

        // 获取指定时间区间内的 UsageStatsManager
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // 获取指定 UsageStatsManager 中的 Events 列表
        val rawEventsList = usageStatsManager.queryEvents(startTime, currentTime)

        // 遍历 rawEvents 并将所有的 Events 归类
        while (rawEventsList.hasNextEvent()){

            var tempEvent = Event()

            // 获取 rawEvents 里的下一个 Event 并将其赋值给 tempEvent
            rawEventsList.getNextEvent(tempEvent)

            // 获取指定包名的事件列表，如果不存在，则创建一个新的列表
            val tempEventsList = aggregatedEventsMap[tempEvent.packageName] ?: mutableListOf()

            // 把 tempEvent 装入 tempEvents 列表中
            tempEventsList.add(tempEvent)

            // 把 tempEvents 装入 aggregatedEvents 映射中
            aggregatedEventsMap[tempEvent.packageName] = tempEventsList
        }

        // 根据 Event 的 type 进行数据整理和统计
        // ACTIVITY_RESUMED → 1： 一个 Activity 切换到前台
        // ACTIVITY_PAUSED  → 2： 一个 Activity 切换到后台
        // ACTIVITY_STOPED  → 23: 一个 Activity 被终止
        var statsEventsMap = mutableMapOf<String, AppUsageInfo>()

        // 数据库存储准备
        val dbHelper = DatabaseHelper(context, "app_usage_info.db", 1)
        val dbDatabase = dbHelper.writableDatabase

        //遍历每一个 app
        aggregatedEventsMap.forEach { app, tempEventsList ->

            // 前台运行总时间
            var timeForeground = 0L

            // 存储每一次打开的时间点
            var timestampStart = mutableListOf<Long>()

            // 存储每一次打开的持续时间
            var timeDurationAggregated = mutableListOf<Long>()

            // 计算每次打开后的持续时间
            var start =startTime
            var end = 0L

            // 打开次数
            var countStart = 0

            // 遍历每一个 app 对应的 Events 列表
            tempEventsList.forEach {

                // 当前 Event 代表应用启动
                if(it.eventType == UsageEvents.Event.ACTIVITY_RESUMED){
                    start = it.timeStamp
                }
                // 当前 Event 代表应用暂停、终止
                else if((it.eventType == UsageEvents.Event.ACTIVITY_STOPPED
                    || it.eventType == UsageEvents.Event.ACTIVITY_PAUSED)
                    && start != 0L){
                    end = it.timeStamp
                }

                // 当 end 不等于 0 的时候开始进行数据统计
                if(end != 0L){
                    // 特殊情况①，该 app 的 Events 列表以 PAUSED 或者 STOPPED 开头
//                    if(start == 0L){
//                        start = startTime
//
//                    }
                    // 数据处理
                    var duration = end - start
                    timeForeground += duration
                    countStart++
                    timestampStart.add(start)
                    timeDurationAggregated.add(duration)

                    Log.i("event", "duration: ${duration}\n" +
                            "countOpen: $countStart \n" +
                            "start: ${Tools.getDate2String(start)}\n" +
                            "end: ${Tools.getDate2String(end)}\n" +
                            "foreground: ${timeForeground}")

                    // 数据存储
                    val data = ContentValues().apply {
                        put("app", it.packageName)
                        put("time_stamp", start)
                        put("time_duration", duration)
                    }
                    dbDatabase.insert("app_usage_info", null, data)
                    "id integer primary key autoincrement," +
                            "app text," +
                            "time_stamp text," +
                            "time_duration integer"

                    // 为下一次遍历做准备
                    start = 0L
                    end = 0L
                }
            }
            // 特殊情况②，Events 列表的最后一个是 START
            if(start != 0L && countStart != 0){
                end = currentTime
                countStart++
                timeForeground += end - start
                timestampStart.add(start)
                timeDurationAggregated.add(end - start)
            }


            // 该应用的 Events 已经处理完毕，可以存入 statsEventsMap
            val appUsageInfo = AppUsageInfo(timeForeground,
                timestampStart,
                timeDurationAggregated,
                countStart)

            // 输出应用使用信息到文件
            var message = "app: $app \n" +
                    "  timeForeground: ${Tools.getDuration2String(timeForeground)} \n" +
                    "  countOpen: $countStart \n"

            appUsageInfo.timestampOpen.forEachIndexed { index, timestamp ->
                var duration = appUsageInfo.timeDurationAggregated[index]
                message += "    timestamp: ${Tools.getDate2String(timestamp)} \n" +
                        "    duration: ${Tools.getDuration2String(duration)} \n" +
                        "    duration: $duration \n"
            }

            try {
                val output = context.openFileOutput("queryEvents", Context.MODE_APPEND)
                val writer = BufferedWriter(OutputStreamWriter(output))
                writer.use {
                    it.write(message + "\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // 把本次循环中处理完成这个 app 放入 statsEventsMap 中
            statsEventsMap[app] = appUsageInfo

        }
    }

}








