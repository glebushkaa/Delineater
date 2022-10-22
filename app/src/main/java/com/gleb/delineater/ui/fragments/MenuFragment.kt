package com.gleb.delineater.ui.fragments

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gleb.delineater.R
import com.gleb.delineater.data.models.PictureModel
import com.gleb.delineater.databinding.FragmentMenuBinding
import com.gleb.delineater.extensions.showSnackBar
import com.gleb.delineater.ui.recycler.MenuPictureAdapter
import com.gleb.delineater.ui.viewModels.PictureMenuViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
        lifecycleScope.launch {
            val a = async { fillList() }
            binding?.menuRecycler?.adapter = adapter
            adapter.setData(a.await())
        }
    }

    private suspend fun fillList() = suspendCoroutine<List<PictureModel>> { emitter ->
        lifecycleScope.launch(Dispatchers.IO) {
            val pictureList = arrayListOf<PictureModel>()
            val drawable = (ResourcesCompat.getDrawable(
                resources, R.drawable.test_img, null
            ) as BitmapDrawable)
            for (i in 1..100) {
                pictureList.add(PictureModel(drawable.bitmap))
            }
            emitter.resume(pictureList)
        }
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