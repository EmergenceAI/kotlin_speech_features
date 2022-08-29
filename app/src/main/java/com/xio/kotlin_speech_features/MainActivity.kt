package com.xio.kotlin_speech_features

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private val permissionRequestCode = 1010

    private fun checkPermission(): Boolean {
        when {
            permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED } -> {
                return true
            }
            permissions.any { shouldShowRequestPermissionRationale(it) } -> {
                finish()
            }
            else -> {
                ActivityCompat.requestPermissions(this, permissions, permissionRequestCode)
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode && grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            finish()
            return
        }
        if (checkPermission()) {

        }
        else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ssc.setOnClickListener {
            Toast.makeText(applicationContext, "SSC called", Toast.LENGTH_LONG).show()
//            TODO: Call SSC on english.wav
        }

        mfcc.setOnClickListener {
            Toast.makeText(applicationContext, "MFCC called", Toast.LENGTH_LONG).show()
//            TODO: Call MFCC on english.wav
        }

        fbank.setOnClickListener {
            Toast.makeText(applicationContext, "FBANK called", Toast.LENGTH_LONG).show()
//            TODO: Call FBANK on english.wav
        }

        logfbank.setOnClickListener {
            Toast.makeText(applicationContext, "LOGFBANK called", Toast.LENGTH_LONG).show()
//            TODO: Call LOGFBANK on english.wav
        }
    }
}