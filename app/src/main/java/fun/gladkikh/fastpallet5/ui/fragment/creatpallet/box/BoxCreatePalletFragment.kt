package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.Close
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.ConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.common.startConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.returnTextWatcherOnChanged
import android.widget.EditText
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.box_scr.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoxCreatePalletFragment :
    BaseFragment<BoxWrapDataCreatePallet?, BoxCreatePalletViewState>() {

    override val layoutRes: Int = R.layout.box_scr
    override val viewModel: BoxCreatePalletViewModel by viewModel()
    private var changed = false


    companion object {
        val EXTRA_GUID_DOC = this::class.java.name + "extra.GUID.DOC"
        val EXTRA_GUID_PRODUCT = this::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_PALLET = this::class.java.name + "extra.GUID.PALLET"
        val EXTRA_GUID_BOX = this::class.java.name + "extra.GUID.BOX"
    }

    private val listEditText: List<EditText> by lazy { listOf(edBarcode, edWeight) }

   private val textChangeListener = returnTextWatcherOnChanged {
        changed = true
        btSave.isEnabled = changed
    }


    override fun renderData(data: BoxWrapDataCreatePallet?) {

        //Если пустая то просто не обновляем
        if (data?.doc == null) return
        val checkEditDoc = checkEditDoc(data.doc)

        btSave.isEnabled = changed
        btDell.isEnabled = checkEditDoc

        listEditText.forEach {
            //Если нельзя редактировать то блокирнем
            if (!checkEditDoc) {
                it.isEnabled = false
            }
            it.removeTextChangedListener(textChangeListener)
        }

        tvInfo.text = data.product?.nameProduct
            ?.plus("\n")
            ?.plus(data.pallet?.number)

        tv_info_doc_left.text = "".plus(data.product?.countBox ?: 0)
            .plus(" / ")
            .plus(data.product?.count ?: 0)

        tvDate.text = data.box?.data?.toSimpleString()

        edBarcode.setText((data.box?.countBox ?: 0).toString())
        edWeight.setText((data.box?.weight ?: 0).toString())

        tvbarcode.text = data.box?.barcode ?: ""

        listEditText.forEach {
            it.setSelection(it.text.length)
            it.addTextChangedListener(textChangeListener)
        }


    }

    override fun initSubscription() {
        super.initSubscription()

        viewModel.setGuid(
            guidDoc = arguments?.get(EXTRA_GUID_DOC) as String,
            guidProduct = arguments?.get(EXTRA_GUID_PRODUCT) as String,
            guidPallet = arguments?.get(EXTRA_GUID_PALLET) as String,
            guidBox = arguments?.get(EXTRA_GUID_BOX) as String
        )

        viewModel.getInfoWrap().observe(viewLifecycleOwner, Observer {
            tv_info_doc_right.text = "".plus(it.countBox ?: 0)
                .plus(" / ")
                .plus(it?.weight ?: 0)
        })

        viewModel.getCommandLd().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Close -> {
                    hostActivity.getNavController().popBackStack()
                }
                is ConfirmDialog -> {
                    startConfirmDialog(activity!!, "Удалить запись?") {
                        viewModel.confirmedDell()
                    }

                }
            }

        })

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.addBox(it)
        })

        tvInfo.setOnClickListener {
            //ToDo this is test
            viewModel.addBox("${(10..99).random()}123456789")
        }

        btDell.setOnClickListener {
            dell()
        }

        btSave.setOnClickListener {
            save()
        }
    }

    private fun save() {
        if (btSave.isEnabled) {
            viewModel.setDataChangeListener(edWeight.text.toString(), edBarcode.text.toString())
        }
    }

    private fun dell() {
        if(btDell.isEnabled){
            viewModel.dell()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroy()

    }


}