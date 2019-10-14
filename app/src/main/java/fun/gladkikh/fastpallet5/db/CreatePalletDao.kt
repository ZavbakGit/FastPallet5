package `fun`.gladkikh.fastpallet5.db


import `fun`.gladkikh.fastpallet5.db.initity.BoxCreatPalletDb
import `fun`.gladkikh.fastpallet5.db.initity.CreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.PalletCreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.ProductCreatePalletDb
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CreatePalletDao {

    //region Doc
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(createPallet: CreatePalletDb): Long

    @Insert
    fun insert(createPallet: List<CreatePalletDb>)

    @Update
    fun update(createPallet: CreatePalletDb)


    @Transaction
    fun insertOrUpdate(createPallet: CreatePalletDb) {
        if (insertIgnore(createPallet) == -1L) {
            update(createPallet)
        }
    }


    @Query("DELETE FROM CreatePalletDb")
    fun dellCreatePalletALL()

    @Query("SELECT * FROM CreatePalletDb")
    fun getAllLd(): LiveData<List<CreatePalletDb>>

    @Query("SELECT * FROM CreatePalletDb WHERE guid = :guid")
    fun getDocByGuidLd(guid: String): LiveData<CreatePalletDb>

    @Query("SELECT * FROM CreatePalletDb WHERE guidServer = :guidServer")
    fun getDocByGuidServer(guidServer: String): CreatePalletDb?

    @Query("SELECT * FROM CreatePalletDb WHERE guid = :guid")
    fun getDocByGuid(guid: String): CreatePalletDb?

    @Delete
    fun dellDoc(createPallet: CreatePalletDb)
    //endregion



    //region Product
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(productCreatePalletDb: ProductCreatePalletDb): Long

    @Update
    fun update(productCreatePalletDb: ProductCreatePalletDb)

    @Transaction
    fun insertOrUpdate(productCreatePalletDb: ProductCreatePalletDb) {
        if (insertIgnore(productCreatePalletDb) == -1L) {
            update(productCreatePalletDb)
        }
    }


    @Query("SELECT * FROM ProductCreatePalletDb WHERE guidDoc = :guidDoc")
    fun getProductByDocLd(guidDoc: String): LiveData<List<ProductCreatePalletDb>>

    @Query("SELECT * FROM ProductCreatePalletDb WHERE guidDoc = :guidDoc")
    fun getProductByDoc(guidDoc: String): List<ProductCreatePalletDb>


    @Query("SELECT * FROM ProductCreatePalletDb WHERE guid = :guid")
    fun getProductByGuidLd(guid: String): LiveData<ProductCreatePalletDb>

    @Query("SELECT * FROM ProductCreatePalletDb WHERE guid = :guid")
    fun getProductByGuid(guid: String): ProductCreatePalletDb

    @Delete
    fun dellProduct(productCreatePalletDb: ProductCreatePalletDb)
    //endregion



    //region PalletServer
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(palletCreatePalletDb: PalletCreatePalletDb): Long

    @Update
    fun update(palletCreatePalletDb: PalletCreatePalletDb)

    @Transaction
    fun insertOrUpdate(palletCreatePalletDb: PalletCreatePalletDb) {
        if (insertIgnore(palletCreatePalletDb) == -1L) {
            update(palletCreatePalletDb)
        }
    }


    @Delete
    fun dellPallet(palletCreatePalletDb: PalletCreatePalletDb)


    @Query("SELECT * FROM PalletCreatePalletDb")
    fun getPalletAll(): List<PalletCreatePalletDb>

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guid = :guid")
    fun getPalletByGuidLd(guid: String): LiveData<PalletCreatePalletDb>

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guid = :guid")
    fun getPalletByGuid(guid: String): PalletCreatePalletDb

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guidProduct = :guidProduct")
    fun getListPalletByProductLd(guidProduct: String): LiveData<List<PalletCreatePalletDb>>

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guidProduct = :guidProduct")
    fun getListPalletByProduct(guidProduct: String): List<PalletCreatePalletDb>
    //endregion



    //region BoxServer
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(boxCreatPalletDb: BoxCreatPalletDb): Long

    @Update
    fun update(boxCreatPalletDb: BoxCreatPalletDb)

    @Transaction
    fun insertOrUpdate(boxCreatPalletDb: BoxCreatPalletDb) {
        if (insertIgnore(boxCreatPalletDb) == -1L) {
            update(boxCreatPalletDb)
        }
    }


    @Query("SELECT * FROM BoxCreatPalletDb")
    fun getBoxAll(): List<BoxCreatPalletDb>

    @Query("SELECT * FROM BoxCreatPalletDb WHERE guid = :guid")
    fun getBoxByGuidLd(guid: String): LiveData<BoxCreatPalletDb>

    @Query("SELECT * FROM BoxCreatPalletDb WHERE guid = :guid")
    fun getBoxByGuid(guid: String): BoxCreatPalletDb

    @Query("SELECT * FROM BoxCreatPalletDb WHERE guidPallet = :guidPallet")
    fun getListBoxByPalletLd(guidPallet: String): LiveData<List<BoxCreatPalletDb>>

    @Query("SELECT * FROM BoxCreatPalletDb WHERE guidPallet = :guidPallet")
    fun getListBoxByPallet(guidPallet: String): List<BoxCreatPalletDb>


    @Delete
    fun dellBox(boxCreatPalletDb: BoxCreatPalletDb)
    //endregion

}