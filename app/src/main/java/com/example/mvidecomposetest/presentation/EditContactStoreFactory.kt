package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.presentation.EditContactStore.Intent

class EditContactStoreFactory (
    private val storeFactory: StoreFactory
){
    private val store: Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> =
        storeFactory.create(
            name = "EditContactStoreFactory",
            initialState = EditContactStore.State(username = "", phone = ""),

        )
    private sealed interface Action

    private sealed interface Message{
        data class ChangeUsername(val username: String): Message

        data class ChangePhone(val phone: String): Message
    }

    private object ReducerImp: Reducer<EditContactStore.State, Message>{
        override fun EditContactStore.State.reduce(msg: Message): EditContactStore.State = when(msg){
            is Message.ChangePhone -> copy(phone = msg.phone)
            is Message.ChangeUsername -> copy(username = msg.username)
        }

    }
}