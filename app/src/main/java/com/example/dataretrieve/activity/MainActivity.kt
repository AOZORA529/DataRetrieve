package com.example.dataretrieve.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.provider.Settings
import android.app.AppOpsManager
import android.net.Uri
import com.example.dataretrieve.R
import com.example.dataretrieve.util.DataRetrieve
import com.example.dataretrieve.util.PicRetrieve


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 绑定此 Activity 和对应的页面文件
        setContentView(R.layout.home_page)

        // 获取授权
//        if(!checkPermission()){
//            grantUsagePermission()
//        }
        //grantExtraStrorage()


        // 定位并设置数据获取按键
        val btn_queryAndAggregateUsageStats: Button = findViewById(R.id.btn_queryAndAggregateUsageStats)
        btn_queryAndAggregateUsageStats.setOnClickListener {
            DataRetrieve.getQueryAndAggregateUsageStatsTest(this)
        }

        val btn_queryUsageStats: Button = findViewById(R.id.btn_queryUsageStats)
        btn_queryUsageStats.setOnClickListener {
            DataRetrieve.getQueryUsageStatsTest(this)
        }

        val btn_queryEvents: Button = findViewById(R.id.btn_queryEvents)
        btn_queryEvents.setOnClickListener {
            DataRetrieve.getQueryEvents(this)
        }

        val btn_queryEventsTest: Button = findViewById(R.id.btn_queryEventRow)
        btn_queryEventsTest.setOnClickListener {
            DataRetrieve.getQueryEventsRow(this)
        }

        val btn_create_table: Button = findViewById(R.id.btn_create_table)
        btn_create_table.setOnClickListener {

        }

        val btn_get_icon: Button = findViewById(R.id.btn_get_icons)
        btn_get_icon.setOnClickListener {
            PicRetrieve.getAppPic(this)
        }

    }

    fun checkPermission() : Boolean{
        return AppOpsManager.OPSTR_GET_USAGE_STATS == "android:get_usage_stats"
    }

    // 获取 PACKAGE_USAGE_STATUS
    private fun grantUsagePermission() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    // 获取外部存储权限
    private fun grantExtraStrorage() {
        val uri = Uri.parse("package:com.example.dataretrieve")

        startActivity(
            Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                uri
            )
        )
    }
}
