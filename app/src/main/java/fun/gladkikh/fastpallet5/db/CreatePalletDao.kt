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
    fun getInfoPallet(guidPallet: String): Sum


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
    fun insertOrUpdateListPallet(list: List<PalletCreatePalletDb>) {
        list.forEach {
            if (insertIgnore(it) == -1L) {
                update(it)
            }
        }
    }

    @Transaction
    fun insertOrUpdateList(list: List<BoxCreatePalletDb>) {
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


    @Query(
        "SELECT Sum(Box.countBox) AS boxCountBox," +
                "       Sum(Round(Box.weight * 1000) ) / 1000 AS boxWeight," +
                "       (" +
                "           SELECT Sum(Round(BPT.weight * 1000) ) / 1000" +
                "             FROM BoxCreatePalletDb BPT" +
                "            WHERE BPT.guidPallet = Box.guidPallet" +
                "       )" +
                "       AS palTotalWeight," +
                "       (" +
                "           SELECT Count( * ) " +
                "             FROM BoxCreatePalletDb BPT" +
                "            WHERE BPT.guidPallet = Box.guidPallet" +
                "       )" +
                "       AS palTotalRowsCount," +
                "       (" +
                "           SELECT Sum(BPT.countBox) " +
                "             FROM BoxCreatePalletDb BPT" +
                "            WHERE BPT.guidPallet = Box.guidPallet" +
                "       )" +
                "       AS palTotalCountBox," +
                "       pal.guid AS palGuid," +
                "       MAX(Pal.number) AS palNumber," +
                "       Doc.guid AS docGuid," +
                "       Doc.guidServer AS docGuidServer," +
                "       Doc.number AS docNumber," +
                "       Doc.description As docDescription," +
                "       Prod.nameProduct As prodName," +
                "       Prod.weightStartProduct As prodStart," +
                "       Prod.weightEndProduct As prodEnd," +
                "       Prod.weightCoffProduct As prodCoeff," +
                "       Box.barcode As boxBarcod," +
                "       Box.guid As  boxGuid," +
                "       Box.data As boxDate" +
                "  FROM BoxCreatePalletDb Box" +
                "       LEFT JOIN" +
                "       PalletCreatePalletDb Pal ON Pal.guid = Box.guidPallet" +
                "       LEFT JOIN" +
                "       ProductCreatePalletDb Prod ON Prod.guid = Pal.guidProduct" +
                "       LEFT JOIN" +
                "       CreatePalletDb Doc ON Doc.guid = Prod.guidDoc" +
                " WHERE Box.guid = :guidBox" +
                " GROUP BY Pal.guid," +
                "          Prod.guid," +
                "          Doc.guid;"
    )
    fun getDataForBoxScreen(guidBox: String): LiveData<DataForBoxScreen>


    @Query("SELECT  SUM(BoxCountBOX) AS boxCount " +
            "       ,SUM(RoundedWieght) AS boxWeight " +
            "       ,SUM(BoxCountRows) AS boxRow " +
            "       ,SUM(PalCount) AS palCount " +
            "       ,ProdGuid AS prodGuid " +
            "       ,ProdName AS prodName " +
            "FROM ( " +
            "SELECT SUM(Box.countBox) AS BoxCountBOX " +
            "       ,SUM(Round(Box.weight * 1000) ) / 1000 AS RoundedWieght " +
            "       ,COUNT(Box.guid) AS BoxCountRows " +
            "       ,COUNT(DISTINCT Pal.guid) AS PalCount " +
            "       ,Prod.guid AS ProdGuid " +
            "       ,MAX(Prod.guidProduct) ProdProductGuid " +
            "       ,MAX(Prod.nameProduct) AS ProdName " +
            "FROM BoxCreatePalletDb Box " +
            "       LEFT JOIN " +
            "       PalletCreatePalletDb Pal ON Pal.guid = Box.guidPallet " +
            "       LEFT JOIN " +
            "       ProductCreatePalletDb Prod ON Prod.guid = Pal.guidProduct " +
            "WHERE Prod.guidDoc =:guidDoc  " +
            "GROUP BY Prod.guid " +
            "UNION ALL " +
            "SELECT 0,0,0,0, Prod.guid  " +
            "       ,Prod.guidProduct  " +
            "       ,Prod.nameProduct " +
            "FROM ProductCreatePalletDb Prod WHERE Prod.guidDoc =:guidDoc) " +
            "GROUP BY ProdName " +
            "       ,ProdProductGuid " +
            "       ,ProdGuid " +
            "ORDER BY ProdName " +
            "       ,ProdProductGuid " +
            "       ,ProdGuid"
            )
    fun getDataForDocument(guidDoc: String): List<DataForDocumentItem>

}


data class DataForDocumentItem(
    val prodGuid: String?,
    val prodName: String?,
    val boxCount: String?,
    val boxWeight: Float?,
    val boxRow: Int?,
    val palCount:Int?
)

data class DataForBoxScreen(
    val docNumber: String?,
    val prodName: String?,
    val prodStart: Int?,
    val prodEnd: Int?,
    val prodCoeff: Float?,

    val palNumber: String?,
    val palTotalWeight: Float?,
    val palTotalRowsCount: Int?,
    val palTotalCountBox: Int?,
    val palGuid: String?,

    val boxGuid: String?,
    val boxCountBox: Int?,
    val boxWeight: Float?,
    val boxBarcode: String?,
    val boxDate: Long?

)

data class Sum(val weight: Float? = null, val count: Int? = null)