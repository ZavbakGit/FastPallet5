package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.dialog_product_scr.*
import kotlinx.android.synthetic.main.documents_frag.tvInfo

class DialogProductCreatePalletFragment :
    BaseFragment<WrapDataDialogProductCreatePallet?, DialogProductCreatePalletViewState>() {

    override val layoutRes: Int = R.layout.dialog_product_scr

    override val viewModel: DialogProductCreatePalletViewModel by lazy {
        ViewModelProviders.of(this).get(DialogProductCreatePalletViewModel::class.java)
    }

    companion object {
        val EXTRA_GUID_PRODUCT = DialogProductCreatePalletFragment::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_DOC = DialogProductCreatePalletFragment::class.java.name + "extra.GUID.DOC"
    }

    private val listEditText: List<EditText> by lazy { listOf(edPlace, edStart, edFinish, edCoeff) }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.setDataChangeListener(
                barcode = edPlace.text.toString(),
                start = edStart.text.toString(),
                finish = edFinish.text.toString(),
                coff = edCoeff.text.toString()
            )
        }

    }

    override fun renderData(data: WrapDataDialogProductCreatePallet?) {
        val product = data?.product



        listEditText.forEach {
            it.removeTextChangedListener(textWatcher)
        }

        tvInfo.text = product?.nameProduct
        edPlace.setText(product?.barcode ?: "")
        edStart.setText(product?.weightStartProduct.toString())
        edFinish.setText(product?.weightEndProduct.toString())

        edCoeff.apply {
            if (product?.weightCoffProduct != 0f) {
                //Если не ноль устанавливаем
                this.setText(product?.weightCoffProduct.toString())

            } else {
                //А сдесь проверяем
                when (edCoeff.text.toString()) {
                    "0.", "", "0", "0.0" -> {
                    }
                    else -> {
                        this.setText("")
                    }
                }
            }
        }


        edWeight.setText(data?.weight.toString())

        listEditText.forEach { ed ->
            //Убираем 0
            if (ed.id != R.id.edCoeff) {
                if (ed.text.toString() == "0") {
                    ed.setText("")
                }
            }
            //Курсор назад
            ed.text?.length?.let {
                ed.setSelection(it)
            }
            //Ставим слушатель
            ed.addTextChangedListener(textWatcher)
        }
    }

    override fun initSubscription() {
        super.initSubscription()
        viewModel.setGuid(
            guidProduct = arguments?.get(EXTRA_GUID_PRODUCT) as String,
            guidDoc = arguments?.get(EXTRA_GUID_DOC) as String
        )

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {

        })

        edStart.setOnFocusChangeListener { view, focus ->
            if (focus) {
                (view as EditText).setSelection(0, 0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroy()

    }


}