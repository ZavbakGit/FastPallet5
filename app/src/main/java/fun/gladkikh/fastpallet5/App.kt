package `fun`.gladkikh.fastpallet5

import `fun`.gladkikh.fastpallet5.db.AppDatabase
import `fun`.gladkikh.fastpallet5.di.DependencyModule
import `fun`.gladkikh.fastpallet5.repository.SettingsRepository
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
        var settingsRepository: SettingsRepository = SettingsRepository()
    }

    override fun onCreate() {
        super.onCreate()

        val s = "SELECT  SUM(BoxCountBOX) AS boxCount" +
                "       ,SUM(RoundedWieght) AS boxWieght" +
                "       ,SUM(BoxCountRows) AS boxRow" +
                "       ,SUM(PalCount) AS palCount" +
                "       ,ProdGuid AS prodguid" +
                "       ,ProdName AS prodName" +
                "FROM (" +
                "SELECT SUM(Box.countBox) AS BoxCountBOX" +
                "       ,SUM(Round(Box.weight * 1000) ) / 1000 AS RoundedWieght" +
                "       ,COUNT(Box.guid) AS BoxCountRows" +
                "       ,COUNT(DISTINCT Pal.guid) AS PalCount" +
                "       ,Prod.guid AS ProdGuid" +
                "       ,MAX(Prod.guidProduct) ProdProductGuid" +
                "       ,MAX(Prod.nameProduct) AS ProdName" +
                "FROM BoxCreatePalletDb Box" +
                "       LEFT JOIN" +
                "       PalletCreatePalletDb Pal ON Pal.guid = Box.guidPallet" +
                "       LEFT JOIN" +
                "       ProductCreatePalletDb Prod ON Prod.guid = Pal.guidProduct" +
                "WHERE Prod.guidDoc =:guidDoc" +
                "GROUP BY Prod.guid" +
                "UNION ALL" +
                "SELECT 0,0,0,0, Prod.guid " +
                "       ,Prod.guidProduct " +
                "       ,Prod.nameProduct" +
                "FROM ProductCreatePalletDb Prod WHERE Prod.guidDoc =:guidDoc)" +
                "GROUP BY ProdName" +
                "       ,ProdProductGuid" +
                "       ,ProdGuid" +
                "ORDER BY ProdName" +
                "       ,ProdProductGuid" +
                "       ,ProdGuid;"

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