package `fun`.gladkikh.fastpallet5.di

import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.repository.DocumetRepository
import `fun`.gladkikh.fastpallet5.ui.fragment.documents.DocumentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object DependeciesModule {

    val appModule = module {

        // a single instance for Repository class
        single { CreatePalletRepository }
        single { DocumetRepository }

        // using factory we Koin will generate
        // a new instance for every call to the giving component
        viewModel { DocumentsViewModel( get()) }
    }
}