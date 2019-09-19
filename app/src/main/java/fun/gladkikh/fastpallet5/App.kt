package `fun`.gladkikh.fastpallet5

import `fun`.gladkikh.fastpallet5.db.AppDatabase
import android.app.Application
import android.content.Context
import androidx.room.Room

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
        println("App -> Constants.UID: ${Constants.UID}; Platform: Android; APP Version: ${Constants.APP_VERSION}; OS Version: ${Constants.OS_VERSION}")
    }
}