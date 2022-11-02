package com.gleb.delineater.ui.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Picture
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.R
import com.gleb.delineater.data.FileHelper
import com.gleb.delineater.data.constants.DOWNLOAD_IMAGE
import com.gleb.delineater.data.constants.PICTURE
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentDrawBinding
import com.gleb.delineater.ui.BaseFragment
import com.gleb.delineater.ui.viewModels.DrawViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DrawFragment : BaseFragment(R.layout.fragment_draw) {

    private lateinit var binding: FragmentDrawBinding
    private val viewModel: DrawViewModel by viewModel()

    private val fileHelper: FileHelper by inject()
    private var recentPicture: PictureEntity? = null

    private lateinit var dialog: androidx.appcompat.app.AlertDialog
    private var isFillBackgroundSelected = false

    companion object {
        var brushWidth = 10f
        var brushColor = Color.BLACK
        var eraserColor = Color.WHITE
        var isEraserSelected = false
    }

    override fun initBinding(view: View) {
        binding = FragmentDrawBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        setBackground()
        initListeners()
        initColorPickerDialog()
        binding.colorPickerBtn.setBackgroundColor(brushColor)
    }

    private fun setBackground() {
        recentPicture?.let {
            binding.paintView.background =
                BitmapFactory.decodeFile(it.picturePath).toDrawable(resources)
        } ?: run {
            binding.paintView.setBackgroundColor(eraserColor)
        }
    }

    private fun getArgs() {
        recentPicture = arguments?.getParcelable(PICTURE)
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            paintSizeSlider.addOnChangeListener { _, value, _ ->
                paintSize.text = "${value.toInt()}dp"
                brushWidth = value
            }
            brushBtn.setOnClickListener {
                isEraserSelected = false
                setPaintBtn(eraseColor = R.color.gray_background, brushColor = R.color.white)
            }
            eraseBtn.setOnClickListener {
                isEraserSelected = true
                setPaintBtn(eraseColor = R.color.white, brushColor = R.color.gray_background)
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
                saveImage()
            }
        }
    }

    private fun saveImage() {
        fileHelper.saveImage(
            binding.paintView.drawToBitmap(
                Bitmap.Config.ARGB_8888
            )
        ) { picturePath ->
            addPicture(picturePath) {
                findNavController().navigate(
                    R.id.draw_to_download,
                    bundleOf(DOWNLOAD_IMAGE to picturePath)
                )
            }
        }
    }

    private fun addPicture(picturePath: String, completeCallback: () -> Unit) {
        recentPicture?.let {
            viewModel.updatePicture(
                PictureEntity(
                    uid = it.uid,
                    picturePath = it.picturePath
                )
            )
            completeCallback()
        } ?: run {
            viewModel.addNewPicture(PictureEntity(picturePath = picturePath))
            completeCallback()
        }
    }

    private fun initColorPickerDialog() {
        dialog = ColorPickerDialog.Builder(requireContext())
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton(getString(R.string.confirm), object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    envelope?.color?.let {
                        if (isFillBackgroundSelected) {
                            eraserColor = it
                            binding.paintView.background = it.toDrawable()
                            isFillBackgroundSelected = false
                        } else {
                            brushColor = it
                            binding.colorPickerBtn.setBackgroundColor(brushColor)
                        }
                    }
                }
            })
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .create()
    }

    private fun setPaintBtn(eraseColor: Int, brushColor: Int) {
        binding.eraseBtn.iconTint = ColorStateList.valueOf(
            resources.getColor(eraseColor, null)
        )
        binding.brushBtn.iconTint = ColorStateList.valueOf(
            (resources.getColor(brushColor, null))
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.paintView.resetPaint()
    }

}