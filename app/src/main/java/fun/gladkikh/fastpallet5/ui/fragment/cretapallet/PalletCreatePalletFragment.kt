package `fun`.gladkikh.fastpallet5.ui.fragment.cretapallet

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.fragment.BaseFragment
import `fun`.gladkikh.fastpallet5.viewmodel.creatpallet.PalletCreatPalletViewModel
import android.content.Context

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.createpallet_doc_fr.*
import java.math.BigDecimal
import java.util.concurrent.TimeUnit


class PalletCreatePalletFragment : BaseFragment() {

    private lateinit var adapter: Adapter

    companion object {
        const val GUID_DOC = "guid_doc"
        const val GUID_STRING_PRODUCT = "guid_string_product"
        const val GUID_PALLET = "guid_pallet"
    }

    private lateinit var viewModel: PalletCreatPalletViewModel

    override fun initSubscription() {
        super.initSubscription()

        adapter =
            Adapter(activity as Context)

        viewModel =

            ViewModelProviders.of(
                this,
                PalletCreatPalletViewModel.ViewModelFactory(
                    arguments?.get(GUID_DOC) as String,
                    arguments?.get(GUID_STRING_PRODUCT) as String,
                    arguments?.get(GUID_PALLET) as String
                )
            ).get(PalletCreatPalletViewModel::class.java)




        viewModel.getPalletByGuid().observe(viewLifecycleOwner, Observer {
            tvInfo.text = it.guid
        })

        viewModel.getListBoxByPallet().observe(viewLifecycleOwner, Observer { list ->
            showList(list.sortedByDescending { it.data })
            viewModel.refreshInfo(list)
        })

        viewModel.getInfoWrap().observe(viewLifecycleOwner, Observer {
            tvInfo.text = viewModel.guidPallet
                .plus("\n").plus(it.countBox)
                .plus("/")
                .plus(it.weight)

        })

        hostActivity.getBarcodeSingleLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.addBox(it)
        })

        tvInfo.setOnClickListener {
            Flowable.interval(300, TimeUnit.MILLISECONDS)
                .take(500)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel.addBox("100")
                }
        }

    }

    override fun getLayout() = R.layout.createpallet_doc_fr

    private fun showList(list: List<Box>) {
        adapter.list = list
        listView.adapter = adapter
    }

    private class Adapter(mContext: Context) : MyBaseAdapter<Box>(mContext) {
        override fun bindView(item: Box, holder: Any) {
            holder as ViewHolder
            holder.tvInfo.text = item.data?.toSimpleString()
                .plus("\n").plus(item.guid)
                .plus("\n").plus(item.barcode)

            holder.tvLeft.text = item.countBox.toString()
            holder.tvRight.text = item.weight.toString()
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

}