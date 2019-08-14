package com.am.testimagelabeling

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BActivity() {
    private lateinit var mBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        btn_device.setOnClickListener {
            textView.text = null
            if (mBitmap != null) {

                //Configure the detector//
                val options = FirebaseVisionLabelDetectorOptions.Builder()
                    .setConfidenceThreshold(0.7f)
                    .build()

                //Create a FirebaseVisionImage object//

                val image = FirebaseVisionImage.fromBitmap(mBitmap)

                //Create an instance of FirebaseVisionLabelDetector//

                val detector = FirebaseVision.getInstance().getVisionLabelDetector(options)

                //Register an OnSuccessListener//

                detector.detectInImage(image).addOnSuccessListener { labels ->
                    //Implement the onSuccess callback//
                    for (label in labels) {
                        Log.d("ttt", label.label)
                    }
                    Log.d("ttt", "--------------")

                }.addOnFailureListener { e -> textView.text = e.message }
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_STORAGE_PERMS1 -> checkStoragePermission(requestCode)
                RC_SELECT_PICTURE -> {
                    val dataUri = data!!.data
                    val path = MyHelper.getPath(this, dataUri)
                    if (path == null) {
                        mBitmap = MyHelper.resizeImage(imageFile, this, dataUri, imageView)
                    } else {
                        mBitmap = MyHelper.resizeImage(imageFile, path, imageView)
                    }
                    if (mBitmap != null) {
                        textView.text = null
                        imageView.setImageBitmap(mBitmap)
                    }
                }
            }
        }
    }

}
