package com.gleb.delineater.ui.fragments

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.R
import com.gleb.delineater.databinding.FragmentSplashBinding
import com.gleb.delineater.ui.viewModels.PictureMenuViewModel
import com.gleb.delineater.ui.viewModels.SplashViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()
    private var binding: FragmentSplashBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawable = (ResourcesCompat.getDrawable(
            resources, R.drawable.test_img, null
        ) as BitmapDrawable)
        lifecycleScope.launch {
            delay(2000)
            val list = async { viewModel.fillPictureList(drawable) }
            findNavController().navigate(
                R.id.splash_to_picture_menu,
                bundleOf(
                    "list" to list.await()
                )
            )
        }
    }

}