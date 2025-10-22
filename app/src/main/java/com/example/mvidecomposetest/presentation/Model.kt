package com.example.mvidecomposetest.presentation

import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Model(
    val username: String,
    val phone: String
): Parcelable
