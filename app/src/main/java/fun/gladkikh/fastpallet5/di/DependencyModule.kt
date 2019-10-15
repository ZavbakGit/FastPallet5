package `fun`.gladkikh.fastpallet5.di

import `fun`.gladkikh.fastpallet5.db.AppDatabase
import `fun`.gladkikh.fastpallet5.domain.intety.SettingsPref
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.repository.DocumentRepository
import `fun`.gladkikh.fastpallet5.repository.SettingsRepository
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box.BoxCreatePalletViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct.DialogProductCreatePalletViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc.CreatePalletViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet.PalletCreatePalletViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product.ProductCreatePalletViewModel
import `fun`.gladkikh.fastpallet5.ui.fragment.documents.DocumentsViewModel
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


object DependencyModule {

    val appModule = module {
        single { getDataBase(androidContext()).getCreatPalletDao() }
        single { CreatePalletRepository(get()) }
        single { SettingsRepository() }
        single { DocumentRepository(get()) }
        scope(named("scope_main_activity")){
            scoped { getSettings(get()) }
        }
        viewModel { DocumentsViewModel(get(), get(),get()) }
        viewModel { CreatePalletViewModel(get()) }
        viewModel { ProductCreatePalletViewModel(get()) }
        viewModel { PalletCreatePalletViewModel(get()) }
        viewModel { DialogProductCreatePalletViewModel(get()) }
        viewModel { BoxCreatePalletViewModel(get()) }
    }


   private fun getDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "mydatabase")
            .allowMainThreadQueries()
            .build()
    }


   private fun getSettings(context: Context): SettingsPref {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

        val host = "preference_host"
        val login = "preference_login"
        val pass = "preference_pass"
        val code = "preference_code_1c"
        val listTsd = "list_tsd"

        return SettingsPref(
            host = sharedPref.getString(host, null),
            code = sharedPref.getString(code, null),
            login = sharedPref.getString(login, null),
            pass = sharedPref.getString(pass, null),
            typeTsd = sharedPref.getString(listTsd, 1.toString()).toIntOrNull()
        )
    }


}