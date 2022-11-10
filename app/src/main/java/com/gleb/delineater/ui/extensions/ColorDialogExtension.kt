package com.gleb.delineater.ui.extensions

import android.content.Context
import com.gleb.delineater.R
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

private const val preferencesName = "MyColorPickerDialog"
private const val isAttachedSlideBar = false

fun Context.initColorPickerDialog(successListener: (Int) -> Unit) =
    ColorPickerDialog.Builder(this)
        .setTitle(getString(R.string.color_picker_dialog))
        .setPreferenceName(preferencesName)
        .attachAlphaSlideBar(isAttachedSlideBar)
        .setPositiveButton(getString(R.string.confirm),
            object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    envelope?.color?.let {
                        successListener(it)
                    }
                }
            })
        .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        .create()