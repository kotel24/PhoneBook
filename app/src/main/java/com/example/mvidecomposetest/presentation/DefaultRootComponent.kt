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

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.ContactList,
        handleBackButton = true,
        childFactory = {configuration, componentContext -> child(componentContext, configuration)}
    )

    private fun child(
        componentContext: ComponentContext,
        config: Config
    ): RootComponent.Child {
        return when (config){
            is Config.AddContact -> {
                val component = DefaultAddContactComponent(
                    componentContext = componentContext,
                    onContactSaved = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.AddContact(component)
            }
            is Config.ContactList -> {
                val component = DefaultContactListComponent(
                    componentContext = componentContext,
                    onEditingContactRequested = {navigation.push(Config.EditContact(contact = it))},
                    onAddContactRequested = { navigation.push(Config.AddContact)}
                )
                RootComponent.Child.ContactList(component)
            }
            is Config.EditContact -> {
                val component = DefaultEditContactComponent(
                    componentContext = componentContext,
                    contact = config.contact,
                    onContactSaved = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.EditContact(component)
            }
        }
    }

    private sealed interface Config: Parcelable {
        @Parcelize
        object ContactList: Config
        @Parcelize
        object AddContact: Config
        @Parcelize
        data class EditContact(val contact: Contact): Config
    }
}