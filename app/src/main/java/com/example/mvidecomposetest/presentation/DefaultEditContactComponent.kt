package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultEditContactComponent (
    private val contact: Contact,
    componentContext: ComponentContext
) : EditContactComponent, ComponentContext by componentContext {

    private val repository = RepositoryImpl
    private val editContactUseCase = EditContactUseCase(repository)

    init {
        stateKeeper.register(KEY){
            model.value
        }
    }

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY)?:Model(username = contact.username, phone = contact.username )
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
    companion object{
        const val KEY = "DefaultEditContactComponent"
    }
}