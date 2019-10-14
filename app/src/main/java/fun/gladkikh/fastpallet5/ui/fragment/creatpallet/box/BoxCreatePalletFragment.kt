package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.domain.checkEditDoc
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.Close
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.ConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.common.startConfirmDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.box_scr.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoxCreatePalletFragment :
    BaseFragment<BoxWrapDataCreatePallet?, BoxCreatePalletViewState>() {

    override val layoutRes: Int = R.layout.box_scr

    override val viewModel: BoxCreatePalletViewModel by viewModel()

    companion object {
        val EXTRA_GUID_DOC = this::class.java.name + "extra.GUID.DOC"
        val EXTRA_GUID_PRODUCT = this::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_PALLET = this::class.java.name + "extra.GUID.PALLET"
        val EXTRA_GUID_BOX = this::class.java.name + "extra.GUID.BOX"
    }

    private val listEditText: List<EditText> by lazy { listOf(edPlace, edWeight) }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.setDataChangeListener(
                weight = edWeight.text.toString(),
                countBox = edPlace.text.toString()
            )
        }

    }


    override fun renderData(data: BoxWrapDataCreatePallet?) {

        listEditText.forEach {
            it.removeTextChangedListener(textWatcher)
        }

        tvInfo.text = data?.product?.nameProduct
            ?.plus("\n")
            ?.plus(data.pallet?.number)

        tv_info_doc_left.text = "".plus(data?.product?.countBox ?: 0)
            .plus(" / ")
            .plus(data?.product?.count ?: 0)

        edPlace.setText((data?.box?.countBox ?: 0).toString())

        edWeight.apply {
            if (data?.box?.weight != 0f) {
                //Если не ноль устанавливаем
                this.setText(data?.box?.weight.toString())

            } else {
                //А сдесь проверяем
                when (edWeight.text.toString()) {
                    "0.", "", "0", "0.0" -> {
                    }
                    else -> {
                        this.setText("")
                    }
                }
            }
        }



        tvDate.text = data?.box?.data?.toSimpleString()

        tvbarcode.text = data?.box?.barcode ?: ""

        listEditText.forEach {
            if (data?.doc != null) {
                if (!checkEditDoc(data.doc)) {
                    it.isEnabled = false
                }
            }
        }

        listEditText.forEach { ed ->
            //Убираем 0
            if (ed.id != R.id.edWeight) {
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
            viewModel.dell()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onFragmentDestroy()

    }


}