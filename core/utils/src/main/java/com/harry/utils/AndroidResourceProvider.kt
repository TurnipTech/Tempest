package com.harry.utils

import android.content.Context

class AndroidResourceProvider(
    private val context: Context,
) : ResourceProvider {
    override fun getString(resId: Int): String = context.getString(resId)
}
