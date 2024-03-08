package com.example.babysleepsounds

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

    private lateinit var myTextView: TextView
    private lateinit var playButton: ImageButton
    private var isPlaying: Boolean = false
    private lateinit var currentTimeView: TextView
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var audioManager: AudioManager

    // Đối tượng companion object, giúp tạo ra Fragment1 với đối số duration
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

    // Ghi đè hàm onCreateView để khởi tạo giao diện người dùng
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Tạo ra view bằng cách inflate layout từ file XML (R.layout.fragment_1)
        val view: View = inflater.inflate(R.layout.fragment_1, container, false)

        // Liên kết biến với các phần tử trong layout
        myTextView = view.findViewById(R.id.txtTimer)
        playButton = view.findViewById(R.id.btnPlay)
        currentTimeView = view.findViewById(R.id.txtCurrentTime)
        volumeSeekBar = view.findViewById(R.id.volumeSeekBar)

        // Lấy đối số duration từ Bundle và cập nhật TextView
        arguments?.getString(ARG_DURATION)?.let {
            updateDuration(it)
        }

        // Khởi tạo trạng thái isPlaying là true và cập nhật hình ảnh của nút playButton
        isPlaying = true // Khởi tạo với trạng thái đúng
        updatePlayButtonImage()

        // Thiết lập sự kiện nghe cho nút playButton để xử lý khi nút được nhấn
        playButton.setOnClickListener {
            onPlayButtonClicked(it)
        }

        // Thiết lập AudioManager và Volume Control
        activity?.let {
            audioManager = it.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            setupVolumeControl()
        }

        // Trả về view đã được tạo để hiển thị trên giao diện người dùng
        return view
    }

    private fun setupVolumeControl() {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.max = maxVolume
        volumeSeekBar.progress = currentVolume

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Có thể thêm code xử lý khi bắt đầu kéo SeekBar nếu cần
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Có thể thêm code xử lý khi dừng kéo SeekBar nếu cần
            }
        })
    }

    fun updateCurrentTime(time: String) {
        currentTimeView.text = time
    }


    // Hàm này cập nhật TextView với giá trị duration mới
    fun updateDuration(duration: String) {
        myTextView.text = duration
    }

    // Hàm này cập nhật trạng thái isPlaying và hình ảnh của nút playButton
    fun updateIsPlaying(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        updatePlayButtonImage()
    }

    // Hàm xử lý khi nút playButton được nhấn
    fun onPlayButtonClicked(view: View) {
        // Đảo ngược trạng thái isPlaying và cập nhật hình ảnh của nút playButton
        isPlaying = !isPlaying
        updatePlayButtonImage()

        // Gọi sự kiện của interface để thông báo trạng thái mới
        (activity as? OnPlayButtonClickListener)?.onPlayButtonClicked(isPlaying)

        // Bắt các ngoại lệ có thể xảy ra trong quá trình gọi sự kiện
        try {
            (requireActivity() as OnPlayButtonClickListener).onPlayButtonClicked(isPlaying)
        } catch (e: Exception) {
            // Ghi log hoặc xử lý ngoại lệ phù hợp với ứng dụng của bạn
            Log.e("Fragment11", "Lỗi trong onPlayButtonClicked", e)
        }
    }

    // Hàm này cập nhật hình ảnh của nút playButton dựa trên trạng thái isPlaying
    private fun updatePlayButtonImage() {
        // Kiểm tra xem playButton đã được khởi tạo chưa
        if (::playButton.isInitialized) {
            // Nếu đã khởi tạo, cập nhật hình ảnh dựa trên trạng thái isPlaying
            if (isPlaying) {
                playButton.setImageResource(R.drawable.iconstop)
            } else {
                playButton.setImageResource(R.drawable.iconplay)
            }
        } else {
            // Nếu chưa khởi tạo, ghi log lỗi
            Log.e("Fragment22", "playButton is null in updatePlayButtonImage()")
        }
    }

    // Interface để gửi sự kiện khi nút playButton được nhấn
    interface OnPlayButtonClickListener {
        fun onPlayButtonClicked(isPlaying: Boolean)
    }
}
