package com.mohsinajavaid.currencyapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class DataStoreManager(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {

        val PASSWORD = stringPreferencesKey("PASSWORD")
        val AUTH_GOOGLE = booleanPreferencesKey("AUTH_GOOGLE")
        val AUTH_EMAIL = booleanPreferencesKey("AUTH_EMAIL")

    }

    suspend fun saveToDataStore(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }


    suspend fun readFromDataStore(key: Preferences.Key<String>): String {
        val preferences = context.dataStore.data.first()
        return preferences[key] ?: ""
    }


}