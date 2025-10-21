package com.example.mvidecomposetest.presentation

import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.AddContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultAddContactComponent : AddContactComponent {

    private val repository = RepositoryImpl
    private val addContactUseCase = AddContactUseCase(repository)

    private val _model = MutableStateFlow(Model(username = "", phone = ""))

    override val model: StateFlow<Model>
        get() = _model.asStateFlow()

    override fun onUsernameChanged(username: String) {
        _model.value = model.value.copy(username = username)
    }

    override fun onPhoneChanged(phone: String) {
        _model.value = model.value.copy(phone = phone)
    }

    override fun onSaveContactClicked() {
        addContactUseCase(_model.value.username, _model.value.phone)
    }
}