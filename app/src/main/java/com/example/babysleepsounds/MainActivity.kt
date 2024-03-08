package com.example.babysleepsounds

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Video.Media
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.babysleepsounds.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var lastClickedPosition: Int? = null
    private var isPlaying: Boolean = false
    private var durationFragmentAdded: Boolean = false
    private var currentDurationFragment: Fragment1? = null

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

        //Click item
        binding.gvSounds1.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            playSound(list, i)
        }
    }

    private fun playSound(list: MutableList<OutData>, position: Int) {
        if (lastClickedPosition == position && mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
        } else {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, list[position].linkSound).also { mp ->
                mp.start()
                isPlaying = true

                // Đảm bảo rằng mediaPlayer đã sẵn sàng trước khi truy cập duration
                val duration = mp.duration // Lấy duration của media player (là số milliseconds)
                val durationSeconds = duration / 1000 // Chuyển đổi sang giây
                val minutes = durationSeconds / 60
                val seconds = durationSeconds % 60
                val formattedDuration = String.format("%02d:%02d", minutes, seconds)

                if (!durationFragmentAdded) {
                    currentDurationFragment = Fragment1.newInstance(formattedDuration)
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fl, currentDurationFragment!!)
                        commit()
                    }
                    durationFragmentAdded = true
                } else {
                    currentDurationFragment?.updateDuration(formattedDuration)
                }

                mp.setOnCompletionListener {
                    it.release()
                    isPlaying = false
                    // You might want to update the UI or fragment state here
                }
            }
            lastClickedPosition = position
            binding.root.setBackgroundResource(list[position].bg)
        }
        updateFragment(isPlaying)
    }

    private fun updateFragment(isPlaying: Boolean) {
        // Your code to update the fragment, for example, to change play/pause button state
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }
}