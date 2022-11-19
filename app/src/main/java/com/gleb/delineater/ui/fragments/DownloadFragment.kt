package com.gleb.delineater.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.gleb.delineater.R
import com.gleb.delineater.data.constants.PICTURE
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentDownloadBinding
import com.gleb.delineater.ui.extensions.*
import com.gleb.delineater.ui.viewModels.DownloadViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

private const val PROVIDER_AUTHORITY = "com.gleb.fileprovider.delineater"
private const val SHARE_TYPE = "image/*"

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
            setDownloadImage(it.picturePath.orEmpty())
        }
    }

    private fun FragmentDownloadBinding.initClickListeners() {
        backBtn.setOnClickListener {
            arguments?.putParcelable(PICTURE, viewModel.pictureEntity)
            findNavController().popBackStack()
        }
        menuBtn.setOnClickListener {
            findNavController().navigate(R.id.download_to_menu)
        }
        saveBtn.setOnClickListener {
            reduceButtonX()
            viewModel.saveGalleryPicture(
                bitmap = downloadImage.drawToBitmap(),
                requireContext().contentResolver
            )
        }
        shareBtn.setOnClickListener {
            sharePicture()
        }
    }

    private fun getContentUri(file: File) = FileProvider.getUriForFile(
        requireContext(), PROVIDER_AUTHORITY, file
    )

    private fun reduceButtonX() {
        binding.saveBtn.reduceSaveBtnXSize {
            binding.progressBar.progressFadeAnimation()
            increaseButtonX()
        }
    }

    private fun setDownloadImage(picturePath: String) {
        Glide.with(binding.downloadImage)
            .load(File(picturePath))
            .into(binding.downloadImage)
    }

    private fun increaseButtonX() {
        binding.saveBtn.increaseSaveBtnXSize {
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

    private fun sharePicture() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_STREAM,
                getContentUri(File(viewModel.pictureEntity?.picturePath.orEmpty()))
            )
            type = SHARE_TYPE
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(
            Intent.createChooser(
                shareIntent, getString(
                    R.string.share_via
                )
            )
        )
    }

}