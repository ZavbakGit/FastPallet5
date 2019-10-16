package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product

import `fun`.gladkikh.fastpallet5.Constants.KEY_DELL
import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command.ConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.common.startConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet.PalletCreatePalletFragment
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.documents_frag.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductCreatePalletFragment :
    BaseFragment<WrapDataProductCreatePallet?, ProductCreatePalletViewState>() {

    override val layoutRes: Int = R.layout.documents_frag

    override val viewModel: ProductCreatePalletViewModel by viewModel()

    companion object {
        val EXTRA_GUID_PRODUCT = ProductCreatePalletFragment::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_DOC = ProductCreatePalletFragment::class.java.name + "extra.GUID.DOC"
    }

    private lateinit var adapter: Adapter

    override fun renderData(data: WrapDataProductCreatePallet?) {

        //ToDo Вывести план факт на товар и факт в списке
        tvInfo.text = data?.product?.nameProduct
        listView.adapter = adapter
        data?.product?.pallets?.let {
            adapter.list = data.listItem
        }
        tv_info_doc_right.text = data?.infoListBoxWrap?.getInfo() ?: ""
        tv_info_doc_left.text = (data?.product?.countBox ?: 0).toString()
            .plus(" / ")
            .plus((data?.product?.count ?: 0).toString())
    }

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)
        viewModel.setGuid(
            arguments?.get(EXTRA_GUID_DOC) as String,
            arguments?.get(EXTRA_GUID_PRODUCT) as String
        )


        listView.setOnItemClickListener { _, _, i, _ ->
            val bundle = Bundle()
            bundle.putString(
                PalletCreatePalletFragment.EXTRA_GUID_DOC,
                arguments?.get(EXTRA_GUID_DOC) as String
            )
            bundle.putString(
                PalletCreatePalletFragment.EXTRA_GUID_PRODUCT,
                arguments?.get(EXTRA_GUID_PRODUCT) as String
            )
            bundle.putString(
                PalletCreatePalletFragment.EXTRA_GUID_PALLET,
                adapter.list[i].pallet?.guid
            )
            hostActivity.getNavController().navigate(
                R.id.action_productCreatePalletFragment_to_palletCreatePalletFragment,
                bundle
            )
        }

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.addPallet(it)
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
            }
        })

        //Открываю диалог подтверждения
        viewModel.getCommandLd().observe(viewLifecycleOwner, Observer {
            when (it) {
                is ConfirmDialog -> {
                    val position = it.data as? Int

                    position?.run {
                        startConfirmDialog(activity!!, "Удалить паллету?") {
                            viewModel.confirmedDell(this)
                        }
                    }
                }
            }
        })

        tvInfo.setOnClickListener {
            viewModel.addPallet("<pal>0214000000${(10..99).random()}</pal>")
        }
    }


    class Adapter(mContext: Context) : MyBaseAdapter<ItemPallet>(mContext) {
        override fun bindView(item: ItemPallet, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.pallet?.number
            holder.tvLeft.text = item.number.toString()
            holder.tvRight.text = item.pallet?.dataChanged?.toSimpleString()

            //Обновляем инфо

            item.infoListBoxWrap.let {
                holder.tvRight.text = it?.getInfo()
            }
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