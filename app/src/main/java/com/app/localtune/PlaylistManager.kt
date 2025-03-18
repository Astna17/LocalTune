package com.app.localtune

import java.io.File

class PlaylistManager {
    private val playlist = mutableListOf<File>()

    // Ajouter un fichier à la playlist
    fun addToPlaylist(file: File) {
        if (!playlist.contains(file)) {
            playlist.add(file)
        }
    }

    // Obtenir le fichier actuel
    fun getCurrentSong(): File? {
        return if (playlist.isNotEmpty()) playlist[0] else null
    }

    // Obtenir la chanson suivante
    fun getNextSong(): File? {
        if (playlist.isNotEmpty()) {
            val nextIndex = (playlist.indexOf(getCurrentSong()) + 1) % playlist.size
            return playlist[nextIndex]
        }
        return null
    }

    // Obtenir la chanson précédente
    fun getPreviousSong(): File? {
        if (playlist.isNotEmpty()) {
            val previousIndex = if (playlist.indexOf(getCurrentSong()) == 0) playlist.size - 1 else playlist.indexOf(getCurrentSong()) - 1
            return playlist[previousIndex]
        }
        return null
    }

    // Obtenir la playlist
    fun getPlaylist(): List<File> {
        return playlist
    }

    // Obtenir la taille de la playlist
    fun getPlaylistSize(): Int {
        return playlist.size
    }
}
