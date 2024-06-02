package com.app.pokeviewr.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.app.pokeviewr.R

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        initializeDialog()
    }

    private fun initializeDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.loading_dialog)
    }
}