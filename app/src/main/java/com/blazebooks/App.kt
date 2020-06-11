package com.blazebooks

import android.app.Application
import com.blazebooks.data.db.AppDatabase
import com.blazebooks.data.firebase.FirebaseSource
import com.blazebooks.data.network.NetworkConnectionInterceptor
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.ui.auth.login.LoginViewModelFactory
import com.blazebooks.ui.auth.signup.SignupViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@App))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { FirebaseSource() }
        bind() from singleton { LoginRepository(instance()) }
        bind() from singleton { LoginViewModelFactory(instance()) }
        bind() from singleton { SignupViewModelFactory(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { StoredBooksRepository(instance()) }
    }
}