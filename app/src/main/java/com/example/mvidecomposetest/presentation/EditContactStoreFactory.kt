package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

class EditContactStoreFactory (
    private val storeFactory: StoreFactory
){
    private val store: Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> =
        storeFactory.create(
            name = "EditContactStoreFactory",
            initialState = EditContactStore.State(username = "", phone = ""),
            reducer = ReducerImp,
            executorFactory = {ExecutorImpl()}
        )
    private sealed interface Action

    private sealed interface Message{
        data class ChangeUsername(val username: String): Message

        data class ChangePhone(val phone: String): Message
    }

    private inner class ExecutorImpl: CoroutineExecutor<EditContactStore.Intent, Action, EditContactStore.State, Message, EditContactStore.Label>(){
        override fun executeIntent(
            intent: EditContactStore.Intent,
            getState: () -> EditContactStore.State
        ) {
            val state = getState
            when(intent){
                is EditContactStore.Intent.ChangePhone -> {
                    dispatch(Message.ChangePhone(phone = intent.phone))
                }
                is EditContactStore.Intent.ChangeUsername -> {
                    dispatch(Message.ChangeUsername(username = intent.username))
                }
                EditContactStore.Intent.SaveContact -> {
                    publish(EditContactStore.Label.ContactSaved)
                }
            }
        }
    }

    private object ReducerImp: Reducer<EditContactStore.State, Message>{
        override fun EditContactStore.State.reduce(msg: Message): EditContactStore.State = when(msg){
            is Message.ChangePhone -> copy(phone = msg.phone)
            is Message.ChangeUsername -> copy(username = msg.username)
        }

    }
}