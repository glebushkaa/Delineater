package com.gleb.delineater.ui.fragments

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gleb.delineater.R
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.extensions.cachePicture
import com.gleb.delineater.data.extensions.decodePictureFile
import com.gleb.delineater.databinding.FragmentDrawBinding
import com.gleb.delineater.ui.constants.IS_NEW_PICTURE
import com.gleb.delineater.ui.constants.NEW_SAVED_PICTURE
import com.gleb.delineater.ui.constants.PICTURE
import com.gleb.delineater.ui.constants.PICTURE_REQUEST_KEY
import com.gleb.delineater.ui.dialogs.ColorPickerDialog
import com.gleb.delineater.ui.dialogs.SaveEditsDialog
import com.gleb.delineater.ui.extensions.setIconTint
import com.gleb.delineater.ui.extensions.showSnackBar
import com.gleb.delineater.ui.listeners.SaveEditsListener
import com.gleb.delineater.ui.types.ColorPickerType
import com.gleb.delineater.ui.types.PaintType
import com.gleb.delineater.ui.viewModels.DrawViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SAVE_DIALOG_TAG = "save_dialog"
private const val COLOR_PICKER_DIALOG_TAG = "color_picker_dialog"

private const val BAR_ANIM_DURATION = 600L

class DrawFragment : Fragment(R.layout.fragment_draw) {

    private val binding: FragmentDrawBinding by viewBinding()
    private val viewModel: DrawViewModel by viewModel()

    private val colorPickDialog = ColorPickerDialog()
    private val saveEditsDialog = SaveEditsDialog()

    private val storagePermissionsArray = arrayOf(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    private var downloadStoragePermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { !it.value }) {
            showStoragePermissionMessage()
            return@registerForActivityResult
        }
        downloadStoragePermissionAction()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getArgs()
        setSavedPictureResultListener()
        binding.initClickListeners()
        binding.setColors()
        binding.providePaintingListener()
        provideColorPickerDialogListener()
        provideSaveEditsDialogListener()
        setPaintBackgroundPicture()
    }


    private fun setSavedPictureResultListener() {
        setFragmentResultListener(PICTURE_REQUEST_KEY) { _, bundle ->
            viewModel.currentPicture = bundle.getParcelable(NEW_SAVED_PICTURE)
        }
    }

    private fun getArgs() {
        arguments?.getParcelable<PictureEntity>(PICTURE)?.let {
            viewModel.currentPicture = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentDrawBinding.initClickListeners() {
        backBtn.setOnClickListener {
            if (paintView.checkEdits()) {
                saveEditsDialog.show(parentFragmentManager, SAVE_DIALOG_TAG)
            } else {
                findNavController().popBackStack()
            }
        }
        downloadBtn.setOnClickListener {
            if (SDK_INT >= VERSION_CODES.R) {
                downloadStoragePermissionAction()
            } else {
                checkStoragePermission()
            }
        }
        paintSizeSlider.addOnChangeListener { _, size, _ ->
            paintSize.text = "${size.toInt()}dp"
            paintView.brushWidth = size
        }
        brushBtn.setOnClickListener {
            setPaintIconColor(PaintType.Brush)
        }
        eraseBtn.setOnClickListener {
            setPaintIconColor(PaintType.Eraser)
        }
        colorPickerBtn.setOnClickListener {
            viewModel.colorPickerType = ColorPickerType.BrushColorPicker
            colorPickDialog.show(parentFragmentManager, COLOR_PICKER_DIALOG_TAG)
        }
        refreshBtn.setOnClickListener {
            paintView.resetSurface()
        }
        fillBackBtn.setOnClickListener {
            viewModel.colorPickerType = ColorPickerType.BackgroundColorPicker
            colorPickDialog.show(parentFragmentManager, COLOR_PICKER_DIALOG_TAG)
        }
        stepBackBtn.setOnClickListener {
            paintView.removeLastStep()
        }
        restoreStepBtn.setOnClickListener {
            paintView.restoreDeletedStep()
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
            is ColorPickerType.BackgroundColorPicker -> {
                setPaintIconColor(paintType = PaintType.Eraser)
                paintView.setEraser(color)
            }
            is ColorPickerType.BrushColorPicker -> {
                setPaintIconColor(paintType = PaintType.Brush)
                paintView.brushColor = color
                colorPickerBtn.setBackgroundColor(color)
            }
        }
    }

    private fun FragmentDrawBinding.setPaintIconColor(paintType: PaintType) {
        val paintIconsList = mapOf(
            PaintType.Eraser to eraseBtn, PaintType.Brush to brushBtn
        )
        paintView.paintType = paintType
        paintIconsList.forEach {
            if (it.key == paintType) {
                it.value.setIconTint(R.color.poor_white)
            } else {
                it.value.setIconTint(R.color.poor_gray)
            }
        }
    }

    private fun downloadStoragePermissionAction() {
        lifecycleScope.launch {
            val filePath = binding.paintView.drawToBitmap().cachePicture()
            viewModel.setNewPicturePath(filePath)
            findNavController().navigate(
                R.id.draw_to_download,
                bundleOf(
                    PICTURE to viewModel.currentPicture,
                    IS_NEW_PICTURE to viewModel.isNewPicture
                )
            )
        }
    }

    private fun setPaintBackgroundPicture() {
        lifecycleScope.launch {
            viewModel.currentPicture?.let {
                runCatching {
                    val picture = requireContext().decodePictureFile(it.picturePath)
                    viewModel.isNewPicture = false
                    binding.paintView.background = picture
                }.recover {
                    view?.showSnackBar(
                        text = getString(R.string.project_not_found),
                        backgroundColor = R.color.cancel_red
                    )
                }
            }
        }
    }

    private fun showStoragePermissionMessage() {
        view?.showSnackBar(
            text = getString(R.string.allow_read_files)
        )
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            downloadStoragePermissionAction()
        } else {
            downloadStoragePermission.launch(storagePermissionsArray)
        }
    }

    private fun provideColorPickerDialogListener() {
        colorPickDialog.colorListener = {
            binding.getColorFromPicker(viewModel.colorPickerType, it)
        }
    }

    private fun provideSaveEditsDialogListener() {
        saveEditsDialog.editsListener = object : SaveEditsListener {
            override fun saveEdits() {
                lifecycleScope.launch {
                    val filePath = binding.paintView.drawToBitmap().cachePicture()
                    viewModel.setNewPicturePath(filePath)
                    viewModel.addCurrentPicture()
                    findNavController().popBackStack()
                }
            }

            override fun discardEdits() {
                findNavController().popBackStack()
            }

            override fun deniedPermission() {
                showStoragePermissionMessage()
            }
        }
    }

    private fun FragmentDrawBinding.providePaintingListener() {
        val defaultY = 0f
        val hideTopBarY = -200f
        val hideBottomBarY = 200f
        paintView.onDownListener = {
            bottomBar.animBar(defaultY, hideBottomBarY)
            topBar.animBar(defaultY, hideTopBarY)
        }
        paintView.onUpListener = {
            bottomBar.animBar(hideBottomBarY, defaultY)
            topBar.animBar(hideTopBarY, defaultY)
        }
    }

    private fun View.animBar(startY: Float, endY: Float) {
        animate().translationYBy(startY).translationY(endY).also {
            it.duration = BAR_ANIM_DURATION
            it.start()
        }
    }
}