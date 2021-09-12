package uz.texnopos.installment.repository

import retrofit2.Response
import uz.texnopos.installment.api.RetrofitInstance
import uz.texnopos.installment.model.ModelPhoneList

class Repository {

    suspend fun getClientData(): Response<ModelPhoneList> {
        return RetrofitInstance.api.getPhoneData()
    }

}