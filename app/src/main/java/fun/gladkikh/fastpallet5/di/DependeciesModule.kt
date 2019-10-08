package `fun`.gladkikh.fastpallet5.di

import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.repository.DocumentRepository
import `fun`.gladkikh.fastpallet5.ui.fragment.documents.DocumentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object DependeciesModule {

    val appModule = module {
        single { CreatePalletRepository }
        single { DocumentRepository }
        viewModel { DocumentsViewModel(get()) }
    }


}