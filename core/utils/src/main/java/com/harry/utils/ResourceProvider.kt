package com.harry.utils

interface ResourceProvider {
    fun getString(resId: Int): String
}
