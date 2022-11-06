package com.gleb.delineater.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gleb.delineater.ui.BaseFragment
import com.gleb.delineater.R
import com.gleb.delineater.data.MediaHelper
import com.gleb.delineater.data.constants.DOWNLOAD_IMAGE
import com.gleb.delineater.databinding.FragmentDownloadBinding
import org.koin.android.ext.android.inject
import java.io.File


class DownloadFragment : BaseFragment(R.layout.fragment_download) {

    private lateinit var binding: FragmentDownloadBinding
    private val mediaHelper: MediaHelper by inject()

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
                mediaHelper.savePictureToGallery(downloadImage.drawable.toBitmap())
            }
        }
    }

    private fun getArgs() {
        arguments?.getString(DOWNLOAD_IMAGE)?.let { picturePath ->
            Glide.with(binding.downloadImage)
                .load(File(picturePath))
                .into(binding.downloadImage)
        }
    }

}