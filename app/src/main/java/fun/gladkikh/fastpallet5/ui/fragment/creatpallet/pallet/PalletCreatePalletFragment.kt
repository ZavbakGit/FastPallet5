package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet

import `fun`.gladkikh.fastpallet5.Constants.KEY_ADD
import `fun`.gladkikh.fastpallet5.Constants.KEY_DELL
import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.ConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.OpenForm
import `fun`.gladkikh.fastpallet5.ui.fragment.common.startConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.box.BoxCreatePalletFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct.DialogProductCreatePalletFragment
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.documents_frag.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PalletCreatePalletFragment :
    BaseFragment<PalletWrapDataCreatePallet?, PalletCreatPalletViewState>() {

    override val layoutRes: Int = R.layout.documents_frag

    override val viewModel: PalletCreatePalletViewModel by viewModel()


    companion object {
        val EXTRA_GUID_DOC = this::class.java.name + "extra.GUID.DOC"
        val EXTRA_GUID_PRODUCT = this::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_PALLET = this::class.java.name + "extra.GUID.PALLET"
    }

    private lateinit var adapter: Adapter

    override fun renderData(data: PalletWrapDataCreatePallet?) {
        tvInfo.text = data?.pallet?.number
        listView.adapter = adapter
        adapter.list = data?.listItem ?: listOf()
        tv_info_doc_left.text = ""
    }

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)


        viewModel.getInfoWrap().observe(viewLifecycleOwner, Observer {
            tv_info_doc_right.text =
                it.row.toString().plus(" / ") //Записей
                    .plus(it.countBox ?: 0) //Коробок
                    .plus(" / ")
                    .plus(it?.weight ?: 0) //Вес
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
            adapter.list[i].box?.guid?.let { openBox(it) }
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
                KEY_DELL -> {
                    //Проверяем, что выбрана строка
                    listView.selectedItemPosition.takeUnless { position ->
                        position == -1
                    }?.run {
                        viewModel.dell(this)
                    }
                }
                KEY_ADD -> {
                    viewModel.addBox()
                }

            }
        })


        //ToDo test
        tvMenu.setOnClickListener {
            //            Flowable.interval(300, TimeUnit.MILLISECONDS)
//                .take(500)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    viewModel.saveBox("${(10..99).random()}123456789")
//                }
            //(0..20).forEach {
            viewModel.addBox("${(10..99).random()}123456789")
            //}

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
            //R.id.action_palletCreatePalletFragment_to_boxCreatePalletFragment,
            R.id.action_palletCreatePalletFragment_to_box1CreatePalletFragment,
            bundle
        )
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<ItemBox>(mContext) {
        override fun bindView(item: ItemBox, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text =
                item.number.toString().plus("   ")
                    .plus(item.box?.data?.toSimpleString())
                    .plus("\n").plus(item.box?.barcode ?: "")

            holder.tvLeft.text = ""
            holder.tvRight.text = ""
                .plus(item.box?.countBox.toString())
                .plus(" / ")
                .plus(item.box?.weight.toString())
        }

        override fun getLayout(): Int = R.layout.fr_list_doc_item
        override fun createViewHolder(view: View): Any =
            ViewHolder(view)
    }

    private class ViewHolder(view: View) {
        var tvInfo: TextView = view.findViewById(R.id.tv_item_info)
        var tvLeft: TextView = view.findViewById(R.id.tv_info_doc_left)
        var tvRight: TextView = view.findViewById(R.id.tv_info_doc_right)
    }

}