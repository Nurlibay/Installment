package uz.texnopos.installment.ui.main.clients

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isInvisible
import uz.texnopos.installment.R
import uz.texnopos.installment.databinding.FragmentClientsBinding


class FloatingAnimation(private val bind: FragmentClientsBinding) {
    private val context: Context = bind.root.context
    private var isOpen = false
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom_anim
        )
    }

    fun onFloatingClicked() {
        setAnimation(isOpen)
        setVisibility(isOpen)
        isOpen = !isOpen
    }

    private fun setVisibility(clicked: Boolean) {
        bind.floatingAddButton.isInvisible = clicked
        bind.floatingCalcButton.isInvisible = clicked
    }

    private fun setAnimation(clicked: Boolean) {
        val anim=if (!clicked) fromBottom else toBottom
        val rotateAnim=if (!clicked) rotateOpen else rotateClose
        bind.floatingAddButton.startAnimation(anim)
        bind.floatingCalcButton.startAnimation(anim)
        bind.floatingButton.startAnimation(rotateAnim)
    }

    fun show() {
        bind.floatingButton.show()
        if (isOpen) {
            onFloatingClicked()
        }
    }

    fun hide() {
        bind.floatingButton.hide()
        if (isOpen) {
            onFloatingClicked()
        }
    }

}