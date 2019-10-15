package `fun`.gladkikh.fastpallet5

import `fun`.gladkikh.fastpallet5.db.AppDatabase
import `fun`.gladkikh.fastpallet5.di.DependencyModule
import android.app.Application
import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun appContext(): Context? = instance?.applicationContext
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "mydatabase")
            .allowMainThreadQueries()
            .build()

        startKoin {
            androidLogger()
            // Android context
            androidContext(this@App)
            // modules
            modules(DependencyModule.appModule)
        }
        println("App -> Constants.UID: ${Constants.UID}; Platform: Android; APP Version: ${Constants.APP_VERSION}; OS Version: ${Constants.OS_VERSION}")
    }
}