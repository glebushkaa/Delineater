package com.gleb.delineater.ui.dialogs

import android.content.Context
import com.gleb.delineater.R
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class ColorDialogHelper(private val context: Context){

    fun initColorPickerDialog(
        successListener: (Int) -> Unit
    ) = ColorPickerDialog.Builder(context)
        .setTitle("ColorPicker Dialog")
        .setPreferenceName("MyColorPickerDialog")
        .attachAlphaSlideBar(false)
        .setPositiveButton(context.getString(R.string.confirm), object : ColorEnvelopeListener {
            override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                envelope?.color?.let {
                    successListener(it)
                }
            }
        })
        .setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
        .create()

}