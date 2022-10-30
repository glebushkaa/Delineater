package com.gleb.delineater.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gleb.delineater.ui.BaseFragment
import com.gleb.delineater.R
import com.gleb.delineater.databinding.FragmentDownloadBinding


class DownloadFragment : BaseFragment(R.layout.fragment_download) {

    private lateinit var binding: FragmentDownloadBinding

    override fun initBinding(view: View) {
        binding = FragmentDownloadBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.get("downloadImage")?.let { bitmap ->
            binding.downloadImage.let { imageView ->
                Glide.with(imageView)
                    .load(bitmap)
                    .into(imageView)
            }
        }
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
        }
    }

}