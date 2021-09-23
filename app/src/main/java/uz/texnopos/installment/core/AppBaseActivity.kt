package uz.texnopos.installment.core

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.texnopos.installment.R

open class AppBaseActivity : AppCompatActivity() {
    var showing = true
    private var progressDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog?.window?.setBackgroundDrawable(ColorDrawable(0))
            progressDialog?.setContentView(R.layout.progress_dialog)
        }
    }

    fun showProgress(show: Boolean) {
        when {
            show -> {
                if (!isFinishing && !progressDialog!!.isShowing) {
                    progressDialog?.setCanceledOnTouchOutside(false)
                    progressDialog?.setCancelable(false)
                    progressDialog?.show()
                    showing = false
                }
            }
            else -> try {
                if (progressDialog?.isShowing!! && !isFinishing) {
                    progressDialog?.dismiss()
                    showing = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}