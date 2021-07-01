package com.walkins.aapkedoorstep.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.walkins.aapkedoorstep.repository.LoginRepository

class LoginViewModelFactory(private val postRepository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        LoginActivityViewModel(postRepository) as T
}