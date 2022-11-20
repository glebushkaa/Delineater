package com.gleb.delineater.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.gleb.delineater.R
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentDownloadBinding
import com.gleb.delineater.ui.constants.PICTURE
import com.gleb.delineater.ui.extensions.*
import com.gleb.delineater.ui.intents.sharePicture
import com.gleb.delineater.ui.viewModels.DownloadViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class DownloadFragment : Fragment(R.layout.fragment_download) {

    private val binding: FragmentDownloadBinding by viewBinding()
    private val viewModel: DownloadViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        binding.initClickListeners()
    }

    private fun getArgs() {
        arguments?.getParcelable<PictureEntity>(PICTURE)?.let {
            viewModel.pictureEntity = it
            setDownloadImage(it.picturePath)
        }
    }

    private fun FragmentDownloadBinding.initClickListeners() {
        backBtn.setOnClickListener {
            arguments?.putParcelable(PICTURE, viewModel.pictureEntity)
            requireContext().showToast(viewModel.pictureEntity?.picturePath.toString())
            findNavController().popBackStack()
        }
        menuBtn.setOnClickListener {
            findNavController().navigate(R.id.download_to_menu)
        }
        saveBtn.setOnClickListener {
            downscaleSaveBtn()
            viewModel.saveGalleryPicture(
                bitmap = downloadImage.drawToBitmap(),
                context = requireContext()
            )
        }
        shareBtn.setOnClickListener {
            requireContext().sharePicture(viewModel.pictureEntity?.picturePath.orEmpty())
        }
    }


    private fun downscaleSaveBtn() {
        binding.saveBtn.downscaleSaveBtnX {
            binding.progressBar.progressFadeAnimation()
            upscaleSaveBtn()
        }
    }

    private fun setDownloadImage(picturePath: String) {
        Glide.with(binding.downloadImage)
            .load(File(picturePath))
            .into(binding.downloadImage)
    }

    private fun upscaleSaveBtn() {
        binding.saveBtn.upscaleSaveBtnX {
            showSaveSuccessMessage()
        }
    }

    private fun showSaveSuccessMessage() {
        view?.showSnackBar(
            text = getString(R.string.picture_saved),
            backgroundColor = R.color.snackbar_blue,
            textColor = R.color.white_background,
            gravity = Gravity.TOP
        )
    }

}