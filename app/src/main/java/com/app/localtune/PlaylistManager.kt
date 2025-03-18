package com.app.localtune

import java.io.File

class PlaylistManager {

    private val playlist = mutableListOf<File>()

    fun addToPlaylist(file: File) {
        playlist.add(file)
    }

    fun removeFromPlaylist(file: File) {
        playlist.remove(file)
    }

    fun getPlaylist(): List<File> {
        return playlist
    }
}
