package com.harry.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

interface Storage {
    suspend fun <T> put(serializer: KSerializer<T>, key: String, value: T)
    
    fun <T> get(serializer: KSerializer<T>, key: String, defaultValue: T): Flow<T>
    
    fun <T> getNullable(serializer: KSerializer<T>, key: String): Flow<T?>
    
    suspend fun remove(key: String)
    
    suspend fun clear()
    
    fun contains(key: String): Flow<Boolean>
}

suspend inline fun <reified T> Storage.put(key: String, value: T) = 
    put(serializer(), key, value)

inline fun <reified T> Storage.get(key: String, defaultValue: T): Flow<T> = 
    get(serializer(), key, defaultValue)

inline fun <reified T> Storage.getNullable(key: String): Flow<T?> = 
    getNullable(serializer(), key)