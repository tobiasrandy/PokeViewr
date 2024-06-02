package com.app.pokeviewr.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.pokeviewr.R
import com.app.pokeviewr.R.drawable
import com.app.pokeviewr.ui.dialog.LoadingDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

open class BaseActivity: AppCompatActivity() {
    lateinit var activity: BaseActivity
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        loadingDialog = LoadingDialog(activity)

    }

    fun showSnackbar(view: View?, message: String?, isError: Boolean) {
        snackbar = Snackbar.make(view!!, message!!, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        snackbar.view.background = ContextCompat.getDrawable(this, drawable.snackbar_background)
        snackbar.setBackgroundTint(ContextCompat.getColor(this, if (isError) R.color.md_red_300 else R.color.md_green_A700))

        val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin + 125)
        snackbar.view.layoutParams = params

        snackbar.setAction("DISMISS") {
            snackbar.dismiss()
        }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.md_grey_200))

        snackbar.show()
    }

    fun showLoadingDialog() {
        loadingDialog.show()
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideLoadingDialog()
    }
}