package com.app.localtune

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var albumArt: ImageView
    private lateinit var songTitleText: TextView
    private lateinit var songArtistText: TextView
    private lateinit var songProgress: SeekBar
    private lateinit var btnPrevious: ImageButton
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var listView: ListView
    private lateinit var audioAdapter: AudioAdapter


    private var songPath: String? = null
    private var songTitle: String? = null
    private var songArtist: String? = null
    private var autoPlay: Boolean = false

    private val playlist = mutableListOf<String>()
    private var currentSongIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.music_player_activity)

        songProgress = findViewById(R.id.songProgress)
        songTitleText = findViewById(R.id.songTitle)
        songArtistText = findViewById(R.id.songArtist)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)

        songPath = intent.getStringExtra("SONG_PATH")
        songTitle = intent.getStringExtra("SONG_TITLE")
        songArtist = intent.getStringExtra("SONG_ARTIST")
        autoPlay = intent.getBooleanExtra("AUTO_PLAY", false)

        playlist.add(songPath ?: "")

        mediaPlayer = MediaPlayer()

        songPath?.let {
            mediaPlayer.setDataSource(it)
            mediaPlayer.prepareAsync()
        }

        mediaPlayer.setOnPreparedListener {
            songProgress.max = mediaPlayer.duration

            if (autoPlay) {
                mediaPlayer.start()
            }

            val updateProgress = object : Runnable {
                override fun run() {
                    songProgress.progress = mediaPlayer.currentPosition
                    if (mediaPlayer.isPlaying) {
                        songProgress.postDelayed(this, 1000)
                    }
                }
            }
            songProgress.post(updateProgress)
        }

        songTitleText.text = songTitle
        songArtistText.text = songArtist

        btnPlayPause.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24)
            } else {
                mediaPlayer.start()
                btnPlayPause.setImageResource(R.drawable.baseline_pause_24)
            }
        }

        btnPrevious.setOnClickListener {
            playPreviousSong()
        }

        btnNext.setOnClickListener {
            playNextSong()
        }

        songProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--
            playSong(playlist[currentSongIndex])
        } else {
            Toast.makeText(this, "No previous song", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playNextSong() {
        if (currentSongIndex < playlist.size - 1) {
            currentSongIndex++
            playSong(playlist[currentSongIndex])
        } else {
            Toast.makeText(this, "No next song", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun playSong(path: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(path)
        mediaPlayer.prepareAsync()

        songTitleText.text = "Song Title"
        songArtistText.text = "Artist Name"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

}
