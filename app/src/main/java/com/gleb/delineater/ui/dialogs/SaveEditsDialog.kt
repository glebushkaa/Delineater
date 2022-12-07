package com.gleb.delineater.ui.dialogs

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var saveEditsStoragePermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { !it.value }) {
            editsListener?.deniedPermission()
            dismiss()
            return@registerForActivityResult
        }
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
            saveEditsStoragePermission.launch(storagePermissionsArray)
        }
    }

    private fun setDialog() {
        val width = resources.getDimensionPixelSize(R.dimen.save_edits_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.save_edits_dialog_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


}