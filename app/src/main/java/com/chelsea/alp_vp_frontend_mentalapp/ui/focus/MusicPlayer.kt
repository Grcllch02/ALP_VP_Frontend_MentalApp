package com.chelsea.alp_vp_frontend_mentalapp.ui.focus

import android.content.Context
import android.media.MediaPlayer

class MusicPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    fun play(resId: Int) {
        stop()
        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = true
            start()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}
