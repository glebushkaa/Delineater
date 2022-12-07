package com.gleb.delineater.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.gleb.delineater.R
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentDownloadBinding
import com.gleb.delineater.ui.constants.IS_NEW_PICTURE
import com.gleb.delineater.ui.constants.NEW_SAVED_PICTURE
import com.gleb.delineater.ui.constants.PICTURE
import com.gleb.delineater.ui.constants.PICTURE_REQUEST_KEY
import com.gleb.delineater.ui.extensions.*
import com.gleb.delineater.ui.intents.sharePicture
import com.gleb.delineater.ui.viewModels.DownloadViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DownloadFragment : Fragment(R.layout.fragment_download) {

    private val binding: FragmentDownloadBinding by viewBinding()
    private val viewModel: DownloadViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        binding.initClickListeners()
        viewModel.addCurrentPicture()
    }

    private fun getArgs() {
        arguments?.also {
            it.getParcelable<PictureEntity>(PICTURE)?.also { picture ->
                viewModel.currentPicture = picture
                binding.downloadImage.setDownloadImage(picture.picturePath)
            }
            viewModel.isNewPicture = it.getBoolean(IS_NEW_PICTURE)
        }
    }

    private fun FragmentDownloadBinding.initClickListeners() {
        backBtn.setOnClickListener {
            setSavedPictureResult()
            findNavController().popBackStack()
        }
        menuBtn.setOnClickListener {
            findNavController().navigate(R.id.download_to_menu)
        }
        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                saveBtn.animSaving(saveProgress)
                showSaveSuccessMessage()
            }
            viewModel.saveGalleryPicture(
                bitmap = downloadImage.drawToBitmap(),
                context = requireContext()
            )
        }
        shareBtn.setOnClickListener {
            requireContext().sharePicture(
                viewModel.currentPicture?.picturePath.orEmpty()
            )
        }
    }

    private fun setSavedPictureResult() {
        setFragmentResult(
            PICTURE_REQUEST_KEY,
            bundleOf(NEW_SAVED_PICTURE to viewModel.currentPicture)
        )
    }

    private fun ImageView.setDownloadImage(picturePath: String) {
        val file = File(picturePath)
        Glide.with(this)
            .load(file)
            .into(this)
    }

    private fun showSaveSuccessMessage() {
        binding.coordinator.showSnackBar(
            text = getString(R.string.picture_saved),
            backgroundColor = R.color.snackbar_blue,
            textColor = R.color.white_background,
            gravity = Gravity.TOP
        )
    }

    private suspend fun Button.animSaving(progressBar: ProgressBar) {
        hideSaveBtn()
        progressBar.animSaveProgress()
        showSaveBtn()
    }

    private suspend fun Button.hideSaveBtn() = suspendCoroutine<Unit> { continuation ->
        val alpha = 0f
        val delay = 0L
        val duration = 500L
        animate().alpha(alpha).also {
            it.startDelay = delay
            it.duration = duration
            it.withEndAction { continuation.resume(Unit) }
            it.start()
        }
    }

    private suspend fun Button.showSaveBtn() = suspendCoroutine<Unit> { continuation ->
        val duration = 3000L
        val alpha = 1f
        animate().alpha(alpha).also {
            it.startDelay = duration
            it.withEndAction { continuation.resume(Unit) }
            it.start()
        }
    }

    private fun ProgressBar.animSaveProgress() {
        val scaleSize = 1f
        val duration = 1400L
        val alpha = 0f
        animate().alpha(scaleSize).also {
            it.duration = duration
            it.withEndAction {
                animate().alpha(alpha).start()
            }
            it.start()
        }
    }

}