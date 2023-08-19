package com.devjeong.myapplication

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore

class MyApplication : Application() {
    val dataStore by preferencesDataStore(name = "user_prefs")
}
