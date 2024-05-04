package com.yusufaltas.caseapp.view

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.yusufaltas.caseapp.R
import com.yusufaltas.caseapp.model.Constants.PERMISSIONS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value
        }
        if (granted) lifecycleScope.launch {}
        else lifecycleScope.launch {
            Toast.makeText(this@MainActivity, getString(R.string.all_permission_error), Toast.LENGTH_LONG).show()
            //If we want, we can require permissions here and close the app if permission is not granted.
            //delay(5000)
            //requireActivity().finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.let {
            if (hasPermissions(this@MainActivity, PERMISSIONS)) lifecycleScope.launch {
                //If we want it to perform an action when permissions are received, we can write it here.
            }
            else permissionRequest.launch(PERMISSIONS)
        }
    }


    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

}