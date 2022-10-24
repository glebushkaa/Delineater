package com.gleb.delineater

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gleb.delineater.PaintView.Companion.paintList
import com.gleb.delineater.databinding.FragmentDrawBinding
import com.gleb.delineater.extensions.showSnackBar


class DrawFragment : Fragment() {

    private var binding: FragmentDrawBinding? = null

    companion object {
        var paint = Paint()
        var path = Path()
        var paintStroke = 10f
    }

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
        /*bitmap?.let { bitmapPicture ->
            binding?.imageContainer?.let {
                Glide.with(it)
                    .load(bitmapPicture)
                    .into(it)
            }
        }*/
        initClickListeners()
    }

    private fun initClickListeners() {
        binding?.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            paintSizeSlider.addOnChangeListener { slider, value, fromUser ->
                paintSize.text = "${value.toInt()}dp"
                paintStroke = value
            }
            brushBtn.setOnClickListener {
                view?.showSnackBar("Brush Button")
            }
            eraseBtn.setOnClickListener {
                view?.showSnackBar("Erase Button")
            }
            colorPickerBtn.setOnClickListener {
                view?.showSnackBar("Color Pick Button")
                val l = arrayListOf<Float>()
                paintList.forEach{
                    l.add(it.paint.strokeWidth)
                }
                Log.d("ARRRRRRR",l.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        path = Path()
        paintList.clear()
    }

}