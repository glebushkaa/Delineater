package com.gleb.delineater.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gleb.delineater.R
import com.gleb.delineater.data.constants.PICTURE
import com.gleb.delineater.data.constants.PICTURES_LIST
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentMenuBinding
import com.gleb.delineater.listeners.MenuPictureListener
import com.gleb.delineater.ui.recycler.adapter.MenuPictureAdapter
import com.gleb.delineater.ui.viewModels.MenuViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val viewModel: MenuViewModel by viewModel()
    private val binding: FragmentMenuBinding by viewBinding()
    private val adapter = MenuPictureAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        getArgs()
    }

    private fun initAdapter() {
        binding.menuRecycler.also {
            it.adapter = adapter
        }
        adapter.setOnItemClickedListener(
            object : MenuPictureListener {
                override fun openExistPicture(picture: PictureEntity) {
                    findNavController().navigate(
                        R.id.menu_to_draw,
                        bundleOf(PICTURE to picture)
                    )
                }

                override fun openNewPicture() {
                    findNavController().navigate(R.id.menu_to_draw)
                }

                override fun deletePicture(picture: PictureEntity) {
                    viewModel.deleteImage(picture)
                }
            }
        )
    }

    private fun initObservers() {
        viewModel.pictureLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    private fun getArgs() {
        arguments?.getParcelableArrayList<PictureEntity>(PICTURES_LIST)?.let { pictureEntities ->
            viewModel.initPictureList(pictureEntities)
        } ?: run {
            viewModel.getAllPictures()
        }
    }
}