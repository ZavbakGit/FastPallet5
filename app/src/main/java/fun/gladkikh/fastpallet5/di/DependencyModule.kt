package `fun`.gladkikh.fastpallet5.di

import `fun`.gladkikh.fastpallet5.db.AppDatabase
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
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object DependencyModule {

    val appModule = module {
        single { getDataBase(androidContext()).getCreatPalletDao() }
        single { CreatePalletRepository(get()) }
        single { SettingsRepository() }
        single { DocumentRepository(get()) }
//        scope(named("scope_main_activity")){
//            scoped { getSettingsFromPref(get()) }
//        }
        viewModel { DocumentsViewModel(get(), get()) }
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

}