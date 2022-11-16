package com.gleb.delineater.ui.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gleb.delineater.R
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.extensions.saveAlbumImage
import com.gleb.delineater.data.types.ColorPickerType
import com.gleb.delineater.databinding.FragmentDrawBinding
import com.gleb.delineater.ui.extensions.*
import com.gleb.delineater.ui.viewModels.DrawViewModel
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val PICTURE = "picture"

class DrawFragment : Fragment(R.layout.fragment_draw) {

    private val binding: FragmentDrawBinding by viewBinding()
    private val viewModel: DrawViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        setColorPickView()
        binding.initListeners()
        binding.setColors()
        binding.setColorDialogClickListeners()
        setPaintBackgroundPicture()
    }

    private fun setPaintBackgroundPicture() {
        viewModel.currentPicture?.let {
            binding.paintView.background =
                BitmapFactory.decodeFile(it.picturePath).toDrawable(resources)
        }
    }

    private fun getArgs() {
        viewModel.currentPicture = arguments?.getParcelable(PICTURE)
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentDrawBinding.initListeners() {
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        downloadBtn.setOnClickListener {
            saveAlbumImage()
        }
        paintSizeSlider.addOnChangeListener { _, size, _ ->
            paintSize.text = "${size.toInt()}dp"
            paintView.brushWidth = size
        }
        brushBtn.setOnClickListener {
            enableBrush()
        }
        eraseBtn.setOnClickListener {
            enableEraser()
        }
        colorPickerBtn.setOnClickListener {
            viewModel.colorPickerType = ColorPickerType.BrushColorPicker
            showColorPickerDialog()
        }
        refreshBtn.setOnClickListener {
            paintView.resetSurface()
        }
        fillBackBtn.setOnClickListener {
            viewModel.colorPickerType = ColorPickerType.BackgroundColorPicker
            showColorPickerDialog()
        }
        stepBackBtn.setOnClickListener {
            paintView.removeLastStep()
        }
        restoreStepBtn.setOnClickListener {
            paintView.restoreDeletedStep()
        }
    }

    private fun FragmentDrawBinding.setColorDialogClickListeners() {
        colorPickDialog.apply {
            cancelBtn.setOnClickListener {
                hideColorPickerDialog()
            }
            confirmBtn.setOnClickListener {
                hideColorPickerDialog()
                getColorFromPicker(viewModel.colorPickerType, colorPickerView.color)
            }
            backgroundBlurCard.setOnClickListener {
                hideColorPickerDialog()
            }
        }
    }

    private fun setColorPickView() {
        val bubbleFlag = BubbleFlag(requireContext())
        bubbleFlag.flagMode = FlagMode.FADE
        binding.colorPickDialog.colorPickerView.flagView = bubbleFlag
    }

    private fun saveAlbumImage() {
        binding.paintView.drawToBitmap().saveAlbumImage {
            viewModel.addCurrentPicture(it)
            navigateDownloadFragment(viewModel.currentPicture)
        }
    }

    private fun navigateDownloadFragment(downloadImage: PictureEntity?) {
        findNavController().navigate(
            R.id.draw_to_download,
            bundleOf(PICTURE to downloadImage)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.paintView.resetPaint()
    }

}