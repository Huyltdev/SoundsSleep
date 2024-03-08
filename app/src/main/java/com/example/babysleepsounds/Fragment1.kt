package com.example.babysleepsounds

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

private const val ARG_DURATION = "duration"
private const val ARG_IS_PLAYING = "isPlaying"

interface OnPlayButtonClickListener {
    fun onPlayButtonClicked(isPlaying: Boolean)
}

class Fragment1 : Fragment(R.layout.fragment_1) {

    private lateinit var myTextView: TextView
    private lateinit var playButton: ImageButton
    private var isPlaying: Boolean = false

    companion object {
        private const val ARG_DURATION = "duration"
        private const val ARG_IS_PLAYING = "isPlaying"
        private var argumentsBtn: Bundle? = null

        fun newInstance(duration: String): Fragment1 {
            return Fragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_DURATION, duration)
                }
            }
        }

        fun newInstanceBtn( isPlaying: Boolean): Fragment1 {
            return Fragment1().apply {
                argumentsBtn = Bundle().apply {
                    putBoolean(ARG_IS_PLAYING, isPlaying)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_1, container, false)
        myTextView = view.findViewById(R.id.txtTimer)
        playButton = view.findViewById(R.id.btnPlay)

        arguments?.getString(ARG_DURATION)?.toLongOrNull()?.let { durationInMillis ->
            val totalSeconds = durationInMillis / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val formattedTime = String.format("%02d:%02d", hours, minutes)
            myTextView.text = formattedTime
        }

        isPlaying = argumentsBtn?.getBoolean(ARG_IS_PLAYING) ?: false
        updatePlayButtonImage()

        playButton.setOnClickListener {
            // Toggle isPlaying and update the button image
            isPlaying = !isPlaying
            updatePlayButtonImage()

            // Notify the MainActivity about the play button click event
            (activity as? OnPlayButtonClickListener)?.onPlayButtonClicked(isPlaying)
        }

        return view
    }

    private fun updatePlayButtonImage() {
        if (isPlaying) {
            playButton.setImageResource(R.drawable.iconstop)
        } else {
            playButton.setImageResource(R.drawable.iconplay)
        }
    }
}
