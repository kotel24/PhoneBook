package com.example.mvidecomposetest.presentation

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.mvidecomposetest.domain.Contact
import kotlinx.parcelize.Parcelize

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext{

    private val navigation = StackNavigation<Config>()

    val stack: Value<ChildStack<Config, ComponentContext>> = childStack(
        source = navigation,
        initialConfiguration = Config.ContactList,
        handleBackButton = true,
        childFactory = {configuration, componentContext -> child(componentContext, configuration)}
    )

    private fun child(
        componentContext: ComponentContext,
        config: Config
    ): ComponentContext {
        return when (config){
            is Config.AddContact -> {
                DefaultAddContactComponent(
                    componentContext = componentContext,
                    onContactSaved = {
                        navigation.pop()
                    }
                )
            }
            is Config.ContactList -> {
                DefaultContactListComponent(
                    componentContext = componentContext,
                    onEditingContactRequested = {navigation.push(Config.EditContact(contact = it))},
                    onAddContactRequested = { navigation.push(Config.AddContact)}
                )
            }
            is Config.EditContact -> {
                DefaultEditContactComponent(
                    componentContext = componentContext,
                    contact = config.contact,
                    onContactSaved = {
                        navigation.pop()
                    }
                )
            }
        }
    }

    sealed interface Config: Parcelable {
        @Parcelize
        object ContactList: Config
        @Parcelize
        object AddContact: Config
        @Parcelize
        data class EditContact(val contact: Contact): Config
    }
}