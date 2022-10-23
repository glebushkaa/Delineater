package com.gleb.delineater

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gleb.delineater.databinding.FragmentDrawBinding


class DrawFragment : Fragment() {

    private var binding: FragmentDrawBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDrawBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bitmap = arguments?.get("bitmap") as Bitmap?
        bitmap?.let { bitmapPicture ->
            binding?.imageContainer?.let {
                Glide.with(it)
                    .load(bitmapPicture)
                    .into(it)
            }
        }
        initClickListeners()
    }

    private fun initClickListeners() {
        binding?.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}