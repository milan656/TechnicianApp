package com.walkins.aapkedoorstep.viewmodel.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.walkins.aapkedoorstep.repository.CommonRepo
import com.walkins.aapkedoorstep.repository.LoginRepository

class CommonViewModelFactory(private val commonRepo: CommonRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CommonViewModel(commonRepo) as T
}