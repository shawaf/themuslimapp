package me.shawaf.themuslimapp.utils

import android.content.Context
import android.media.MediaPlayer
import me.shawaf.themuslimapp.R

object SoundUtils {
    private var mediaPlayer: MediaPlayer? = null

    fun playClickSound(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.click_sound1)
        mediaPlayer?.start()
    }
}