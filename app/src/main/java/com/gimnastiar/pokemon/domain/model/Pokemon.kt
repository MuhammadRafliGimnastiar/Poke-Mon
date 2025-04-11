package com.gimnastiar.pokemon.domain.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val name: String,
    val imageUrl: String,
    val abilities: List<String?>? = null,
    val weight: Int,
    val height: Int,
): Parcelable

