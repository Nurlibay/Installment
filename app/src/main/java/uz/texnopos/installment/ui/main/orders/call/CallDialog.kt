package uz.texnopos.installment.ui.main.orders.call

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.fragment.app.DialogFragment
import timber.log.Timber
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.core.Constants.ASK_PHONE_PERMISSION_REQUEST_CODE
import uz.texnopos.installment.data.model.Client
import uz.texnopos.installment.data.model.Phone
import uz.texnopos.installment.databinding.CallDialogBinding

class CallDialog : DialogFragment() {

    private lateinit var binding: CallDialogBinding
    private val adapter = CallAdapter()
    private var client: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = arguments?.getParcelable(Constants.CLIENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        dialog!!.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.call_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CallDialogBinding.bind(view).apply {
            rvPhoneList.adapter = adapter
            btnClose.onClick {
                dialog!!.dismiss()
            }
        }
        setData()
        adapter.onItemClick {
            callToUser(it.phone)
        }
    }

    private fun setData(){
        for(i in 0..4) {
            adapter.models.add(i, Phone("+998 99 953 56 06"))
        }
    }

    private fun callToUser(phoneNumber: String) {
        if (isHasPermission(Manifest.permission.CALL_PHONE)) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        } else askPermission(
            arrayOf(Manifest.permission.CALL_PHONE),
            ASK_PHONE_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == Constants.ASK_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (client != null) {
                    //for(i in 0 until client.phones.)
                }
            } else {
                toast("PERMISSION DENIED")
            }
        }
    }

}