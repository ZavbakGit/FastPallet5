package `fun`.gladkikh.fastpallet5.ui.fragment.cretapallet

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.domain.intety.Product
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.fragment.BaseFragment
import `fun`.gladkikh.fastpallet5.viewmodel.creatpallet.CreatPalletViewModel
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.createpallet_doc_fr.*


class CreatePalletFragment : BaseFragment() {

    private lateinit var adapter: Adapter

    companion object {
        const val GUID_DOC = "guid_doc"
    }

    private lateinit var viewModel: CreatPalletViewModel

    override fun initSubscription() {
        super.initSubscription()

        adapter =
            Adapter(activity as Context)

        viewModel = ViewModelProviders.of(
            this,
            CreatPalletViewModel.ViewModelFactory(arguments?.get(GUID_DOC) as String)
        )
            .get(CreatPalletViewModel::class.java)


        viewModel.getDoc().observe(viewLifecycleOwner, Observer {
            tvInfo.text = it.guid

        })

        viewModel.getListProduct().observe(viewLifecycleOwner, Observer {
            showList(it)
        })

        listView.setOnItemClickListener { _, _, i, _ ->

            val bundle = Bundle()
            bundle.putString(ProductCreatePalletFragment.GUID_DOC, viewModel.guidDoc)
            bundle.putString(ProductCreatePalletFragment.GUID_STRING_PRODUCT, adapter.list[i].guid)

            hostActivity.getHostNavController().navigate(
                R.id.action_createPaletDocFragment_to_stringProductCreatPalletFragment
                , bundle
            )
        }

    }

    override fun getLayout() = R.layout.createpallet_doc_fr

    private fun showList(list: List<Product>) {
        adapter.list = list
        listView.adapter = adapter
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<Product>(mContext) {
        override fun bindView(item: Product, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.nameProduct
            holder.tvLeft.text = item.barcode
            holder.tvRight.text = item.guid
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