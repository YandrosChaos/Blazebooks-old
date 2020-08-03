package com.blazebooks

import android.app.Application
import com.blazebooks.data.db.AppDatabase
import com.blazebooks.data.firebase.FirebaseSource
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.*
import com.blazebooks.ui.auth.AuthViewModelFactory
import com.blazebooks.ui.becomepremium.BecomePremiumViewModelFactory
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdViewModelFactory
import com.blazebooks.ui.customdialogs.profileimage.ProfileImageViewModelFactory
import com.blazebooks.ui.main.MainViewModelFactory
import com.blazebooks.ui.reader.BookStyleViewModelFactory
import com.blazebooks.ui.reader.ReaderViewModelFactory
import com.blazebooks.ui.search.SearchActivityViewModelFactory
import com.blazebooks.ui.settings.sharedpreferences.SharedPrefController
import com.blazebooks.ui.showbook.ShowBookViewModelFactory
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
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }

        //repositories
        bind() from singleton { LoginRepository(instance()) }
        bind() from singleton { StoredBooksRepository(instance()) }
        bind() from singleton { AccountRepository(instance()) }
        bind() from singleton { PremiumRepository(instance()) }
        bind() from singleton { CatalogRepository(instance()) }

        //factories
        bind() from singleton { AuthViewModelFactory(instance(), instance()) }
        bind() from singleton { ForgotPasswdViewModelFactory(instance()) }
        bind() from singleton { ProfileImageViewModelFactory(instance()) }
        bind() from singleton { ReaderViewModelFactory(instance()) }
        bind() from singleton { MainViewModelFactory(instance(), instance()) }
        bind() from singleton { BecomePremiumViewModelFactory(instance()) }
        bind() from singleton { SearchActivityViewModelFactory(instance(), instance()) }
        bind() from singleton { BookStyleViewModelFactory(instance()) }
        bind() from singleton {
            ShowBookViewModelFactory(
                instance(),
                instance(),
                instance()
            )
        }

        //controllers
        bind() from singleton { SharedPrefController(instance(), instance()) }
    }
}