package `fun`.gladkikh.fastpallet5.db


import `fun`.gladkikh.fastpallet5.db.initity.BoxCreatPalletDb
import `fun`.gladkikh.fastpallet5.db.initity.CreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.PalletCreatePalletDb
import `fun`.gladkikh.fastpallet5.db.initity.ProductCreatePalletDb
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CreatePalletDao {

    //CreatePallet

    @Insert
    fun insert(createPallet: CreatePalletDb)

    @Insert
    fun insert(createPallet: List<CreatePalletDb>)

    @Update
    fun update(createPallet: CreatePalletDb)

    @Query("DELETE FROM CreatePalletDb")
    fun dellCreatePalletALL()

    @Query("SELECT * FROM CreatePalletDb")
    fun getAllLd(): LiveData<List<CreatePalletDb>>

    @Query("SELECT * FROM CreatePalletDb WHERE guid = :guid")
    fun getDocByGuidLd(guid:String): LiveData<CreatePalletDb>

    @Query("SELECT * FROM CreatePalletDb WHERE guid = :guid")
    fun getDocByGuid(guid:String): CreatePalletDb


    //Product

    @Insert
    fun insert(productCreatePalletDb: ProductCreatePalletDb)

    @Insert
    fun insertProdList(listProductCreatePalletDb: List<ProductCreatePalletDb>)

    @Update
    fun update(productCreatePalletDb: ProductCreatePalletDb)

    @Query("SELECT * FROM ProductCreatePalletDb WHERE guidDoc = :guidDoc")
    fun getProductByDocLd(guidDoc:String):LiveData<List<ProductCreatePalletDb>>

    @Query("SELECT * FROM ProductCreatePalletDb WHERE guid = :guid")
    fun getProductByGuidLd(guid:String): LiveData<ProductCreatePalletDb>

    @Query("SELECT * FROM ProductCreatePalletDb WHERE guid = :guid")
    fun getProductByGuid(guid:String): ProductCreatePalletDb


    //Pallet

    @Insert
    fun insert(palletCreatePalletDb: PalletCreatePalletDb)

    @Delete
    fun dellPallet(palletCreatePalletDb: PalletCreatePalletDb)

    @Insert
    fun insertPalletList(listPalletCreatePalletDb: List<PalletCreatePalletDb>)

    @Update
    fun update(palletCreatePalletDb: PalletCreatePalletDb)

    @Query("SELECT * FROM PalletCreatePalletDb")
    fun getPalletAllLd(): LiveData<List<PalletCreatePalletDb>>

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guid = :guid")
    fun getPalletByGuidLd(guid:String): LiveData<PalletCreatePalletDb>

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guid = :guid")
    fun getPalletByGuid(guid:String): PalletCreatePalletDb

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guidProduct = :guidProduct")
    fun getListPalletByProductLd(guidProduct:String):LiveData<List<PalletCreatePalletDb>>

    @Query("SELECT * FROM PalletCreatePalletDb WHERE guidProduct = :guidProduct")
    fun getListPalletByProduct(guidProduct:String):List<PalletCreatePalletDb>

    //Box
    @Insert
    fun insert(boxCreatPalletDb: BoxCreatPalletDb)

    @Insert
    fun insertBoxList(boxCreatPalletDb: List<BoxCreatPalletDb>)

    @Update
    fun update(boxCreatPalletDb: BoxCreatPalletDb)

    @Query("SELECT * FROM BoxCreatPalletDb")
    fun getBoxAllLd(): LiveData<List<BoxCreatPalletDb>>

    @Query("SELECT * FROM BoxCreatPalletDb WHERE guid = :guid")
    fun getBoxByGuidLd(guid:String): LiveData<BoxCreatPalletDb>

    @Query("SELECT * FROM BoxCreatPalletDb WHERE guid = :guid")
    fun getBoxByGuid(guid:String): BoxCreatPalletDb

    @Query("SELECT * FROM BoxCreatPalletDb WHERE guidPallet = :guidPallet")
    fun getListBoxByPalletLd(guidPallet:String):LiveData<List<BoxCreatPalletDb>>

}