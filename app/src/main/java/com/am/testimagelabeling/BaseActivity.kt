package com.am.testimagelabeling

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File


open class BaseActivity : AppCompatActivity() {
    lateinit var imageFile: File

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle(intent.getStringExtra(ACTION_BAR_TITLE))
        }
    }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            //If “gallery_action” is selected, then...//

            R.id.action_gallery ->

                //...check we have the WRITE_STORAGE permission//

                checkStoragePermission(RC_STORAGE_PERMS1)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_STORAGE_PERMS1 ->

                //If the permission request is granted, then...//

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //...call selectPicture//

                    selectPicture()

                    //If the permission request is denied, then...//

                } else {

                    //...display the “permission_request” string//

                    MyHelper.needPermission(this, requestCode, R.string.permission_request)
                }
        }
    }

    //Check whether the user has granted the WRITE_STORAGE permission//

    fun checkStoragePermission(requestCode: Int) {
        when (requestCode) {
            RC_STORAGE_PERMS1 -> {
                val hasWriteExternalStoragePermission =
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                //If we have access to external storage...//

                if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {

                    //...call selectPicture, which launches an Activity where the user can select an image//

                    selectPicture()

                    //If permission hasn’t been granted, then...//

                } else {

                    //...request the permission//

                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        requestCode
                    )
                }
            }
        }
    }

    private fun selectPicture() {
        imageFile = MyHelper.createTempFile(imageFile)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_SELECT_PICTURE)
    }

    companion object {
        val RC_STORAGE_PERMS1 = 101
        val RC_SELECT_PICTURE = 103
        val ACTION_BAR_TITLE = "action_bar_title"
    }

}