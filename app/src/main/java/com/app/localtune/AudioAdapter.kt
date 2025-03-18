package com.app.localtune

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mpatric.mp3agic.Mp3File
import java.io.File

class AudioAdapter(
    private val context: Context,
    private val audioFiles: List<File>
) : BaseAdapter() {

    override fun getCount(): Int = audioFiles.size

    override fun getItem(position: Int): Any = audioFiles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    private fun getArtistFromFile(audioFile: File): String? {
        return try {
            val mp3File = Mp3File(audioFile.absolutePath)
            if (mp3File.hasId3v2Tag()) {
                mp3File.id3v2Tag.artist
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val audioFile = getItem(position) as File
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = audioFile.name

        val artist = getArtistFromFile(audioFile) ?: "Artiste inconnu"

        view.setOnClickListener {
            val intent = Intent(context, MusicPlayerActivity::class.java).apply {
                putExtra("SONG_PATH", audioFile.path)
                putExtra("SONG_TITLE", audioFile.name)
                putExtra("SONG_ARTIST", artist)
                putExtra("AUTO_PLAY", false)
            }
            context.startActivity(intent)
        }

        return view
    }
}