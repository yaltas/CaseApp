package com.yusufaltas.caseapp.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.yusufaltas.caseapp.R

object DialogHelper {

    fun showDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.ok), null)
            .show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
