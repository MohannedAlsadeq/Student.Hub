package com.studenthub.app.ui

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.toast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

/** Shows a Save/Cancel dialog. onSave returns true to close it, false to keep it open. */
fun Fragment.formDialog(title: String, content: View, onSave: () -> Boolean) {
    val dialog = MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setView(content)
        .setPositiveButton("Save", null)
        .setNegativeButton("Cancel", null)
        .create()
    dialog.setOnShowListener {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (onSave()) dialog.dismiss()
        }
    }
    dialog.show()
}
