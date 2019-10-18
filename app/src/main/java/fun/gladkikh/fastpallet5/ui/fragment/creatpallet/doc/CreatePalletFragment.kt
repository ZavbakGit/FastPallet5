package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product.ProductCreatePalletFragment
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.documents_frag.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePalletFragment : BaseFragment<DocWrapDataCreatePallet?, CreatePalletViewState>() {

    override val layoutRes: Int = R.layout.documents_frag

    override val viewModel: CreatePalletViewModel by viewModel()

    companion object {
        val EXTRA_GUID = CreatePalletFragment::class.java.name + "extra.GUID"
    }

    private lateinit var adapter: Adapter

    override fun renderData(data: DocWrapDataCreatePallet?) {
        tvInfo.text = data?.doc?.description
        listView.adapter = adapter
        data?.let {
            adapter.list = data.listItem
        }
    }

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)
        viewModel.setGuid(arguments?.get(EXTRA_GUID) as String)


        listView.setOnItemClickListener { _, _, i, _ ->
            val bundle = Bundle()
            bundle.putString(ProductCreatePalletFragment.EXTRA_GUID_PRODUCT, adapter.list[i].product?.guid)
            bundle.putString(ProductCreatePalletFragment.EXTRA_GUID_DOC, arguments?.get(EXTRA_GUID) as String)
            hostActivity.getNavController().navigate(R.id.action_creatPalletFragment_to_productCreatePalletFragment, bundle)
        }

        tvInfo.setOnClickListener {
            viewModel.test()

        }
    }

    private class Adapter(context: Context) : MyBaseAdapter<ItemProduct>(context) {
        override fun bindView(item: ItemProduct, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.product?.nameProduct
            holder.tvLeft.text =    "${item.product?.count}".plus(" / ").plus(item.product?.countBox)

            //ToDo Пересчитать причем постораться в другом потоке
            item.infoListBoxWrap.let {
                holder.tvRight.text = it?.getInfoPallet()
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