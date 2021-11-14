package uz.texnopos.installment.data.model

import java.io.File

data class PostClient(
    val firstName: String,
    val lastName: String,
    val letter: File,
    val middleName: String,
    val passportNumber: String,
    val passportPhoto: File,
    val passportSerial: String,
    val phone1: String,
    val phone2: String,
)