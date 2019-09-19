package `fun`.gladkikh.fastpallet5.ui.fragment

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.domain.intety.CreatePallet
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.fragment.cretapallet.CreatePalletFragment
import `fun`.gladkikh.fastpallet5.viewmodel.DocListViewModel
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.doc_list_frag.*

class DocListFragment : BaseFragment() {
    private lateinit var viewModel: DocListViewModel

    private lateinit var adapter: Adapter


    override fun initSubscription() {
        super.initSubscription()

        viewModel = ViewModelProviders.of(this)
            .get(DocListViewModel::class.java)


        adapter = Adapter(activity as Context)

        viewModel.message.observe(viewLifecycleOwner, Observer {
            hostActivity.showMessage(it)
        })

        viewModel.getListCreatePallets().observe(viewLifecycleOwner, Observer {
            showList(it)
        })

        listView.setOnItemClickListener { _, _, i, _ ->
            val bundle = Bundle()
            bundle.putString(CreatePalletFragment.GUID_DOC, adapter.list[i].guid)
            hostActivity.getHostNavController().navigate(R.id.action_docListFragment_to_createPaletDocFragment, bundle)
        }


        btLoad.setOnClickListener {
            viewModel.loadDocs()
        }

    }

    private fun showList(list: List<CreatePallet>) {
        adapter.list = list
        listView.adapter = adapter
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<CreatePallet>(mContext) {
        override fun bindView(item: CreatePallet, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.guid
            holder.tvLeft.text = item.number
            holder.tvRight.text = item.date?.toSimpleString()
        }

        override fun getLayaot(): Int = R.layout.fr_list_doc_item
        override fun createViewHolder(view: View): Any =
            ViewHolder(view)
    }

    private class ViewHolder(view: View) {
        var tvInfo: TextView = view.findViewById(R.id.tv_item_info)
        var tvLeft: TextView = view.findViewById(R.id.tv_info__doc_left)
        var tvRight: TextView = view.findViewById(R.id.tv_info_doc_right)
    }

    override fun getLayout() = R.layout.doc_list_frag
}