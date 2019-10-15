package `fun`.gladkikh.fastpallet5.ui.fragment.documents

import `fun`.gladkikh.fastpallet5.Constants
import `fun`.gladkikh.fastpallet5.Constants.KEY_DELL
import `fun`.gladkikh.fastpallet5.Constants.KEY_MENU
import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.domain.intety.ItemDocument
import `fun`.gladkikh.fastpallet5.domain.intety.Status
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.common.Command
import `fun`.gladkikh.fastpallet5.ui.fragment.common.startConfirmDialog
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.doc.CreatePalletFragment
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.documents_frag.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DocumentsFragment : BaseFragment<List<ItemDocument>?, DocumentsViewState>() {

    override val layoutRes: Int = R.layout.documents_frag

    override val viewModel: DocumentsViewModel by viewModel()

    private lateinit var adapter: Adapter

    override fun renderData(data: List<ItemDocument>?) {
        listView.adapter = adapter
        data?.let {
            adapter.list = it
        }
    }

    override fun initSubscription() {
        super.initSubscription()
        adapter = Adapter(activity as Context)


        viewModel.getCommandLd().observe(viewLifecycleOwner, Observer {

            when (it) {
                is Command.ConfirmDialog -> {
                    val position = it.data as? Int

                    position?.run {
                        startConfirmDialog(activity!!, "Удалить запись?") {
                            viewModel.confirmedDell(this)
                        }
                    }
                }
            }


        })


        hostActivity.getKeyListenerLd().observe(viewLifecycleOwner, Observer { key ->
            when (key) {
                KEY_DELL -> {

                    listView.selectedItemPosition.takeUnless { position ->
                        position == -1
                    }?.run {
                        viewModel.dell(this)
                    }
                }
                KEY_MENU -> {
                    showMenu()
                }
                Constants.KEY_UNLOAD -> {
                    //Выгружаем документ
                    listView.selectedItemPosition.takeUnless { position ->
                        position == -1
                    }?.run {
                        viewModel.sendDocToServer(this)
                    }
                }
            }
        })

        //Нажали меню
        tvMenu.setOnClickListener {
            showMenu()
        }

        listView.setOnItemClickListener { _, _, i, _ ->
            val bundle = Bundle()
            bundle.putString(CreatePalletFragment.EXTRA_GUID, adapter.list[i].guid)
            hostActivity.getNavController()
                .navigate(R.id.action_documentsFragment_to_creatPalletFragment, bundle)
        }

    }


    private fun showMenu() {
        PopupMenu(activity, tvMenu).run {
            inflate(R.menu.documents_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.settings -> {
                        hostActivity.getNavController()
                            .navigate(R.id.action_documentsFragment_to_settingsFragment)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.download -> {
                        viewModel.loadDocs()
                        return@setOnMenuItemClickListener true
                    }
                    else -> false
                }
            }
            show()
        }
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<ItemDocument>(mContext) {
        override fun bindView(item: ItemDocument, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.description
            holder.tvLeft.text = Status.getStatusById(item.status ?: 0)?.fullName
            holder.tvRight.text = ""
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