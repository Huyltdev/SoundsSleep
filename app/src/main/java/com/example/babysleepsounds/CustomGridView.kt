package com.example.babysleepsounds

import android.app.Activity
import android.provider.MediaStore.Images
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomGridView (val activity: Activity, val list: List<OutData>):ArrayAdapter<OutData>(activity,R.layout.layout_item) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val context = activity.layoutInflater
        val rowView = context.inflate(R.layout.layout_item,parent,false)

        val images = rowView.findViewById<ImageView>(R.id.imgSound)
        val txtSound = rowView.findViewById<TextView>(R.id.txtSound)

        images.setImageResource(list[position].images)
        txtSound.text = list[position].nameSound
        return rowView
    }
}
