package `fun`.gladkikh.fastpallet5.ui.fragment.cretapallet

import `fun`.gladkikh.fastpallet5.Constants.KEY_DELL
import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.domain.intety.Pallet
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.fragment.BaseFragment
import `fun`.gladkikh.fastpallet5.viewmodel.creatpallet.ProductCreatPalletViewModel
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.createpallet_doc_fr.*


class ProductCreatePalletFragment : BaseFragment() {

    private lateinit var adapter: Adapter

    companion object {
        const val GUID_DOC = "guid_doc"
        const val GUID_STRING_PRODUCT = "guid_string_product"
    }

    private lateinit var viewModel: ProductCreatPalletViewModel

    override fun initSubscription() {
        super.initSubscription()

        adapter =
            Adapter(activity as Context)

        viewModel =

            ViewModelProviders.of(
                this,
                ProductCreatPalletViewModel.ViewModelFactory(
                    arguments?.get(GUID_DOC) as String,
                    arguments?.get(GUID_STRING_PRODUCT) as String
                )
            ).get(ProductCreatPalletViewModel::class.java)


        viewModel.messageError.observe(viewLifecycleOwner, Observer {
            hostActivity.showMessage(it)
        })


        viewModel.getDataModelLd().observe(viewLifecycleOwner, Observer {
            tvInfo.text =
                (it.docCreatePallet?.number ?: "")
                    .plus("\n").plus(it.product?.nameProduct ?: "")

            showList(it.listPallet)
        })


        //Открываю экран палеты
        listView.setOnItemClickListener { _, _, i, _ ->
            val bundle = Bundle()
            bundle.putString(PalletCreatePalletFragment.GUID_DOC, viewModel.guidDoc)
            bundle.putString(PalletCreatePalletFragment.GUID_STRING_PRODUCT, viewModel.guidProduct)
            bundle.putString(PalletCreatePalletFragment.GUID_PALLET, adapter.list[i].guid)
            hostActivity.getHostNavController()
                .navigate(
                    R.id.action_stringProductCreatPalletFragment_to_palletCreatPalletFragment,
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
                    listView.selectedItemPosition.takeUnless { position -> position == -1 }?.let {
                        viewModel.keyPressDell(it)
                    }
                }
            }
        })

        //Открываю диалог подтверждения
        viewModel.getConfirmDellDialogLd().observe(viewLifecycleOwner, Observer {
            AlertDialog.Builder(activity!!)
                .setTitle(it.message)
                .setMessage("Удалить")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton("Да") { dialog, which ->
                    viewModel.dialogConfirmedDell(it.position)
                }
                .show()
        })
    }

    override fun getLayout() = R.layout.createpallet_doc_fr

    private fun showList(list: List<Pallet>) {
        adapter.list = list
        listView.adapter = adapter
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<Pallet>(mContext) {
        override fun bindView(item: Pallet, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.number
            holder.tvLeft.text = item.barcode
            holder.tvRight.text = item.dataChanged?.toSimpleString()
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