package uz.texnopos.installment.core.imagehelper

import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File

fun ImageView.setDrawableImage(@DrawableRes resource: Int, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(resource)
    if (applyCircle) {
        glide.apply(RequestOptions.circleCropTransform()).into(this)
    } else {
        glide.into(this)
    }
}

fun ImageView.setLocalImage(uri: Uri, applyCircle: Boolean = false) {
    val glide = Glide.with(this).load(uri)
    if (applyCircle) {
        glide.apply(RequestOptions.circleCropTransform()).into(this)
    } else {
        glide.into(this)
    }
}

fun Fragment.pickCameraImage(code: Int) {
    ImagePicker.with(this)
        .cameraOnly()
        .saveDir(requireActivity().getExternalFilesDir(null)!!)
        .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
        .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
        .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
        .saveDir(File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!,
            "ImagePicker"))
        .saveDir(requireActivity().getExternalFilesDir("ImagePicker")!!)
        .saveDir(File(requireActivity().externalCacheDir, "ImagePicker"))
        .saveDir(File(requireActivity().cacheDir, "ImagePicker"))
        .saveDir(File(requireActivity().filesDir, "ImagePicker"))
        .start(code)
}

fun Fragment.pickGalleryImage(code: Int) {
    ImagePicker.with(this)
        .crop()
        .galleryOnly()
        .galleryMimeTypes(
            mimeTypes = arrayOf(
                "image/png",
                "image/jpg",
                "image/jpeg"
            )
        )
        .maxResultSize(1080, 1920)
        .start(code)
}
