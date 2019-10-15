package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.returnTextWatcherOnChanged
import android.widget.EditText
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.dialog_product_scr.*
import kotlinx.android.synthetic.main.dialog_product_scr.btSave
import kotlinx.android.synthetic.main.dialog_product_scr.edBarcode
import kotlinx.android.synthetic.main.dialog_product_scr.edWeight
import kotlinx.android.synthetic.main.documents_frag.tvInfo
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogProductCreatePalletFragment :
    BaseFragment<WrapDataDialogProductCreatePallet?, DialogProductCreatePalletViewState>() {

    override val layoutRes: Int = R.layout.dialog_product_scr
    override val viewModel: DialogProductCreatePalletViewModel by viewModel()
    private var changed = false

    companion object {
        val EXTRA_GUID_PRODUCT = DialogProductCreatePalletFragment::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_DOC = DialogProductCreatePalletFragment::class.java.name + "extra.GUID.DOC"
    }

    private val listEditText: List<EditText> by lazy { listOf(edBarcode, edStart, edFinish, edCoeff) }

    private val textChangeListener = returnTextWatcherOnChanged {
        changed = true
        btSave.isEnabled = changed
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
        edWeight.setText(data.weight.toString())
        edCoeff.setText(product.weightCoffProduct.toString())

        listEditText.forEach {
            it.setSelection(it.text.length)
            it.addTextChangedListener(textChangeListener)
        }
    }

    override fun initSubscription() {
        super.initSubscription()
        viewModel.setGuid(
            guidProduct = arguments?.get(EXTRA_GUID_PRODUCT) as String,
            guidDoc = arguments?.get(EXTRA_GUID_DOC) as String
        )

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            //ToDo слушатель
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroy()

    }


}