package com.gleb.delineater.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.R
import com.gleb.delineater.data.constants.*
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentMenuBinding
import com.gleb.delineater.extensions.showToast
import com.gleb.delineater.listeners.MenuPictureListener
import com.gleb.delineater.ui.BaseFragment
import com.gleb.delineater.ui.recycler.adapter.MenuPictureAdapter
import com.gleb.delineater.ui.viewModels.MenuViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : BaseFragment(R.layout.fragment_menu) {

    private val viewModel: MenuViewModel by viewModel()
    private val adapter = MenuPictureAdapter()
    private lateinit var binding: FragmentMenuBinding

    override fun initBinding(view: View) {
        binding = FragmentMenuBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        getArgs()
    }

    private fun initAdapter() {
        binding.menuRecycler.let {
            it.adapter = adapter
            it.itemAnimator = null
        }
        adapter.setOnItemClickedListener(
            object : MenuPictureListener {
                override fun showAddPictureInfo(text: String) {
                    findNavController().navigate(
                        R.id.menu_to_draw
                    )
                }

                override fun showPictureInfo(text: String, picture: PictureEntity) {
                    findNavController().navigate(
                        R.id.menu_to_draw, bundleOf(
                            PICTURE to picture
                        )
                    )
                }
                override fun deleteImage(picture: PictureEntity) {
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