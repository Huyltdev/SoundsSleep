package com.example.babysleepsounds

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.babysleepsounds.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Fragment1.OnPlayButtonClickListener {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false
    private var currentDurationFragment: Fragment1? = null
    private var lastClickedPosition: Int? = null
    private var durationFragmentAdded: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hiển thị Splash Screen trong 3 giây
//        Thread.sleep(3000)
        installSplashScreen()

        // Binding: Kết nối giao diện với mã nguồn
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo danh sách âm thanh
        initializeSoundList()
    }

    private fun initializeSoundList() {
        // Dữ liệu mẫu - thay thế bằng dữ liệu âm thanh thực tế
        val list = mutableListOf<OutData>()
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
        // Tạo Adapter và thiết lập GridView
        val customGV = CustomGridView(this, list)
        binding.gvSounds1.adapter = customGV

        // Thiết lập sự kiện click cho GridView
        binding.gvSounds1.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            playSound(list, position)
        }
    }

    private fun playSound(list: MutableList<OutData>, position: Int) {
        // Kiểm tra nếu âm thanh được chọn đang được phát và người dùng nhấn lại, tạm dừng hoặc tiếp tục phát
        if (lastClickedPosition == position && mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
        } else {
            // Nếu có âm thanh đang phát, giải phóng MediaPlayer trước đó
            mediaPlayer?.release()

            // Tạo mới MediaPlayer và bắt đầu phát âm thanh
            mediaPlayer = MediaPlayer.create(this, list[position].linkSound)
            mediaPlayer?.start()
            updateCurrentTimeEverySecond()
            // Cập nhật trạng thái isPlaying
            mediaPlayer?.let {
                isPlaying = true

                // Lấy thời lượng âm thanh và định dạng thành chuỗi hh:mm
                val durationMs = it.duration
                val durationFormatted = formatDuration(durationMs)

                // Thêm hoặc cập nhật Fragment hiển thị thời lượng
                if (!durationFragmentAdded) {
                    currentDurationFragment = Fragment1.newInstance(durationFormatted)
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fl, currentDurationFragment!!)
                        commit()
                    }
                    durationFragmentAdded = true

                } else {
                    currentDurationFragment?.updateDuration(durationFormatted)
                }


                // Thiết lập sự kiện khi âm thanh phát hết
                it.setOnCompletionListener { mediaPlayer ->
                    mediaPlayer.release()
                    isPlaying = false
                    currentDurationFragment?.updateIsPlaying(isPlaying)
                }
            } ?: run {
                // Xử lý trường hợp mediaPlayer là null
                Toast.makeText(this, "Không thể tạo MediaPlayer", Toast.LENGTH_SHORT).show()
            }

            // Lưu vị trí âm thanh được chọn và cập nhật nền của layout
            lastClickedPosition = position
            binding.root.setBackgroundResource(list[position].bg)
        }

        // Cập nhật trạng thái isPlaying cho Fragment1
        currentDurationFragment?.updateIsPlaying(isPlaying)
    }

    private fun updateCurrentTimeEverySecond() {
        runnable = Runnable {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    val currentTime = it.currentPosition
                    val currentTimeFormatted = formatcurrentTime(currentTime)
                    currentDurationFragment?.updateCurrentTime(currentTimeFormatted)
                }
                handler.postDelayed(runnable, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
    }


    private fun formatcurrentTime(durationMs: Int): String {
        // Định dạng thời lượng thành chuỗi hh:mm
        val hours = (durationMs / (1000 * 60 * 60)) % 24
        val minutes = (durationMs / (1000 * 60)) % 60
        val seconds = (durationMs / 1000) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    private fun formatDuration(durationMs: Int): String {
        // Định dạng thời lượng thành chuỗi hh:mm
        val hours = (durationMs / (1000 * 60 * 60)) % 24
        val minutes = (durationMs / (1000 * 60)) % 60
        return String.format("%02d:%02d", hours, minutes)
    }

    // Gọi khi nút Play trên Fragment1 được nhấn
    override fun onPlayButtonClicked(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        // Tạm dừng hoặc tiếp tục âm thanh khi nút Play được nhấn
        if (isPlaying) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
        // Cập nhật trạng thái isPlaying cho Fragment1
        currentDurationFragment?.updateIsPlaying(!isPlaying)
    }

    override fun onDestroy() {
        // Giải phóng MediaPlayer khi Activity bị hủy
        handler.removeCallbacks(runnable)
        mediaPlayer?.release()
        super.onDestroy()
    }
}