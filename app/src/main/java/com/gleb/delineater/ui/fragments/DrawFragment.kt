package com.gleb.delineater.ui.fragments

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
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
import com.gleb.delineater.ui.dialogs.ColorPickerDialog
import com.gleb.delineater.ui.dialogs.SaveEditsDialog
import com.gleb.delineater.ui.extensions.*
import com.gleb.delineater.ui.listeners.SaveEditsListener
import com.gleb.delineater.ui.types.ColorPickerType
import com.gleb.delineater.ui.types.PaintType
import com.gleb.delineater.ui.viewModels.DrawViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DrawFragment : Fragment(R.layout.fragment_draw) {

    private val binding: FragmentDrawBinding by viewBinding()
    private val viewModel: DrawViewModel by viewModel()

    private val colorPickDialog = ColorPickerDialog()
    private val saveEditsDialog = SaveEditsDialog()

    private val storagePermissionsArray = arrayOf(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    private var settingsActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

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
        initColorPickerDialogListener()
        initSaveEditsDialogListener()
        setPaintBackgroundPicture()
    }


    private fun setSavedPictureResultListener() {
        setFragmentResultListener(PICTURE_REQUEST_KEY) { _, bundle ->
            viewModel.currentPicture = bundle.getParcelable(NEW_SAVED_PICTURE)
        }
    }

    private fun initColorPickerDialogListener() {
        colorPickDialog.colorListener = {
            binding.getColorFromPicker(viewModel.colorPickerType, it)
        }
    }

    private fun initSaveEditsDialogListener() {
        saveEditsDialog.editsListener = object : SaveEditsListener {
            override fun saveEdits() {
                binding.paintView.drawToBitmap().saveAlbumImage {
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

    private fun getArgs() {
        arguments?.getParcelable<PictureEntity>(PICTURE)?.let {
            viewModel.currentPicture = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun FragmentDrawBinding.initClickListeners() {
        backBtn.setOnClickListener {
            if (paintView.checkEdits()) {
                saveEditsDialog.show(parentFragmentManager, "")
            } else {
                findNavController().popBackStack()
            }
        }
        downloadBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                downloadStoragePermissionAction()
            }else{
                requireContext().showToast("READ: ${ContextCompat.checkSelfPermission(
                    requireContext(),
                    MANAGE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED}\t")
                downloadStoragePermission.launch(storagePermissionsArray)
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
            colorPickDialog.show(parentFragmentManager, "")
        }
        refreshBtn.setOnClickListener {
            paintView.resetSurface()
        }
        fillBackBtn.setOnClickListener {
            viewModel.colorPickerType = ColorPickerType.BackgroundColorPicker
            colorPickDialog.show(parentFragmentManager, "")
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
        binding.paintView.drawToBitmap().saveAlbumImage {
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

    private fun setPaintBackgroundPicture() {
        viewModel.currentPicture?.let {
            requireContext().decodePictureFile(it.picturePath) { picture ->
                viewModel.isNewPicture = false
                binding.paintView.background = picture
            }
        }
    }

    private fun showStoragePermissionMessage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view?.showSnackBar(
                text = getString(R.string.allow_read_files),
                action = Pair(
                    getString(R.string.provide_storage_access),
                    provideStorageMessageAction()
                ),
                actionColor = R.color.blue
            )
        } else {
            view?.showSnackBar(
                text = getString(R.string.allow_read_files)
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun provideStorageMessageAction() = View.OnClickListener {
        val intent = Intent(
            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
            Uri.parse("package:" + requireActivity().packageName)
        )
        settingsActivityResult.launch(intent)
    }

}