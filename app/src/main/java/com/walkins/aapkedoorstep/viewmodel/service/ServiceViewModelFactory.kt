package com.walkins.aapkedoorstep.viewmodel.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.walkins.aapkedoorstep.repository.CommonRepo
import com.walkins.aapkedoorstep.repository.ServiceRepo
import com.walkins.aapkedoorstep.viewmodel.common.CommonViewModel

class ServiceViewModelFactory(private val serviceRepo: ServiceRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ServiceViewModel(serviceRepo) as T
}