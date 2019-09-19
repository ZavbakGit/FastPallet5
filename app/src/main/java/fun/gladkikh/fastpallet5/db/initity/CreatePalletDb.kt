package `fun`.gladkikh.fastpallet5.db.initity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class CreatePalletDb(
    @PrimaryKey val guid: String,
    val number: String?,
    val date: Long?,
    val status: Int?,
    var guidServer: String?,
    var typeFromServer: String?,
    var dataChanged: Long?,
    var isWasLoadedLastTime: Boolean?,
    var description: String?,
    var barcode: String?
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = CreatePalletDb::class,
        parentColumns = arrayOf("guid"),
        childColumns = arrayOf("guidDoc"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ProductCreatePalletDb(
    @PrimaryKey val guid: String,
    val guidDoc: String,
    var number: String?,
    var barcode: String?,

    var guidProduct: String?,
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
    @PrimaryKey val guid: String,
    var guidProduct: String?,
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
data class BoxCreatPalletDb(
    @PrimaryKey val guid: String,
    var guidPallet:String,
    var barcode: String?,
    var weight: Float?,
    var countBox: Int?,
    var data: Long?
)
