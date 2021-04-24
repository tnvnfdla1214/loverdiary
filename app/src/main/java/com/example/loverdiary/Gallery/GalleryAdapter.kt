package com.example.loverdiary.Gallery

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loverdiary.R
import java.util.*

class GalleryAdapter(galleryActivity: GalleryActivity, getImagePath: ArrayList<String>) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    private var ArrayList_ImageList : ArrayList<String> = getImagePath
    private var Activity: Activity = galleryActivity
    val INTENT_PATH = "path"
    val INTENT_MEDIA = "media"
    val GALLERY_IMAGE = 0
    val GALLERY_VIDEO = 1

    class GalleryViewHolder(var Cardview: CardView) : RecyclerView.ViewHolder(Cardview)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val Cardview = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false) as CardView
        val Galleryviewholder = GalleryViewHolder(Cardview)
        Cardview.setOnClickListener {
            val Resultintent = Intent()
            Resultintent.putExtra(INTENT_PATH, ArrayList_ImageList[Galleryviewholder.adapterPosition])
            Activity!!.setResult(RESULT_OK, Resultintent)
            Activity!!.finish()
        }
        return Galleryviewholder
    }
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val Cardview = holder.Cardview
        val Imageview = Cardview.findViewById<ImageView>(R.id.Directory_Image)
        Glide.with(Activity!!).load(ArrayList_ImageList[position]).centerCrop().override(500).into(Imageview)
    }

    override fun getItemCount(): Int {
        return ArrayList_ImageList.size
    }
}