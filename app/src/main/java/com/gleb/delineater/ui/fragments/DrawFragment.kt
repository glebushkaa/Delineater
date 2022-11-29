package com.gleb.delineater.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gleb.delineater.R
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.extensions.decodePictureFile
import com.gleb.delineater.data.extensions.saveAlbumImage
import com.gleb.delineater.databinding.FragmentDrawBinding
import com.gleb.delineater.ui.constants.IS_NEW_PICTURE
import com.gleb.delineater.ui.constants.NEW_SAVED_PICTURE
import com.gleb.delineater.ui.constants.PICTURE
import com.gleb.delineater.ui.constants.PICTURE_REQUEST_KEY
import com.gleb.delineater.ui.extensions.*
import com.gleb.delineater.ui.types.ColorPickerType
import com.gleb.delineater.ui.types.PaintType
import com.gleb.delineater.ui.viewModels.DrawViewModel
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class DrawFragment : Fragment(R.layout.fragment_draw) {

    private val binding: FragmentDrawBinding by viewBinding()
    private val viewModel: DrawViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        setSavedPictureResultListener()
        setColorPickView()
        binding.initListeners()
        binding.setColorDialogClickListeners()
        binding.setSaveEditsClickListeners()
        binding.setColors()
        binding.backPressed()
        setPaintBackgroundPicture()
    }

    private fun setPaintBackgroundPicture() {
        viewModel.currentPicture?.let {
            requireContext().decodePictureFile(it.picturePath) { picture ->
                viewModel.isNewPicture = false
                binding.paintView.background = picture
            }
        }
    }

    private fun setSavedPictureResultListener() {
        setFragmentResultListener(PICTURE_REQUEST_KEY) { _, bundle ->
            bundle.getParcelable<PictureEntity>(NEW_SAVED_PICTURE)?.let {
                viewModel.currentPicture = it
            }
        }
    }

    private fun getArgs() {
        arguments?.getParcelable<PictureEntity>(PICTURE)?.let {
            viewModel.currentPicture = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentDrawBinding.initListeners() {
        backBtn.setOnClickListener {
            if (paintView.checkEdits()) {
                showSaveEditsDialog()
            } else {
                findNavController().popBackStack()
            }
        }
        downloadBtn.setOnClickListener {
            saveAlbumImage {
                viewModel.setNewPicturePath(it)
                findNavController().navigate(
                    R.id.draw_to_download,
                    bundleOf(
                        PICTURE to viewModel.currentPicture,
                        IS_NEW_PICTURE to viewModel.isNewPicture
                    )
                )
            }
        }
        paintSizeSlider.addOnChangeListener { _, size, _ ->
            paintSize.text = "${size.toInt()}dp"
            paintView.brushWidth = size
        }
        brushBtn.setOnClickListener {
            setPaintTypeIcon(
                paintType = PaintType.Brush,
                eraseColor = R.color.poor_gray,
                brushColor = R.color.poor_white
            )
        }
        eraseBtn.setOnClickListener {
            setPaintTypeIcon(
                paintType = PaintType.Eraser,
                eraseColor = R.color.poor_white,
                brushColor = R.color.poor_gray
            )
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
            colorPickerBlur.setOnClickListener {
                hideColorPickerDialog()
            }
            dialogCardView.isClickable = true
        }
    }

    private fun FragmentDrawBinding.setSaveEditsClickListeners() {
        saveEditsDialog.apply {
            discardEditsBtn.setOnClickListener {
                hideSaveEditsDialog {
                    findNavController().popBackStack()
                }
            }
            saveEditsBtn.setOnClickListener {
                saveAlbumImage {
                    viewModel.setNewPicturePath(it)
                    viewModel.addCurrentPicture()
                }
                binding.hideSaveEditsDialog { findNavController().popBackStack() }
            }
            saveEditsBlur.setOnClickListener {
                hideSaveEditsDialog()
            }
            saveDialogCard.isClickable = true
        }
    }

    private fun FragmentDrawBinding.setColors() {
        colorPickerBtn.setBackgroundColor(paintView.brushColor)
        paintView.setBackgroundColor(paintView.eraserColor)
    }

    private fun FragmentDrawBinding.getColorFromPicker(
        colorPickerType: ColorPickerType,
        color: Int
    ) {
        when (colorPickerType) {
            is ColorPickerType.BackgroundColorPicker -> setEraserColor(color)
            is ColorPickerType.BrushColorPicker -> setBrushColor(color)
        }
    }

    private fun FragmentDrawBinding.setBrushColor(color: Int) {
        setPaintTypeIcon(
            paintType = PaintType.Brush,
            eraseColor = R.color.poor_gray,
            brushColor = R.color.poor_white
        )
        paintView.brushColor = color
        colorPickerBtn.setBackgroundColor(color)
    }

    private fun FragmentDrawBinding.setEraserColor(color: Int) {
        setPaintTypeIcon(
            paintType = PaintType.Eraser,
            eraseColor = R.color.poor_gray,
            brushColor = R.color.poor_white
        )
        paintView.setEraser(color)
    }

    private fun FragmentDrawBinding.setPaintTypeIcon(
        paintType: PaintType,
        eraseColor: Int,
        brushColor: Int
    ) {
        paintView.paintType = paintType
        eraseBtn.setIconTint(eraseColor)
        brushBtn.setIconTint(brushColor)
    }

    private fun setColorPickView() {
        val bubbleFlag = BubbleFlag(requireContext())
        bubbleFlag.flagMode = FlagMode.FADE
        binding.colorPickDialog.colorPickerView.flagView = bubbleFlag
    }

    private fun saveAlbumImage(endAction: (String) -> Unit) {
        binding.paintView.drawToBitmap().saveAlbumImage(endAction)
    }

    private fun FragmentDrawBinding.showColorPickerDialog() {
        colorPickDialog.colorPickerBlur.blurFadeAnim()
        colorPickDialog.dialogCardView.translateDialogByXCenter()
    }

    private fun FragmentDrawBinding.hideColorPickerDialog() {
        colorPickDialog.colorPickerBlur.hideFadeAnim()
        colorPickDialog.dialogCardView.translateDialogByXOverBorder()
    }

    private fun FragmentDrawBinding.showSaveEditsDialog() {
        saveEditsDialog.saveEditsBlur.blurFadeAnim()
        saveEditsDialog.saveDialogCard.defaultFadeAnim()
    }

    private fun FragmentDrawBinding.hideSaveEditsDialog(endAction: (() -> Unit)? = null) {
        saveEditsDialog.saveEditsBlur.hideFadeAnim()
        saveEditsDialog.saveDialogCard.hideFadeAnim {
            endAction?.invoke()
        }
    }

    private fun FragmentDrawBinding.backPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (saveEditsDialog.saveDialogCard.isVisible) {
                    hideSaveEditsDialog()
                } else if (colorPickDialog.dialogCardView.isVisible) {
                    hideColorPickerDialog()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

}