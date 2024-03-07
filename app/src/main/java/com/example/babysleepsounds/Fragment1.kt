package com.example.babysleepsounds

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment1 : Fragment(R.layout.fragment_1) {
    private lateinit var myTextView: TextView

    // Hàm tạo với tham số
    companion object {
        fun newInstance(duration: String): Fragment1 {
            val fragment = Fragment1()
            val bundle = Bundle().apply {
                putString("mText", duration)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_1, container, false)
        myTextView = view.findViewById(R.id.txtTimer)

        // Lấy thời lượng tính bằng mili giây từ đối số
        val durationInMillis = arguments?.getString("mText")?.toLongOrNull()

        // Kiểm tra xem durationInMillis có khác null không
        if (durationInMillis != null) {

            // Chuyển đổi mili giây thành tổng số giây
            val totalSeconds = durationInMillis / 1000

            // Tính giờ, phút và giây
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60

            // Định dạng thời gian thành HH:mm
            val formattedTime = String.format("%02d:%02d", hours, minutes)

            // Đặt thời gian đã định dạng vào TextView
            myTextView.text = formattedTime
        }

        return view
    }

}
