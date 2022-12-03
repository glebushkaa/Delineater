package com.gleb.delineater.ui.dialogs

import com.gleb.delineater.databinding.DialogSaveEditsBinding
import com.gleb.delineater.ui.extensions.blurFadeAnim
import com.gleb.delineater.ui.extensions.defaultFadeAnim
import com.gleb.delineater.ui.extensions.hideFadeAnim

class SaveEditsDialog(private val saveEditsBinding: DialogSaveEditsBinding) {

    var discardEditsListener: (() -> Unit)? = null
    var saveEditsListener: (() -> Unit)? = null

    fun initClickListeners() {
        saveEditsBinding.apply {
            discardEditsBtn.setOnClickListener {
                hide(discardEditsListener)
            }
            saveEditsBtn.setOnClickListener {
                saveEditsListener?.invoke()
            }
            saveEditsBlur.setOnClickListener {
                hide()
            }
            saveDialogCard.isClickable = true
        }
    }

    fun show() {
        saveEditsBinding.apply {
            saveEditsBlur.blurFadeAnim()
            saveDialogCard.defaultFadeAnim()
        }
    }

    fun hide(endAction: (() -> Unit)? = null) {
        saveEditsBinding.apply {
            saveEditsBlur.hideFadeAnim()
            saveDialogCard.hideFadeAnim {
                endAction?.invoke()
            }
        }
    }

}