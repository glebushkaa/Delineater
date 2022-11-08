package com.gleb.delineater.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gleb.delineater.R
import com.gleb.delineater.data.MediaHelper
import com.gleb.delineater.data.constants.DOWNLOAD_IMAGE
import com.gleb.delineater.databinding.FragmentDownloadBinding
import com.gleb.delineater.ui.BaseFragment
import org.koin.android.ext.android.inject
import java.io.File


class DownloadFragment : BaseFragment(R.layout.fragment_download) {

    private lateinit var binding: FragmentDownloadBinding
    private val mediaHelper: MediaHelper by inject()

    private var picturePath: String? = null

    override fun initBinding(view: View) {
        binding = FragmentDownloadBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        initClickListeners()
    }

    private fun initClickListeners() {
        binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            menuBtn.setOnClickListener {
                findNavController().navigate(R.id.download_to_menu)
            }
            saveBtn.setOnClickListener {
                doButtonReduction()
                mediaHelper.savePictureToGallery(downloadImage.drawable.toBitmap())
            }
            shareBtn.setOnClickListener {
                sharePicture()
            }
        }
    }

    private fun sharePicture() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, getContentUri(File(picturePath.orEmpty())))
            putExtra(Intent.EXTRA_TEXT, "Aplikácia funguje výborné!")
            type = "image/*"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(Intent.createChooser(shareIntent, "Čo хочешь!?"))
    }

    private fun getContentUri(file: File) =
        FileProvider.getUriForFile(requireContext(), "com.gleb.fileprovider.delineater", file)

    private fun doButtonReduction() {
        binding.saveBtn.animate().scaleX(0f).alpha(0f).withEndAction {
            binding.progressBar.visibility = View.VISIBLE
            binding.saveBtn.animate()
                .scaleX(1f).alpha(1f).withEndAction {
                    binding.progressBar.visibility = View.GONE
                }.startDelay = 2000
        }

    }

    private fun getArgs() {
        arguments?.getString(DOWNLOAD_IMAGE)?.let { picturePath ->
            this.picturePath = picturePath
            Glide.with(binding.downloadImage)
                .load(File(picturePath))
                .into(binding.downloadImage)
        }
    }

}