package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.mvidecomposetest.presentation.AddContactStore.Intent

class AddContactStoreFactory (
    private val storeFactory: StoreFactory
) {
    private val store: Store<AddContactStore.Intent, AddContactStore.State, AddContactStore.Label> =
        storeFactory.create(
            name = "AddContactStoreFactory",
            initialState = AddContactStore.State(username = "", phone = ""),

        )

    private sealed interface Action

    private sealed interface Message {

        data class ChangeUsername(val username: String): Message

        data class ChangePhone(val phone: String): Message
    }

    private object ReducerImpl: Reducer<AddContactStore.State, Message>{
        override fun AddContactStore.State.reduce(msg: Message): AddContactStore.State = when(msg){
            is Message.ChangePhone -> copy(phone = msg.phone)
            is Message.ChangeUsername -> copy(username = msg.username)
        }

    }
}