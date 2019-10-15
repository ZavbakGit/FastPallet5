package `fun`.gladkikh.fastpallet5

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