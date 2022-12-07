package com.gleb.delineater.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gleb.delineater.R
import com.gleb.delineater.databinding.DialogColorPickBinding
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode


class ColorPickerDialog : DialogFragment(R.layout.dialog_color_pick) {

    private val binding: DialogColorPickBinding by viewBinding()
    var colorListener: ((Int) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDialog()
        binding.initClickListeners()
        setColorPickerView()
    }

    private fun DialogColorPickBinding.initClickListeners() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
        confirmBtn.setOnClickListener {
            colorListener?.invoke(colorPickerView.color)
            dismiss()
        }
    }

    private fun setDialog() {
        dialog?.window?.setWindowAnimations(R.style.ColorPickerDialogAnim)
        val width = resources.getDimensionPixelSize(R.dimen.color_picker_width)
        val height = resources.getDimensionPixelSize(R.dimen.color_picker_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun setColorPickerView() {
        val bubbleFlag = BubbleFlag(binding.root.context)
        bubbleFlag.flagMode = FlagMode.FADE
        binding.colorPickerView.flagView = bubbleFlag
    }

}