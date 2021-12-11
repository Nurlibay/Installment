package uz.texnopos.installment.ui.main.addclient

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.RESULT_ERROR
import com.github.dhaval2404.imagepicker.util.IntentUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.core.imagehelper.pickCameraImage
import uz.texnopos.installment.core.imagehelper.pickGalleryImage
import uz.texnopos.installment.core.imagehelper.setLocalImage
import uz.texnopos.installment.core.mask.MaskWatcherPhone
import uz.texnopos.installment.data.model.PostClient
import uz.texnopos.installment.databinding.FragmentAddClientBinding
import uz.texnopos.installment.core.Constants.NO_INTERNET
import uz.texnopos.installment.data.model.Phone
import uz.texnopos.installment.ui.main.addclient.phone.PhoneAdapter
import java.io.File

class AddClientFragment : Fragment(R.layout.fragment_add_client) {

    private val viewModel by viewModel<AddClientViewModel>()
    private var mPassportImageUri: Uri? = null
    private var mLetterImageUri: Uri? = null
    private val phoneAdapter=PhoneAdapter()
    private lateinit var bind: FragmentAddClientBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentAddClientBinding.bind(view)
        setUpObserver()
        bind.apply {
            rvPhones.adapter=phoneAdapter
            phoneAdapter.add()
            addPhone.onClick {
                phoneAdapter.add()
            }
            etPassportSeries.filters= arrayOf(InputFilter.AllCaps(),InputFilter.LengthFilter(2))
            toolbar.navOnClick {
                requireActivity().onBackPressed()
            }

            selectPassportImage.apply {
                txtGallery.text = getString(R.string.select_passport_title)
                fabAddGalleryPhoto.onClick {
                    imageUploadDialog(PASSPORT)
                }
                imgGallery.onClick {
                    showImage(this)
                }
            }

            selectLatterImage.apply {
                txtGallery.text = getString(R.string.select_letter_title)
                fabAddGalleryPhoto.onClick {
                    imageUploadDialog(LETTER)
                }
                imgGallery.onClick {
                    showImage(this)
                }
            }

            btnSignUp.onClick {
                if (validate()) {
                    etName.text.toString()
                    val newClient = PostClient(
                        firstName = etName.textToString(),
                        lastName = etSurname.textToString(),
                        middleName = etPatronymic.textToString(),
                        passportSerial = etPassportSeries.textToString(),
                        passportNumber = etPassportNumber.textToString(),
                        passportPhoto = File(mPassportImageUri?.path!!),
                        letter = File(mLetterImageUri?.path!!),
                        phone1 = "etPhone1.textToString().getOnlyDigits()",
                        phone2 = "etPhone2.textToString().getOnlyDigits()"
                    )
                    viewModel.register(newClient)
                }
            }
        }

    }

    private fun setUpObserver() {
        viewModel.register.observe(requireActivity(), {
            when (it.status) {
                ResourceState.LOADING -> showProgress()
                ResourceState.SUCCESS -> {
                    hideProgress()
                    toast(it.data!!.message)
                    requireActivity().onBackPressed()
                }
                ResourceState.ERROR -> {
                    hideProgress()
                    toast(it.message!!)
                }
                ResourceState.NETWORK_ERROR -> {
                    hideProgress()
                    toast(NO_INTERNET)
                }
            }
        })
    }
    private fun Context.drawableToUri(drawable: Int):Uri{
        return Uri.parse("android.resource://$packageName/$drawable")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                val uri: Uri = data?.data!!
                when (requestCode) {
                    PASSPORT_GALLERY_REQ_CODE, PASSPORT_CAMERA_REQ_CODE -> {
                        this.mPassportImageUri = uri
                        bind.selectPassportImage.imgGallery.setLocalImage(uri)
                    }
                    LETTER_GALLERY_REQ_CODE, LETTER_CAMERA_REQ_CODE -> {
                        this.mLetterImageUri = uri
                        bind.selectLatterImage.imgGallery.setLocalImage(uri)
                    }
                }
            }
            RESULT_ERROR -> {
                toast(ImagePicker.getError(data))
            }
        }
    }


    private fun showImage(view: View) {
        val uri = when (view) {
            bind.selectPassportImage.imgGallery -> this.mPassportImageUri
            bind.selectLatterImage.imgGallery -> this.mLetterImageUri
            else -> null
        }
        uri?.let {
            startActivity(IntentUtils.getUriViewIntent(requireContext(), uri))
        }
    }

    private fun imageUploadDialog(type: Int) {
        val dialog = ImageUploadDialog(this)
        dialog.onGallerySelected {
            when (type) {
                PASSPORT -> pickGalleryImage(PASSPORT_GALLERY_REQ_CODE)
                LETTER -> pickGalleryImage(LETTER_GALLERY_REQ_CODE)
            }
        }
        dialog.onCameraSelected {
            when (type) {
                PASSPORT -> pickCameraImage(PASSPORT_CAMERA_REQ_CODE)
                LETTER -> pickCameraImage(LETTER_CAMERA_REQ_CODE)
            }
        }
    }

    private fun FragmentAddClientBinding.validate(): Boolean {
        return when {
            etName.checkIsEmpty() -> etName.showError(getString(R.string.required))
            etSurname.checkIsEmpty() -> etSurname.showError(getString(R.string.required))
            etPatronymic.checkIsEmpty() -> etPatronymic.showError(getString(R.string.required))
            etPassportSeries.checkIsEmpty() -> etPassportSeries.showError(getString(R.string.required))
            etPassportNumber.checkIsEmpty() -> etPassportNumber.showError(getString(R.string.required))
            mPassportImageUri == null -> {
                toast("Passport image required")
                false
            }
            mLetterImageUri == null -> {
                toast("Letter image required")
                false
            }
//            etPhone1.checkIsEmpty() -> etPhone1.showError(getString(R.string.required))
//            etPhone2.checkIsEmpty() -> etPhone2.showError(getString(R.string.required))
            else -> true
        }
    }

    companion object {

        private const val PASSPORT_GALLERY_REQ_CODE = 102
        private const val PASSPORT_CAMERA_REQ_CODE = 103
        private const val LETTER_GALLERY_REQ_CODE = 104
        private const val LETTER_CAMERA_REQ_CODE = 105

        private const val PASSPORT = 0
        private const val LETTER = 1
    }
}