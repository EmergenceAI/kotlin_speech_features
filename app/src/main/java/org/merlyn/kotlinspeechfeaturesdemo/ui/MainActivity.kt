package org.merlyn.kotlinspeechfeaturesdemo.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.merlyn.kotlinspeechfeaturesdemo.R

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ssc.setOnClickListener {
            Toast.makeText(this, "SSC called", Toast.LENGTH_LONG).show()
            viewModel.performSsc()
        }

        mfcc.setOnClickListener {
            Toast.makeText(this, "MFCC called", Toast.LENGTH_LONG).show()
            viewModel.performMfcc()
        }

        fbank.setOnClickListener {
            Toast.makeText(this, "FBANK called", Toast.LENGTH_LONG).show()
            viewModel.performFbank()
        }

        logfbank.setOnClickListener {
            Toast.makeText(this, "LOGFBANK called", Toast.LENGTH_LONG).show()
            viewModel.performLogfbank()
        }
    }
}