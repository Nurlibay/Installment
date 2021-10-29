package uz.texnopos.installment.data.model

import java.io.File

data class PostClient(
    val first_name: String,
    val last_name: String,
    val latter: File,
    val middle_name: String,
    val pasport_number: String,
    val pasport_photo: File,
    val pasport_serial: String,
    val phone1: String,
    val phone2: String,
)