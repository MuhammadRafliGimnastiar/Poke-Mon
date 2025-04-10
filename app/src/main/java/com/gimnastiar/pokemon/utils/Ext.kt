package com.gimnastiar.pokemon.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

fun View.setVisibleOrGone(visible: Boolean) {
    visibility = if (visible) VISIBLE else GONE
}