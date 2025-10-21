package com.example.mvidecomposetest.presentation

import kotlinx.coroutines.flow.StateFlow

interface EditContactComponent {

    val model: StateFlow<Model>

    fun onUsernameEdit(username: String)

    fun onPhoneEdit(phone: String)

    fun onSaveEdit()
}