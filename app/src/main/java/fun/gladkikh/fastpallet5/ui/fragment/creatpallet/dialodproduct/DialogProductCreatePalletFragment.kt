package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.domain.extend.getWeightByBarcode
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.returnTextWatcherOnChanged
import android.widget.EditText
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_product_scr.*
import kotlinx.android.synthetic.main.documents_frag.tvInfo
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogProductCreatePalletFragment :
    BaseFragment<WrapDataDialogProductCreatePallet?, DialogProductCreatePalletViewState>() {

    override val layoutRes: Int = R.layout.dialog_product_scr
    override val viewModel: DialogProductCreatePalletViewModel by viewModel()
    private var changed = false

    companion object {
        val EXTRA_GUID_PRODUCT =
            DialogProductCreatePalletFragment::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_DOC = DialogProductCreatePalletFragment::class.java.name + "extra.GUID.DOC"
    }

    private val listEditText: List<EditText> by lazy {
        listOf(
            edBarcode,
            edStart,
            edFinish,
            edCoeff
        )
    }

    private val textChangeListener = returnTextWatcherOnChanged {
        changed = true
        btSave.isEnabled = changed
        refreshWeight()
    }

    override fun renderData(data: WrapDataDialogProductCreatePallet?) {

        if (data?.doc == null) return
        val product = data.product ?: return


        val checkEditDoc = checkEditDoc(data.doc)
        btSave.isEnabled = changed

        listEditText.forEach {
            //Если нельзя редактировать то блокирнем
            if (!checkEditDoc) {
                it.isEnabled = false
            }
            it.removeTextChangedListener(textChangeListener)
        }

        tvInfo.text = product.nameProduct

        edBarcode.setText(product.barcode ?: "")
        edStart.setText(product.weightStartProduct.toString())
        edFinish.setText(product.weightEndProduct.toString())
        edCoeff.setText(product.weightCoffProduct.toString())

        refreshWeight()

        listEditText.forEach {
            it.setSelection(it.text.length)
            it.addTextChangedListener(textChangeListener)
        }
    }

    private fun refreshWeight() {
        val weight = getWeightByBarcode(
            barcode = edBarcode.text.toString(),
            start = edStart.text.toString().toIntOrNull() ?: 0,
            finish = edFinish.text.toString().toIntOrNull() ?: 0,
            coff = edCoeff.text.toString().toFloatOrNull() ?: 0f
        )
        edWeight.setText(weight.toString())
    }


    override fun initSubscription() {
        super.initSubscription()
        viewModel.setGuid(
            guidProduct = arguments?.get(EXTRA_GUID_PRODUCT) as String,
            guidDoc = arguments?.get(EXTRA_GUID_DOC) as String
        )

        btSave.setOnClickListener {
            viewModel.save(
                barcode = edBarcode.text.toString(),
                start = edStart.text.toString(),
                finish = edFinish.text.toString(),
                coff = edCoeff.text.toString()
            )
        }

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            //ToDo слушатель
        })

    }




}