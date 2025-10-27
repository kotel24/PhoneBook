package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import kotlinx.coroutines.launch

class ContactListStoreFactory(
    private val storeFactory: StoreFactory,
    private val getContactsUseCase: GetContactsUseCase
) {
    fun create(): ContactListStore = object : ContactListStore,
        Store<ContactListStore.Intent, ContactListStore.State, ContactListStore.Label> by
        storeFactory.create(
            name = "ContactListStoreFactory",
            initialState = ContactListStore.State(listOf()),
            reducer = ReducerImpl,
            bootstrapper = BootstrapperImpl(),
            executorFactory = {ExecutorImpl()}
        ){}

    private sealed interface Action{
        data class ContactsLoaded(val contacts: List<Contact>): Action
    }

    private sealed interface Message{
        data class ContactsLoaded(val contacts: List<Contact>): Message
    }

    private object ReducerImpl: Reducer<ContactListStore.State, Message>{
        override fun ContactListStore.State.reduce(msg: Message): ContactListStore.State = when(msg){
            is Message.ContactsLoaded -> {
                copy(contactList = msg.contacts)
            }
        }
    }

    private inner class BootstrapperImpl: CoroutineBootstrapper<Action>(){
        override fun invoke() {
            scope.launch {
                getContactsUseCase().collect {
                    dispatch(Action.ContactsLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl: CoroutineExecutor<ContactListStore.Intent, Action, ContactListStore.State, Message, ContactListStore.Label>(){
        override fun executeIntent(
            intent: ContactListStore.Intent,
            getState: () -> ContactListStore.State
        ) {
            when(intent){
                ContactListStore.Intent.AddContact -> {
                    publish(ContactListStore.Label.AddContact)
                }
                is ContactListStore.Intent.SelectContact -> {
                    publish(ContactListStore.Label.EditContact(intent.contact))
                }
            }
        }

        override fun executeAction(
            action: Action,
            getState: () -> ContactListStore.State
        ) {
            when(action){
                is Action.ContactsLoaded -> {
                    dispatch(Message.ContactsLoaded(action.contacts))
                }
            }
        }
    }
}