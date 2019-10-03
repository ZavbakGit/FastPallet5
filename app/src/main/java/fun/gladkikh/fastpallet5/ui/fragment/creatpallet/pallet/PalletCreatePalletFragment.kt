package `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.pallet

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.domain.intety.Box
import `fun`.gladkikh.fastpallet5.ui.adapter.MyBaseAdapter
import `fun`.gladkikh.fastpallet5.ui.base.BaseFragment
import `fun`.gladkikh.fastpallet5.ui.fragment.creatpallet.dialodproduct.DialogProductCreatePalletFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.documents_frag.*
import java.util.concurrent.TimeUnit

class PalletCreatePalletFragment :
    BaseFragment<WrapDataPalletCreatePallet?, PalletCreatPalletViewState>() {

    override val layoutRes: Int = R.layout.documents_frag

    override val viewModel: PalletCreatePalletViewModel by lazy {
        ViewModelProviders.of(this).get(PalletCreatePalletViewModel::class.java)
    }

    companion object {
        val EXTRA_GUID_DOC = PalletCreatePalletFragment::class.java.name + "extra.GUID.DOC"
        val EXTRA_GUID_PRODUCT = PalletCreatePalletFragment::class.java.name + "extra.GUID.PRODUCT"
        val EXTRA_GUID_PALLET = PalletCreatePalletFragment::class.java.name + "extra.GUID.PALLET"
    }

    private lateinit var adapter: Adapter

    override fun renderData(data: WrapDataPalletCreatePallet?) {
        tvInfo.text = data?.pallet?.number
        listView.adapter = adapter
        data?.pallet?.boxes?.let {
            adapter.list = data.pallet.boxes.sortedByDescending { it.data }
        }


    }

    override fun initSubscription() {
        super.initSubscription()

        adapter = Adapter(activity as Context)


        viewModel.getInfoWrap().observe(viewLifecycleOwner, Observer {
            tv_info_doc_right.text = "".plus(it.countBox?:0)
                .plus(" / ")
                .plus(it?.weight?:0)
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

        tvMenu.setOnClickListener {

            Flowable.interval(300, TimeUnit.MILLISECONDS)
                .take(500)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel.addBox("${(10..99).random()}123456789")
                }

            //viewModel.addBox("${(10..99).random()}123456789")
        }

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