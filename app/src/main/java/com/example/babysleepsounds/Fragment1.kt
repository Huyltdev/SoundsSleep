package com.example.babysleepsounds

import android.app.TimePickerDialog
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class Fragment1 : Fragment() {

    private lateinit var txtTime: TextView
    private lateinit var playButton: ImageButton
    private var isPlaying: Boolean = false
    private lateinit var currentTimeView: TextView
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var audioManager: AudioManager
    private lateinit var btnMute: ImageButton
    private lateinit var btnSetTime: ImageButton

    companion object {
        private const val ARG_DURATION = "duration"

        fun newInstance(duration: String): Fragment1 {
            return Fragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_DURATION, duration)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_1, container, false)

        txtTime = view.findViewById(R.id.txtTime)
        playButton = view.findViewById(R.id.btnPlay)
        currentTimeView = view.findViewById(R.id.txtCurrentTime)
        volumeSeekBar = view.findViewById(R.id.volumeSeekBar)
        btnMute = view.findViewById(R.id.btnMute)
        btnSetTime = view.findViewById(R.id.btnSetTime)

        arguments?.getString(ARG_DURATION)?.let {
            updateDuration(it)
        }

        isPlaying = true
        updatePlayButtonImage()

        playButton.setOnClickListener {
            onPlayButtonClicked(it)
        }

        activity?.let {
            audioManager = it.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            setupVolumeControl()
        }

        btnMute.setOnClickListener {
            onPlayButtonViewClicked(it)
        }

        btnSetTime.setOnClickListener {
            onbtnSetTimeViewClicked(it)
        }

        return view
    }

    private fun onbtnSetTimeViewClicked(view: View) {
        TimePickerDialog(
            context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//                updateDuration(String.format("%02d:%02d", hourOfDay, minute))
            },0,0,true
        ).show()
    }

    private fun onPlayButtonViewClicked(view: View) {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val newVolume = if (currentVolume == 0) maxVolume else 0
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
        volumeSeekBar?.progress = newVolume
    }

    private fun setupVolumeControl() {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar?.max = maxVolume
        volumeSeekBar?.progress = currentVolume

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun updateCurrentTime(time: String) {
        currentTimeView.text = time
    }

    fun updateDuration(duration: String) {
        txtTime.text = duration
    }

    fun updateIsPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        updatePlayButtonImage()
    }

    fun onPlayButtonClicked(view: View) {
        isPlaying = !isPlaying
        updatePlayButtonImage()
        (activity as? OnPlayButtonClickListener)?.onPlayButtonClicked(isPlaying)
        try {
            (requireActivity() as OnPlayButtonClickListener).onPlayButtonClicked(isPlaying)
        } catch (e: Exception) {
            Log.e("Fragment1", "Error in onPlayButtonClicked", e)
        }
    }

    private fun updatePlayButtonImage() {
        if (::playButton.isInitialized) {
            if (isPlaying) {
                playButton.setImageResource(R.drawable.iconstop)
            } else {
                playButton.setImageResource(R.drawable.iconplay)
            }
        } else {
            Log.e("Fragment1", "playButton is null in updatePlayButtonImage()")
        }
    }

    interface OnPlayButtonClickListener {
        fun onPlayButtonClicked(isPlaying: Boolean)
    }
}
