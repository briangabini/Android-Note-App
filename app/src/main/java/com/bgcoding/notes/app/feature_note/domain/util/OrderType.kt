package com.bgcoding.notes.app.feature_note.domain.util

sealed class OrderType { // subclasses will be known at compile time
    object Ascending: OrderType()
    object Descending: OrderType()
}