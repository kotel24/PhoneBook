package com.example.mvidecomposetest.presentation

import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultEditContactComponent (
    private val contact: Contact
) : EditContactComponent {

    val repository = RepositoryImpl
    val editContactUseCase = EditContactUseCase(repository)

    val _model = MutableStateFlow(
        Model(username = contact.username, phone = contact.username )
    )

    override val model: StateFlow<Model>
        get() = _model

    override fun onUsernameEdit(username: String) {
        _model.value = model.value.copy(username = username)
    }

    override fun onPhoneEdit(phone: String) {
        _model.value = model.value.copy(phone = phone)
    }

    override fun onSaveEdit() {
        editContactUseCase(contact.copy(username = model.value.username, phone = model.value.phone))
    }
}