package com.example.picBook.broadcast

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DownloadBroadCastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L)
                Toast
                    .makeText(context, "Download Success", Toast.LENGTH_SHORT)
                    .show()
            else
                Toast
                    .makeText(context, "Download Failed",Toast.LENGTH_SHORT)
                    .show()
        }
    }
}