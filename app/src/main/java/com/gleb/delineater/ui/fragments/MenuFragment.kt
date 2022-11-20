package com.gleb.delineater.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gleb.delineater.R
import com.gleb.delineater.ui.constants.PICTURE
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentMenuBinding
import com.gleb.delineater.ui.listeners.ExistPictureListener
import com.gleb.delineater.ui.listeners.NewPictureListener
import com.gleb.delineater.ui.recycler.adapter.ExistPictureAdapter
import com.gleb.delineater.ui.recycler.adapter.NewPictureAdapter
import com.gleb.delineater.ui.viewModels.MenuViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val viewModel: MenuViewModel by viewModel()
    private val binding: FragmentMenuBinding by viewBinding()
    private lateinit var existPictureAdapter: ExistPictureAdapter
    private lateinit var newPictureAdapter: NewPictureAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        viewModel.getAllPictures()
    }

    private fun initAdapter() {
        existPictureAdapter = ExistPictureAdapter(provideExistPictureListener())
        newPictureAdapter = NewPictureAdapter(provideNewPictureListener())
        newPictureAdapter.addItem(true)
        binding.menuRecycler.adapter = ConcatAdapter(existPictureAdapter, newPictureAdapter)
    }

    private fun initObservers() {
        viewModel.pictureLiveData.observe(viewLifecycleOwner) {
            existPictureAdapter.submitList(it.toMutableList())
        }
    }

    private fun provideExistPictureListener() = object : ExistPictureListener {

        override fun deletePicture(picture: PictureEntity) {
            viewModel.deleteImage(picture)
        }

        override fun openPicture(picture: PictureEntity) {
            findNavController().navigate(
                R.id.menu_to_draw,
                bundleOf(PICTURE to picture)
            )
        }
    }

    private fun provideNewPictureListener() = object : NewPictureListener {
        override fun openNewPicture() {
            findNavController().navigate(R.id.menu_to_draw)
        }
    }
}