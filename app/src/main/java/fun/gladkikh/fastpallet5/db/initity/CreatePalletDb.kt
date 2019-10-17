package `fun`.gladkikh.fastpallet5.db.initity

import androidx.room.*

@Entity
data class CreatePalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    val number: String?,
    val date: Long?,
    val status: Int?,
    @ColumnInfo(index = true) var guidServer: String?,
    var typeFromServer: String?,
    @ColumnInfo(index = true) var dataChanged: Long?,
    var isWasLoadedLastTime: Boolean?,
    var description: String?,
    var barcode: String?
)


//ToDo не вижу guid из 1С
@Entity(
    foreignKeys = [ForeignKey(
        entity = CreatePalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidDoc"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductCreatePalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) val guidDoc: String,
    var number: String?,
    var barcode: String?,

    @ColumnInfo(index = true) var guidProduct: String?,
    var nameProduct: String?,
    var codeProduct: String?,
    var ed: String?,

    var weightBarcode: String?,
    var weightStartProduct: Int?,
    var weightEndProduct: Int?,
    var weightCoffProduct: Float?,

    var edCoff: Float?,
    var count: Float?,
    var countBox: Int?,
    var countPallet: Int?,


    var dataChanged: Long?,
    var isWasLoadedLastTime: Boolean?
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProductCreatePalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidProduct"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class PalletCreatePalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) var guidProduct: String?,
    var number: String?,
    var barcode: String?,

    var dataChanged: Long?,
    var count: Float?,
    var countBox: Int?,

    var nameProduct: String?,
    var state: String?,
    var sclad: String?
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = PalletCreatePalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidPallet"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class BoxCreatePalletDb(
    @PrimaryKey @ColumnInfo(index = true) val guid: String,
    @ColumnInfo(index = true) var guidPallet: String,
    var barcode: String?,
    var weight: Float?,
    var countBox: Int?,
    var data: Long?
)
