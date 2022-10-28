package com.gleb.delineater.ui.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.ui.customViews.PaintView.Companion.paintList
import com.gleb.delineater.R
import com.gleb.delineater.databinding.FragmentDrawBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class DrawFragment : Fragment() {

    private var binding: FragmentDrawBinding? = null

    private lateinit var dialog: androidx.appcompat.app.AlertDialog
    private var isFillBackgroundSelected = false

    companion object {
        var brushWidth = 10f
        var brushColor = Color.BLACK
        var eraserColor = Color.WHITE
        var isEraserSelected = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDrawBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initColorPickerDialog()
        binding?.colorPickerBtn?.setBackgroundColor(brushColor)
        binding?.paintView?.setBackgroundColor(eraserColor)
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        binding?.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            paintSizeSlider.addOnChangeListener { _, value, _ ->
                paintSize.text = "${value.toInt()}dp"
                brushWidth = value
            }
            brushBtn.setOnClickListener {
                isEraserSelected = false
                setColorPaintButtons()
            }
            eraseBtn.setOnClickListener {
                isEraserSelected = true
                setColorPaintButtons()
            }
            colorPickerBtn.setOnClickListener {
                dialog.show()
            }
            refreshBtn.setOnClickListener {
                paintView.resetSurface()
            }
            fillBackBtn.setOnClickListener {
                isFillBackgroundSelected = true
                dialog.show()
            }
            downloadBtn.setOnClickListener {
                val bitmap = paintView.drawToBitmap()
                findNavController().navigate(
                    R.id.draw_to_download, bundleOf(
                        "downloadImage" to bitmap
                    )
                )
            }
        }
    }

    private fun initColorPickerDialog() {
        dialog = ColorPickerDialog.Builder(requireContext())
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Confirm", object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    envelope?.color?.let {
                        if (isFillBackgroundSelected) {
                            eraserColor = it
                            binding?.paintView?.background = it.toDrawable()
                            isFillBackgroundSelected = false
                        } else {
                            brushColor = it
                            binding?.colorPickerBtn?.setBackgroundColor(brushColor)
                        }
                    }
                }
            })
            .setNegativeButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }
            .create()
    }

    private fun setColorPaintButtons() {
        binding?.apply {
            if (isEraserSelected) {
                eraseBtn.iconTint = ColorStateList.valueOf(
                    resources.getColor(R.color.white, null)
                )
                brushBtn.iconTint = ColorStateList.valueOf(
                    (resources.getColor(R.color.gray_background, null))
                )
            } else {
                eraseBtn.iconTint = ColorStateList.valueOf(
                    (resources.getColor(R.color.gray_background, null))
                )
                brushBtn.iconTint = ColorStateList.valueOf(
                    (resources.getColor(R.color.white, null))
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        brushColor = Color.BLACK
        brushWidth = 10f
        paintList.clear()
    }

}