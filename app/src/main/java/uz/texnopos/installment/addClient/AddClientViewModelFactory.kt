package uz.texnopos.installment.addClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.texnopos.installment.repository.Repository

class AddClientViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddClientViewModel(repository) as T
    }
}