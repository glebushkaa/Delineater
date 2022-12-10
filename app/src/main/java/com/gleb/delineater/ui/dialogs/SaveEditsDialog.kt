package com.gleb.delineater.ui.dialogs

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gleb.delineater.R
import com.gleb.delineater.databinding.DialogSaveEditsBinding
import com.gleb.delineater.ui.extensions.showToast
import com.gleb.delineater.ui.listeners.SaveEditsListener

class SaveEditsDialog : DialogFragment(R.layout.dialog_save_edits) {

    var editsListener: SaveEditsListener? = null

    private val binding: DialogSaveEditsBinding by viewBinding()

    private val storagePermissionsArray = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var saveEditsStoragePermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { !it.value }) {
            dismissNow()
            editsListener?.deniedPermission()
            return@registerForActivityResult
        }
        dismissNow()
        editsListener?.saveEdits()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDialog()
        binding.initClickListeners()
    }

    private fun DialogSaveEditsBinding.initClickListeners() {
        discardEditsBtn.setOnClickListener {
            dismissNow()
            editsListener?.discardEdits()
        }
        saveEditsBtn.setOnClickListener {
            checkPermission()
        }
    }

    private fun setDialog() {
        val width = resources.getDimensionPixelSize(R.dimen.save_edits_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.save_edits_dialog_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dismissNow()
            editsListener?.saveEdits()
        } else {
            checkStoragePermission()
        }
    }

    private fun checkStoragePermission() {
        val writePermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

        if (writePermission) {
            dismissNow()
            editsListener?.saveEdits()
        } else {
            saveEditsStoragePermission.launch(storagePermissionsArray)
        }
    }
}