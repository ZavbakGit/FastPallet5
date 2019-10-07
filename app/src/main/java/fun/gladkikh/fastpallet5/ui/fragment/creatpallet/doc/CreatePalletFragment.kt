package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.product.ProductCreatePalletFragment
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.documents_frag.*

class CreatePalletFragment : BaseFragment<CreatePallet?, CreatPalletViewState>() {

    override val layoutRes: Int = R.layout.documents_frag

    override val viewModel: CreatePalletViewModel by lazy {
        ViewModelProviders.of(this).get(CreatePalletViewModel::class.java)
    }

    companion object {
        val EXTRA_GUID = CreatePalletFragment::class.java.name + "extra.GUID"
    }

    private lateinit var adapter: Adapter

    override fun renderData(data: CreatePallet?) {
        tvInfo.text = data?.description
        listView.adapter = adapter
        data?.let {
            adapter.list = data.listProduct
        }
    }

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)
        viewModel.setGuid(arguments?.get(EXTRA_GUID) as String)


        listView.setOnItemClickListener { _, _, i, _ ->
            val bundle = Bundle()
            bundle.putString(ProductCreatePalletFragment.EXTRA_GUID_PRODUCT, adapter.list[i].guid)
            bundle.putString(ProductCreatePalletFragment.EXTRA_GUID_DOC, arguments?.get(EXTRA_GUID) as String)
            hostActivity.getNavController().navigate(R.id.action_creatPalletFragment_to_productCreatePalletFragment, bundle)
        }
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<Product>(mContext) {
        override fun bindView(item: Product, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.nameProduct
            holder.tvLeft.text =    "${item.count}".plus(" / ").plus(item.countBox)

            //ToDo Пересчитать причем постораться в другом потоке
            holder.tvRight.text = "10 / 100"
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