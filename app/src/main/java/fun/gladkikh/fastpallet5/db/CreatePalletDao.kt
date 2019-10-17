package `fun`.gladkikh.fastpallet5.db


import `fun`.gladkikh.fastpallet5.db.initity.BoxCreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.CreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.PalletCreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.ProductCreatePalletDb
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CreatePalletDao {


    //@Query("SELECT SUM(weight) as total FROM BoxCreatePalletDb")
    @Query("SELECT cast(SUM(weight) as DECIMAL(18,2)) as weight FROM BoxCreatePalletDb")
    fun getSumWeight(): Sum

    @Query("SELECT cast(SUM(weight) as DECIMAL(18,2)) as weight,SUM(countBox)  as count  FROM BoxCreatePalletDb  WHERE guidPallet = :guidPallet")
    fun getInfoPallet(guidPallet:String):Sum


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
    fun insertIgnore(boxCreatPalletDb: BoxCreatePalletDb): Long

    @Update
    fun update(boxCreatPalletDb: BoxCreatePalletDb)

    @Transaction
    fun insertOrUpdate(boxCreatPalletDb: BoxCreatePalletDb) {
        if (insertIgnore(boxCreatPalletDb) == -1L) {
            update(boxCreatPalletDb)
        }
    }

    @Transaction
    fun insertOrUpdateList(list:List<BoxCreatePalletDb>) {
        list.forEach {
            if (insertIgnore(it) == -1L) {
                update(it)
            }
        }
    }

    @Query("SELECT * FROM BoxCreatePalletDb")
    fun getBoxAll(): List<BoxCreatePalletDb>

    @Query("SELECT * FROM BoxCreatePalletDb WHERE guid = :guid")
    fun getBoxByGuidLd(guid: String): LiveData<BoxCreatePalletDb>

    @Query("SELECT * FROM BoxCreatePalletDb WHERE guid = :guid")
    fun getBoxByGuid(guid: String): BoxCreatePalletDb

    @Query("SELECT * FROM BoxCreatePalletDb WHERE guidPallet = :guidPallet")
    fun getListBoxByPalletLd(guidPallet: String): LiveData<List<BoxCreatePalletDb>>

    @Query("SELECT * FROM BoxCreatePalletDb WHERE guidPallet = :guidPallet")
    fun getListBoxByPallet(guidPallet: String): List<BoxCreatePalletDb>


    @Delete
    fun dellBox(boxCreatPalletDb: BoxCreatePalletDb)
    //endregion

}

data class Sum(val weight:Float? = null, val count:Int? = null)