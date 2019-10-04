package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.box_scr.*
import kotlinx.android.synthetic.main.box_scr.edPlace
import kotlinx.android.synthetic.main.box_scr.edWeight
import kotlinx.android.synthetic.main.box_scr.tvInfo

class BoxCreatePalletFragment :
    BaseFragment<BoxWrapDataCreatePallet?, BoxCreatePalletViewState>() {

    override val layoutRes: Int = R.layout.box_scr

    override val viewModel: BoxCreatePalletViewModel by lazy {
        ViewModelProviders.of(this).get(BoxCreatePalletViewModel::class.java)
    }

    companion object {
        val EXTRA_GUID_DOC = this::class.java.name + "extra.GUID.DOC"
        val EXTRA_GUID_PRODUCT = this::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_PALLET = this::class.java.name + "extra.GUID.PALLET"
        val EXTRA_GUID_BOX = this::class.java.name + "extra.GUID.BOX"
    }

    val listEditText: List<EditText> by lazy { listOf(edPlace, edWeight) }

    override fun renderData(data: BoxWrapDataCreatePallet?) {
        tvInfo.text = data?.product?.nameProduct
            ?.plus("\n")
            ?.plus(data?.pallet?.number)

        tv_info_doc_left.text = "".plus(data?.product?.countBox ?: 0)
            .plus(" / ")
            .plus(data?.product?.count ?: 0)

        edPlace.setText(data?.box?.countBox.toString())
        edWeight.setText(data?.box?.weight.toString())
        tvDate.text = data?.box?.data?.toSimpleString()

        tvbarcode.text = data?.box?.barcode ?: ""
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
                is Confirm -> {
                    //ToDo Вынести в общие
                    AlertDialog.Builder(activity!!)
                        .setTitle("Вы уверены!")
                        .setMessage(it.message)
                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                        .setPositiveButton("Да") { dialog, which ->
                            viewModel.confirmedDell()
                        }
                        .show()
                }
            }

        })


        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            //viewModel.addBox(it)
        })

        tvInfo.setOnClickListener {
            viewModel.addBox("${(10..99).random()}123456789")
        }

        tvMenu.setOnClickListener {
            viewModel.dell()
        }
    }
}