package com.gleb.delineater.ui.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.R
import com.gleb.delineater.data.constants.PICTURES_LIST
import com.gleb.delineater.data.constants.PICTURE_PATH
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.FragmentMenuBinding
import com.gleb.delineater.listeners.MenuPictureListener
import com.gleb.delineater.ui.BaseFragment
import com.gleb.delineater.ui.recycler.adapter.MenuPictureAdapter
import com.gleb.delineater.ui.viewModels.MenuViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

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
        /* saveImage(resources.getDrawable(R.drawable.test_img, null)) {
             viewModel.addPicture(
                 PictureEntity(
                     picturePath = it
                 )
             )
         }*/
    }

    private fun saveImage(drawable: Drawable, callback: (String) -> Unit) {
        val file = getDisc()

        if (!file.exists() && !file.mkdirs()) {
            file.mkdir()
        }

        val uuid = UUID.randomUUID().toString()
        val name = "picture" + uuid + ".jpg"
        val fileName = file.absolutePath + "/" + name
        val newFile = File(fileName)

        try {
            val draw = drawable as BitmapDrawable
            val bitmap = draw.bitmap
            val fileOutPutStream = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutPutStream)
            fileOutPutStream.flush()
            fileOutPutStream.close()
            callback(fileName)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getDisc(): File {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(file, "PictureAlbum")
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

                override fun showPictureInfo(text: String, picturePath: String) {
                    findNavController().navigate(
                        R.id.menu_to_draw, bundleOf(
                            PICTURE_PATH to picturePath
                        )
                    )
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
            adapter.setData(pictureEntities)
        } ?: run {
            viewModel.getAllPictures()
        }
    }
}