package com.gleb.delineater.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.R
import com.gleb.delineater.data.ColorPickerType
import com.gleb.delineater.data.FileHelper
import com.gleb.delineater.data.PaintType
import com.gleb.delineater.data.constants.DOWNLOAD_IMAGE
import com.gleb.delineater.data.constants.PICTURE
import com.gleb.delineater.databinding.FragmentDrawBinding
import com.gleb.delineater.ui.BaseFragment
import com.gleb.delineater.ui.dialogs.ColorDialogHelper
import com.gleb.delineater.ui.viewModels.DrawViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DrawFragment : BaseFragment(R.layout.fragment_draw) {

    private lateinit var binding: FragmentDrawBinding
    private val viewModel: DrawViewModel by viewModel()

    private val fileHelper: FileHelper by inject()
    private var colorDialog: ColorDialogHelper? = null
    private var dialog: Dialog? = null

    private var colorPickerType: ColorPickerType = ColorPickerType.BrushColorPicker

    override fun initBinding(view: View) {
        binding = FragmentDrawBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        initListeners()
        initColorDialog()
        setColors()
        setBackground()
    }

    private fun setBackground() {
        viewModel.currentPicture?.let {
            binding.paintView.background =
                BitmapFactory.decodeFile(it.picturePath).toDrawable(resources)
        }
    }

    private fun setColors() {
        binding.apply {
            colorPickerBtn.setBackgroundColor(paintView.brushColor)
            paintView.setBackgroundColor(paintView.eraserColor)
        }
    }

    private fun getArgs() {
        viewModel.currentPicture = arguments?.getParcelable(PICTURE)
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            paintSizeSlider.addOnChangeListener { _, size, _ ->
                paintSize.text = "${size.toInt()}dp"
                paintView.brushWidth = size
            }
            brushBtn.setOnClickListener {
                paintView.paintType = PaintType.Brush
                setPaintBtn(eraseColor = R.color.gray_background, brushColor = R.color.white)
            }
            eraseBtn.setOnClickListener {
                paintView.paintType = PaintType.Eraser
                setPaintBtn(eraseColor = R.color.white, brushColor = R.color.gray_background)
            }
            colorPickerBtn.setOnClickListener {
                colorPickerType = ColorPickerType.BrushColorPicker
                dialog?.show()
            }
            refreshBtn.setOnClickListener {
                paintView.resetSurface()
            }
            fillBackBtn.setOnClickListener {
                colorPickerType = ColorPickerType.BackgroundColorPicker
                dialog?.show()
            }
            downloadBtn.setOnClickListener {
                saveImage()
            }
            stepBackBtn.setOnClickListener {
                paintView.removeLastStep()
            }
            restoreStepBtn.setOnClickListener {
                paintView.restoreDeletedStep()
            }
        }
    }

    private fun saveImage() {
        fileHelper.saveImage(
            binding.paintView.drawToBitmap(Bitmap.Config.ARGB_8888)
        ) {
            viewModel.addCurrentPicture(it)
            findNavController().navigate(
                R.id.draw_to_download,
                bundleOf(DOWNLOAD_IMAGE to it)
            )
        }
    }

    private fun setPaintBtn(eraseColor: Int, brushColor: Int) {
        binding.eraseBtn.iconTint = ColorStateList.valueOf(
            resources.getColor(eraseColor, null)
        )
        binding.brushBtn.iconTint = ColorStateList.valueOf(
            (resources.getColor(brushColor, null))
        )
    }

    private fun initColorDialog() {
        binding.apply {
            colorDialog = ColorDialogHelper(requireContext())
            dialog = colorDialog?.initColorPickerDialog {
                getColorFromPicker(it)
            }
        }
    }

    private fun getColorFromPicker(color: Int) {
        when (colorPickerType) {
            is ColorPickerType.BackgroundColorPicker -> setEraseColor(color)
            is ColorPickerType.BrushColorPicker -> {
                setBrushColor(color)
                setPaintBtn(R.color.gray_background, R.color.white)
            }
        }
    }

    private fun setBrushColor(color: Int) {
        binding.apply {
            paintView.brushColor = color
            colorPickerBtn.setBackgroundColor(color)
        }
    }

    private fun setEraseColor(color: Int) {
        binding.apply {
            paintView.eraserColor = color
            paintView.background = color.toDrawable()
            paintView.updateEraseColor(color)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.paintView.resetPaint()
    }

}