package `fun`.gladkikh.fastpallet5.ui.activity

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.viewmodel.util.SingleLiveEvent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.gladkikh.mylibrary.BarcodeHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.item_box.*
import kotlinx.android.synthetic.main.progress_overlay.*


class MainActivity : BaseActivity(), HostActivity {


    lateinit var barcodeHelper: BarcodeHelper
    var barcode = SingleLiveEvent<String>()

    private lateinit var navController: NavController

    override fun showMessage(text: CharSequence) {
        Snackbar.make(root, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun getHostNavController() = navController

    override fun isShowProgress() = (progressView.visibility == View.VISIBLE)

    override fun showProgress() {
        progressView.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressView.visibility = View.GONE
    }

    override fun getBarcodeSingleLiveData(): SingleLiveEvent<String> = barcode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

        //ToDo параметры ТСД из настроек
        barcodeHelper = BarcodeHelper(this, BarcodeHelper.TYPE_TSD.ATOL_SMART_DROID)
        barcodeHelper.getBarcodeLiveData().observe(this, Observer {
            barcode.postValue(it)
        })

        val options = NavOptions.Builder().
            setEnterAnim(R.anim.nav_custom_enter_anim).
            build()


        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = false
        }

        bottom_sheet.setOnClickListener {
            bottomSheetBehavior.state =

                when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> BottomSheetBehavior.STATE_EXPANDED
                    BottomSheetBehavior.STATE_EXPANDED -> BottomSheetBehavior.STATE_COLLAPSED
                    else -> BottomSheetBehavior.STATE_COLLAPSED
                }
        }

        btSettings.setOnClickListener {
            navController.navigate(R.id.settingsFragment,null,options)
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun getLayout() = R.layout.activity_main
}

