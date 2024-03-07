package com.example.babysleepsounds

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Video.Media
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.babysleepsounds.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var lastClickedPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Screen Splash
        Thread.sleep(3000)
        installSplashScreen()
        //Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Code
        //


        //Declare list
        var list = mutableListOf<OutData>()
        list.add(OutData(R.drawable.rainy, "Rainy", R.raw.rain, R.drawable.bgrain, false))
        list.add(OutData(R.drawable.leaf, "Leaf", R.raw.leaf, R.drawable.bgleaf, false))
        list.add(OutData(R.drawable.flash, "Flash", R.raw.flash, R.drawable.bgrain, false))
        list.add(OutData(R.drawable.wave, "Wawe", R.raw.wave, R.drawable.bgwave, false))
        list.add(OutData(R.drawable.wind, "Wind", R.raw.wind, R.drawable.bgwave, false))
        list.add(
            OutData(
                R.drawable.heavyrain,
                "Heavy Rain",
                R.raw.heavyrain,
                R.drawable.bgrain,
                false
            )
        )
        list.add(
            OutData(
                R.drawable.waterdrop,
                "Water Drop",
                R.raw.waterdrop,
                R.drawable.bgwaterfall,
                false
            )
        )
        list.add(
            OutData(
                R.drawable.waterfall,
                "Water Fall",
                R.raw.waterfall,
                R.drawable.bgwaterfall,
                false
            )
        )
        //

        val customGV = CustomGridView(this, list)
        binding.gvSounds1.adapter = customGV

        //Fragment
//        val sub = Fragment1()
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fl, sub)
//            commit()
//        }

        //Click item
        binding.gvSounds1.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                // Stop the currently playing sound
                if (lastClickedPosition == i) {
                    // Toggle between playing and stopping the sound
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            it.pause()
                        } else {
                            it.start()
                        }
                    }
                } else {
                    // Stop the currently playing sound
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            it.stop()
                            it.release()
                        }
                    }

                    // Start playing the new sound
                    try {
                        mediaPlayer = MediaPlayer.create(this, list[i].linkSound)
                        mediaPlayer?.start()

                        //
                        val duration = mediaPlayer?.duration.toString()
                        val fragment = Fragment1.newInstance(duration)
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fl, fragment)
                            commitAllowingStateLoss()
                        }
                        // Release the MediaPlayer when it's completed
                        mediaPlayer?.setOnCompletionListener {
                            it.release()
                        }

                        // BGScreen change click item
                        binding.root.setBackgroundResource(list[i].bg)

                        // Update the last clicked position
                        lastClickedPosition = i
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Handle the exception (show a toast or log the error)
                        Toast.makeText(this, "Error playing the sound", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onDestroy() {
        // Release any resources here
        mediaPlayer?.release()
        super.onDestroy()
    }


}