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
                if (lastClickedPosition == i) {
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            it.pause()
                            isPlaying = false
                            val fragment1 = Fragment1.newInstanceBtn(isPlaying)
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.fl, fragment1)
                                commitAllowingStateLoss()
                            }
                        } else {
                            it.start()
                            isPlaying = true
                            val fragment1 = Fragment1.newInstanceBtn(isPlaying)
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.fl, fragment1)
                                commitAllowingStateLoss()
                            }
                        }
                    }
                } else {
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            it.stop()
                            it.release()
                            isPlaying = false
                            val fragment1 = Fragment1.newInstanceBtn(isPlaying)
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.fl, fragment1)
                                commitAllowingStateLoss()
                            }
                        }
                    }

                    try {
                        mediaPlayer = MediaPlayer.create(this, list[i].linkSound)
                        mediaPlayer?.start()
                        isPlaying = true

                        val duration = mediaPlayer?.duration.toString()
                        val fragment = Fragment1.newInstance(duration)
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fl, fragment)
                            commitAllowingStateLoss()
                        }
                        val fragment1 = Fragment1.newInstanceBtn(isPlaying)
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.fl, fragment1)
                            commitAllowingStateLoss()
                        }

                        mediaPlayer?.setOnCompletionListener {
                            it.release()
                            isPlaying = false
                            val fragment1 = Fragment1.newInstanceBtn(isPlaying)
                            supportFragmentManager.beginTransaction().apply {
                                replace(R.id.fl, fragment1)
                                commitAllowingStateLoss()
                            }
                        }

                        binding.root.setBackgroundResource(list[i].bg)
                        lastClickedPosition = i
                    } catch (e: Exception) {
                        e.printStackTrace()
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