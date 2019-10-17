package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box1

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment1
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box.BoxCreatePalletFragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.box_scr.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class Box1CreatePalletFragment : BaseFragment1() {

    override val layoutRes: Int = R.layout.box_scr
    override val viewModel: Box1CreatePalletViewModel by viewModel()

    companion object {
        val EXTRA_GUID_BOX = this::class.java.name + "extra.GUID.BOX"
    }

    override fun initSubscription() {
        super.initSubscription()

        viewModel.setGuid(arguments?.get(BoxCreatePalletFragment.EXTRA_GUID_BOX) as String)

        viewModel.getBox1CreatePalletViewStateLd().observe(viewLifecycleOwner, Observer {
            refreshScreen(it)
        })

        viewModel.getCountBufferSaveLiveData().observe(viewLifecycleOwner, Observer {
            tvBuffer.text = it.toString()
        })

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.addBox(it)
        })

        tvInfo.setOnClickListener {
            testTvInfoClick()
        }
    }

    private fun testTvInfoClick() {
        viewModel.addBox("${(10..99).random()}123456789")
    }

    private fun refreshScreen(viewState: Box1CreatePalletViewState) {

        //Центр
        tvInfo.text = viewState.productName
            ?.plus("\n")
            ?.plus(viewState.palletNumber)

        //Лево
        tv_info_doc_left.text = "".plus(viewState.palletCountBox ?: 0)
            .plus(" / ")
            .plus(viewState.palletWeight)

        //Дата
        tvDate.text = viewState.boxGuid?.plus("\n")
            .plus(viewState.boxDate?.toSimpleString() ?: "")

        edBarcode.setText((viewState.boxCountBox ?: 0).toString())
        edWeight.setText((viewState.boxWeight ?: 0f).toString())

        //Штрихкод
        tvbarcode.text = viewState.boxBarcode ?: ""

        //Право итоги
        tv_info_doc_right.text = "".plus(viewState.palletCountBox ?: 0)
            .plus(" / ")
            .plus(viewState.palletWeight ?: 0)

    }
}