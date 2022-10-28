package com.gleb.delineater.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.gleb.delineater.data.room.dao.PictureDao
import com.gleb.delineater.databinding.FragmentSplashBinding
import com.gleb.delineater.ui.viewModels.SplashViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModel()
    private var binding: FragmentSplashBinding? = null

    private val pictureDao: PictureDao by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            pictureDao.getAllPictures()
//            findNavController().navigate(R.id.splash_to_picture_menu)
        }
    }

}