package uz.texnopos.installment.api

import retrofit2.Response
import retrofit2.http.GET
import uz.texnopos.installment.model.ModelPhoneList

interface ApiInterface {


    @GET("")
    suspend fun getPhoneData(): Response<ModelPhoneList>

}