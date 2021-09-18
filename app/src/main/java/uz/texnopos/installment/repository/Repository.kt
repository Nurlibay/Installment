package uz.texnopos.installment.repository

import org.koin.dsl.module
import retrofit2.Response
import uz.texnopos.installment.api.RetrofitInstance
import uz.texnopos.installment.model.ModelPhoneList

class Repository {

    val repositoryModule = module {
        factory { Repository() }
    }

    suspend fun getClientData(): Response<ModelPhoneList> {
        return RetrofitInstance.api.getPhoneData()
    }

}