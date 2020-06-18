package com.blazebooks

import android.app.Application
import com.blazebooks.data.db.AppDatabase
import com.blazebooks.data.firebase.FirebaseSource
import com.blazebooks.data.firebase.FirestoreDataBase
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.StoredBooksRepository
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.UsersRepository
import com.blazebooks.ui.auth.AuthViewModelFactory
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdViewModelFactory
import com.blazebooks.ui.customdialogs.profileimage.ProfileImageViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@App))

        //databases
        bind() from singleton { FirebaseSource() }
        bind() from singleton { FirestoreDataBase() }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }

        //repositories
        bind() from singleton { LoginRepository(instance()) }
        bind() from singleton { UsersRepository(instance()) }
        bind() from singleton { StoredBooksRepository(instance()) }

        //factories
        bind() from singleton { AuthViewModelFactory(instance()) }
        bind() from singleton { ForgotPasswdViewModelFactory(instance()) }
        bind() from singleton { ProfileImageViewModelFactory(instance()) }


        //bind() from singleton { MainViewModelFactory(instance(), instance()) }
    }
}