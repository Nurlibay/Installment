package uz.texnopos.installment.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import uz.texnopos.installment.data.model.client.Phone

@Parcelize
data class Client(
    @SerializedName("all_sum")
    val allSum: Double,
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("client_name")
    val clientName: String,
    val color: String,
    val count: Int,
    val latter: String,
    val paid: Double,
    @SerializedName("pasport_number")
    val passportNumber: String,
    @SerializedName("pasport_photo")
    val passportPhoto: String,
    @SerializedName("pasport_serial")
    val passportSerial: String,
    val phones: List<Phones>
): Parcelable