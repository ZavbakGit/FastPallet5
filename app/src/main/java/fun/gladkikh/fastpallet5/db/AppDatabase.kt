package `fun`.gladkikh.fastpallet5.db


import `fun`.gladkikh.fastpallet5.db.initity.BoxCreatPalletDb
import `fun`.gladkikh.fastpallet5.db.initity.CreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.PalletCreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.ProductCreatePalletDb
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CreatePalletDb::class,
        ProductCreatePalletDb::class,
        PalletCreatePalletDb::class,
        BoxCreatPalletDb::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCreatPalletDao(): CreatePalletDao
}