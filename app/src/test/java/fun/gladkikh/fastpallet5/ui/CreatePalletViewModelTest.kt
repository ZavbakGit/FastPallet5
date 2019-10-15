package `fun`.gladkikh.fastpallet5.ui

import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.domain.intety.Type
import `fun`.gladkikh.fastpallet5.repository.CreatePalletRepository
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc.CreatePalletViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


class CreatePalletViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockCreatePalletRepository = mock<CreatePalletRepository>()
    private lateinit var viewModel: CreatePalletViewModel

    private val testFoo = mock<Foo>()

    private val creatPalletLiveData = MutableLiveData<CreatePallet>()
    private val listProductLiveData = MutableLiveData<List<Product>>()

    val testDoc = CreatePallet(
        guid = "12345",
        barcode = "46546546546",
        dataChanged = Date(),
        date = Date(),
        description = "Описание с сервера",
        guidServer = "54321",
        isWasLoadedLastTime = false,
        number = "5",
        status = 1,
        typeFromServer = Type.CREATE_PALLET.nameServer,
        listProduct = listOf(Product(guid = "123"), Product(guid = "321"))
    )

    @Before
    fun setup() {
        reset(mockCreatePalletRepository)
        whenever(mockCreatePalletRepository.getDocByGuidLd(testDoc.guid)).thenReturn(
            creatPalletLiveData
        )
        whenever(mockCreatePalletRepository.getListProductByDocLd(testDoc.guid)).thenReturn(
            listProductLiveData
        )
        viewModel = CreatePalletViewModel(mockCreatePalletRepository)

        //А здесь при любой строке
        whenever(testFoo.getList(any())).thenReturn((0..100).map {
            it.toString()
        })

        //Обращение к переменной
        whenever(testFoo.str).thenReturn("Hi")
    }

    @Test
    fun `setCreatePallet should return CreatePallet`() {
        var result: CreatePallet? = null
        var testData = testDoc


        viewModel.getViewState().observeForever {
            result = it.document
        }
        viewModel.setGuid(testDoc.guid)
        creatPalletLiveData.value = testDoc
        listProductLiveData.value = testData.listProduct

        println(testFoo.str)
        println(testFoo.getList("kkk").joinToString (";"))


        Assert.assertEquals(testData, result)
    }

}

class Foo {
    val str = "456"

    fun getList(str: String): List<String> {
        return (0..10).map {
            it.toString() + str
        }
    }
}