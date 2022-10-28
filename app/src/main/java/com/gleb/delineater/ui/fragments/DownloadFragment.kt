package com.gleb.delineater.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gleb.delineater.R
import com.gleb.delineater.databinding.FragmentDownloadBinding


class DownloadFragment : Fragment() {

    private var binding: FragmentDownloadBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.get("downloadImage")?.let { bitmap ->
            binding?.downloadImage?.let { imageView ->
                Glide.with(imageView)
                    .load(bitmap)
                    .into(imageView)
            }
        }
        initClickListeners()
    }

    private fun initClickListeners() {
        binding?.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            menuBtn.setOnClickListener {
                findNavController().navigate(R.id.download_to_menu)
                findNavController().backQueue.clear()
            }
        }
    }

}