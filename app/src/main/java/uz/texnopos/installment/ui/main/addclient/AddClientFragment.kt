package uz.texnopos.installment.ui.main.addclient

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.RESULT_ERROR
import com.github.dhaval2404.imagepicker.util.IntentUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.installment.R
import uz.texnopos.installment.core.*
import uz.texnopos.installment.core.imagehelper.pickCameraImage
import uz.texnopos.installment.core.imagehelper.pickGalleryImage
import uz.texnopos.installment.core.imagehelper.setLocalImage
import uz.texnopos.installment.data.model.PostClient
import uz.texnopos.installment.databinding.FragmentAddClientBinding
import java.io.File


class AddClientFragment : Fragment(R.layout.fragment_add_client) {

    private val viewModel by viewModel<AddClientViewModel>()
    private var mPassportImageUri: Uri? = null
    private var mLetterImageUri: Uri? = null
    private lateinit var bind: FragmentAddClientBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentAddClientBinding.bind(view)
        navController = Navigation.findNavController(view)
        setUpObserver()
        bind.apply {

            etPhone1.addTextChangedListener(MaskWatcherPhone.phoneNumber())
            etPhone2.addTextChangedListener(MaskWatcherPhone.phoneNumber())
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
                    val newClient = PostClient(
                        first_name = etName.textToString(),
                        last_name = etSurname.textToString(),
                        middle_name = etPatronymic.textToString(),
                        pasport_serial = etPassportSeries.textToString(),
                        pasport_number = etPassportNumber.textToString(),
                        pasport_photo = File(mPassportImageUri?.path!!),
                        latter = File(mLetterImageUri?.path!!),
                        phone1 = etPhone1.textToString().getOnlyDigits(),
                        phone2 = etPhone2.textToString().getOnlyDigits()
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
                    toast("No internet connection")
                }
            }
        })
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

    fun validate(): Boolean {
        return when {
            bind.etName.checkIsEmpty() -> {
                bind.etName.showError(getString(R.string.required))
                false
            }
            bind.etSurname.checkIsEmpty() -> {
                bind.etSurname.showError(getString(R.string.required))
                false
            }
            bind.etPatronymic.checkIsEmpty() -> {
                bind.etPatronymic.showError(getString(R.string.required))
                false
            }
            bind.etPassportSeries.checkIsEmpty() -> {
                bind.etPassportSeries.showError(getString(R.string.required))
                false
            }
            bind.etPassportNumber.checkIsEmpty() -> {
                bind.etPassportNumber.showError(getString(R.string.required))
                false
            }
            mPassportImageUri==null ->{
                toast("Passport image required")
                false
            }
            mLetterImageUri==null ->{
                toast("Letter image required")
                false
            }
            bind.etPhone1.checkIsEmpty() -> {
                bind.etPhone1.showError(getString(R.string.required))
                false
            }
            bind.etPhone2.checkIsEmpty() -> {
                bind.etPhone2.showError(getString(R.string.required))
                false
            }
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