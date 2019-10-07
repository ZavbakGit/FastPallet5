package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet

import `fun`.gladkikh.fastpallet5.Constants
import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.*
import `fun`.gladkikh.fastpallet5.ui.fragment.common.startConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box.BoxCreatePalletFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct.DialogProductCreatePalletFragment
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.documents_frag.*

class PalletCreatePalletFragment :
    BaseFragment<PalletWrapDataCreatePallet?, PalletCreatPalletViewState>() {

    override val layoutRes: Int = R.layout.documents_frag

    override val viewModel: PalletCreatePalletViewModel by lazy {
        ViewModelProviders.of(this).get(PalletCreatePalletViewModel::class.java)
    }

    companion object {
        val EXTRA_GUID_DOC = this::class.java.name + "extra.GUID.DOC"
        val EXTRA_GUID_PRODUCT = this::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_PALLET = this::class.java.name + "extra.GUID.PALLET"
    }

    private lateinit var adapter: Adapter

    override fun renderData(data: PalletWrapDataCreatePallet?) {
        tvInfo.text = data?.pallet?.number
        listView.adapter = adapter
        data?.pallet?.boxes?.let {
            adapter.list = data.pallet.boxes
        }


    }

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)


        viewModel.getInfoWrap().observe(viewLifecycleOwner, Observer {
            tv_info_doc_right.text = "".plus(it.countBox ?: 0)
                .plus(" / ")
                .plus(it?.weight ?: 0)
        })


        viewModel.setGuid(
            arguments?.get(EXTRA_GUID_DOC) as String,
            arguments?.get(EXTRA_GUID_PRODUCT) as String,
            arguments?.get(EXTRA_GUID_PALLET) as String
        )

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.addBox(it)
        })


        tvInfo.setOnClickListener {
            //Открываю диалог продуктов
            val bundle = Bundle()
            bundle.putString(
                DialogProductCreatePalletFragment.EXTRA_GUID_PRODUCT,
                arguments?.get(EXTRA_GUID_PRODUCT) as String
            )
            bundle.putString(
                DialogProductCreatePalletFragment.EXTRA_GUID_DOC,
                arguments?.get(EXTRA_GUID_DOC) as String
            )
            hostActivity.getNavController()
                .navigate(
                    R.id.action_palletCreatePalletFragment_to_dialogProductCreatePalletFragment,
                    bundle
                )
        }

        listView.setOnItemClickListener { _, _, i, _ ->
            openBox(adapter.list[i].guid)
        }

        viewModel.getCommandLd().observe(viewLifecycleOwner, Observer {

            when (it) {
                is ConfirmDialog -> {
                    val position = it.data as? Int

                    position?.run {
                        startConfirmDialog(activity!!, "Удалить запись?") {
                            viewModel.confirmedDell(this)
                        }
                    }
                }

                is OpenForm -> {
                    openBox(it.data as String)
                }
            }


        })


        hostActivity.getKeyListenerLd().observe(viewLifecycleOwner, Observer { key ->
            when (key) {
                //Проверяем, что нажата dell
                Constants.KEY_DELL -> {

                    //Проверяем, что выбрана строка
                    listView.selectedItemPosition.takeUnless { position ->
                        position == -1
                    }?.run {
                        viewModel.dell(this)
                    }
                }
            }
        })



        tvMenu.setOnClickListener {
            //            Flowable.interval(300, TimeUnit.MILLISECONDS)
//                .take(500)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    viewModel.addBox("${(10..99).random()}123456789")
//                }

            viewModel.addBox("${(10..99).random()}123456789")
        }

    }

    private fun openBox(guid: String) {
        val bundle = Bundle()
        bundle.putString(
            BoxCreatePalletFragment.EXTRA_GUID_DOC,
            arguments?.get(EXTRA_GUID_DOC) as String
        )
        bundle.putString(
            BoxCreatePalletFragment.EXTRA_GUID_PRODUCT,
            arguments?.get(EXTRA_GUID_PRODUCT) as String
        )
        bundle.putString(
            BoxCreatePalletFragment.EXTRA_GUID_PALLET,
            arguments?.get(EXTRA_GUID_PALLET) as String
        )
        bundle.putString(BoxCreatePalletFragment.EXTRA_GUID_BOX, guid)

        hostActivity.getNavController().navigate(
            R.id.action_palletCreatePalletFragment_to_boxCreatePalletFragment,
            bundle
        )
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<Box>(mContext) {
        override fun bindView(item: Box, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.data?.toSimpleString()
                //.plus("\n").plus(item.guid)
                .plus("\n").plus(item.barcode)

            //holder.tvLeft.text = item.countBox.toString()
            holder.tvRight.text = ""
                .plus(item.countBox.toString())
                .plus(" / ")
                .plus(item.weight.toString())
        }

        override fun getLayaot(): Int = R.layout.fr_list_doc_item
        override fun createViewHolder(view: View): Any =
            ViewHolder(view)
    }

    private class ViewHolder(view: View) {
        var tvInfo: TextView = view.findViewById(R.id.tv_item_info)
        var tvLeft: TextView = view.findViewById(R.id.tv_info_doc_left)
        var tvRight: TextView = view.findViewById(R.id.tv_info_doc_right)
    }

}