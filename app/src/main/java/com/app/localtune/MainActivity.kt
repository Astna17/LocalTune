package com.app.localtune

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var audioFiles: List<File>
    private lateinit var audioAdapter: AudioAdapter
    private lateinit var listView: ListView

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.audioListView)

        if (checkPermission()) {
            loadAudioFiles()
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadAudioFiles()
            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadAudioFiles() {
        audioFiles = getAudioFiles()
        audioAdapter = AudioAdapter(this, audioFiles)
        listView.adapter = audioAdapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedFile = audioFiles[position]
            val intent = Intent(this, MusicPlayerActivity::class.java).apply {
                putExtra("song_path", selectedFile.absolutePath)
            }
            startActivity(intent)
        }
    }

    private fun getAudioFiles(): List<File> {
        val audioList = mutableListOf<File>()
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (it.moveToNext()) {
                val filePath = it.getString(dataIndex)
                audioList.add(File(filePath))
            }
        }

        return audioList
    }
}
