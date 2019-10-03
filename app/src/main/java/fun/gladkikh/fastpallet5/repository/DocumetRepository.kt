package `fun`.gladkikh.fastpallet5.repository

import `fun`.gladkikh.fastpallet5.App
import `fun`.gladkikh.fastpallet5.domain.intety.Document
import `fun`.gladkikh.fastpallet5.maping.toCreatPallet
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.anit.fastpallet4.domain.intity.Type.CREATE_PALLET

object DocumetRepository {
    fun getDocumetListLiveData(): LiveData<List<Document>> = Transformations.map(
        App.database.getCreatPalletDao().getAllLd()
    ) {
        it.map { doc ->
           val createPallet =  doc.toCreatPallet()
            Document(
                guid = createPallet.guid,
                type = CREATE_PALLET.id,
                date = createPallet.date,
                description = createPallet.description,
                number = createPallet.number,
                status = createPallet.status,
                dataChange = createPallet.dataChanged
            )
        }
    }
}