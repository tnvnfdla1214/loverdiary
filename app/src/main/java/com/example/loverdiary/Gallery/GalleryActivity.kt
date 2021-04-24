package com.example.loverdiary.Gallery

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loverdiary.R
import java.util.*

class GalleryActivity : AppCompatActivity() {
    val INTENT_PATH = "path"
    val INTENT_MEDIA = "media"
    val GALLERY_IMAGE = 0
    val GALLERY_VIDEO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        //setToolbarTitle("");
        checkpermission()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recyclerInit()
                } else {
                    finish()
                    Toast.makeText(this@GalleryActivity, resources.getString(R.string.please_grant_permission), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun checkpermission() {
        if (ContextCompat.checkSelfPermission(this@GalleryActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@GalleryActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@GalleryActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                Toast.makeText(this@GalleryActivity, resources.getString(R.string.please_grant_permission), Toast.LENGTH_SHORT).show()
            } // part18 : strings에 메시지 관리하기
        } else {
            recyclerInit()
        }
    }
    private fun recyclerInit() {
        val numberOfColumns = 3
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, numberOfColumns)
        val mAdapter: RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> = GalleryAdapter(this, GetImagePath(this))
        recyclerView.adapter = mAdapter
    }
    fun GetImagePath(activity: Activity): ArrayList<String> {
        val ListOfImage = ArrayList<String>()
        val projection: Array<String>
        var PathOfImage: String?
        val Column_Index_Data: Int
        val Image_Reset_Button: Button
        val CursorEvent: Cursor?
        val URI: Uri

        val ImageIntent = intent
        val Media = ImageIntent.getIntExtra(INTENT_MEDIA, GALLERY_IMAGE)

        Image_Reset_Button = findViewById(R.id.Image_Reset_Button)
        Image_Reset_Button.setOnClickListener(onClickListener)
        if (Media == GALLERY_VIDEO) {
            URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        } else {
            URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        }
        CursorEvent = activity.contentResolver.query(URI, projection, null, null, null)
        Column_Index_Data = CursorEvent!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (CursorEvent.moveToNext()) {
            PathOfImage = CursorEvent.getString(Column_Index_Data)
            ListOfImage.add(PathOfImage)
        }
        return ListOfImage
    }

    var onClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.Image_Reset_Button -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }
}