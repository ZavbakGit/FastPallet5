package `fun`.gladkikh.fastpallet5.ui.activity

import androidx.appcompat.app.AppCompatActivity


class MainActivity1 : AppCompatActivity() {

//    private lateinit var viewModel: PalletViewModel
//
//    lateinit var barcodeHelper: BarcodeHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        rvItemPrice.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter = BoxAdapter()
//        }
//
//        viewModel = ViewModelProviders.of(this)
//            .get(PalletViewModel::class.java)
//
//
//        viewModel.listBoxMld.observe(this, Observer {
//            (rvItemPrice.adapter as BoxAdapter).updateData(it)
//        })
//
//        viewModel.infoMld.observe(this, Observer {
//            textInfo.text = it?:""
//        })
//
//
//        var typeTsd = BarcodeHelper.TYPE_TSD.ATOL_SMART_DROID
//        barcodeHelper = BarcodeHelper(this, typeTsd)
//
//        barcodeHelper.getDataFlowable()
//            .subscribe {
//                viewModel.addBox(
//                    Box(
//                        barcode = "$it",
//                        countBox = 1,
//                        data = Date(),
//                        guid = UUID.randomUUID().toString(),
//                        weight = 50f
//                    )
//                )
//            }
//
//        textInfo.setOnClickListener {
//            Flowable.interval(300, TimeUnit.MILLISECONDS)
//                .take(1000)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    //presenter.readBarcode()
//                   barcodeHelper.barcodePublishSubject.onNext("6970171170854")
//                }
//        }
//
//
//    }
}
