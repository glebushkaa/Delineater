package com.gleb.delineater.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.MenuPictureListener
import com.gleb.delineater.R
import com.gleb.delineater.databinding.FragmentMenuBinding
import com.gleb.delineater.extensions.showSnackBar
import com.gleb.delineater.ui.recycler.MenuPictureAdapter
import com.gleb.delineater.ui.viewModels.PictureMenuViewModel

class MenuFragment : Fragment() {

    private val viewModel: PictureMenuViewModel by viewModels()
    private var binding: FragmentMenuBinding? = null
    private var adapter = MenuPictureAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        initAdapter()
    }

    private fun initAdapter() {
        binding?.menuRecycler?.let {
            it.adapter = adapter
            it.itemAnimator = null
        }
        adapter.setData(listOf())
        adapter.setOnItemClickedListener(
            object : MenuPictureListener {
                override fun showAddPictureInfo(text: String) {
                    findNavController().navigate(R.id.menu_to_draw)
                }

                override fun showPictureInfo(text: String, bitmap: Bitmap) {
                    findNavController().navigate(
                        R.id.menu_to_draw, bundleOf(
                            "bitmap" to bitmap
                        )
                    )
                }
            }
        )
    }

    private fun initClickListeners() {
        binding?.apply {
            optionBtn.setOnClickListener {
                view?.showSnackBar(
                    text = "Options function",
                    backgroundColor = resources.getColor(
                        R.color.white_background,
                        null
                    ),
                    textColor = resources.getColor(
                        R.color.black_background,
                        null
                    )
                )
            }
        }
    }
}