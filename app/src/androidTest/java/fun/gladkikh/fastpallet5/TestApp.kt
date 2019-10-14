package `fun`.gladkikh.fastpallet5

import `fun`.gladkikh.fastpallet5.di.DependeciesModule
import android.app.Application
import org.koin.core.context.startKoin


class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // modules
            modules(emptyList())
        }
    }

}