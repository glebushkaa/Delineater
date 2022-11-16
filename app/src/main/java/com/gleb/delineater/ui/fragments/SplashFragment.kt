package com.gleb.delineater.ui.fragments

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.R
import com.gleb.delineater.ui.extensions.showSnackBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val permissionsArray = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var storagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { !it.value }) {
                view?.showSnackBar(text = getString(R.string.allow_read_files))
            }
            navigateToMenu()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storagePermissionLauncher.launch(permissionsArray)
    }

    private fun navigateToMenu(){
        lifecycleScope.launch {
            delay(1000)
            findNavController().navigate(R.id.splash_to_menu)
        }
    }

}