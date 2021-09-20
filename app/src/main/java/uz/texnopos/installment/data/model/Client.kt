package uz.texnopos.installment.data.model

data class Client(
    val created_at: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val latter: String,
    val middle_name: String,
    val pasport_number: String,
    val pasport_photo: String,
    val pasport_serial: String,
    val phone1: String,
    val phone2: String,
    val updated_at: String
)