package `fun`.gladkikh.fastpallet5.ui.activity

import `fun`.gladkikh.fastpallet5.R
import `fun`.gladkikh.fastpallet5.repository.DocumetRepository
import `fun`.gladkikh.fastpallet5.ui.base.SingleLiveEvent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.gladkikh.mylibrary.BarcodeHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_box.*
import kotlinx.android.synthetic.main.progress_overlay.*


class MainActivity : BaseActivity(),HostActivity {


    lateinit var barcodeHelper: BarcodeHelper

    private lateinit var navController: NavController

    override fun showMessage(text: CharSequence) {
        Snackbar.make(root, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun showErrorMessage(text: CharSequence) {
        Snackbar.make(root, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun getNavController() = navController

    override fun showProgress() {
        progressView.visibility = View.VISIBLE
        showProgress.postValue(true)
    }

    override fun hideProgress() {
        progressView.visibility = View.GONE
        showProgress.postValue(false)
    }

    override fun getBarcodeSingleLiveData(): SingleLiveEvent<String> = barcode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        barcodeHelper = BarcodeHelper(this, BarcodeHelper.TYPE_TSD.ATOL_SMART_DROID)
        barcodeHelper.getBarcodeLiveData().observe(this, Observer {
            if (showProgress.value != true){
                barcode.postValue(it)
            }
        })

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.documentsFragment) {
                //ToDo параметры ТСД из настроек при уходе с экрана настроек
                barcodeHelper = BarcodeHelper(this, BarcodeHelper.TYPE_TSD.ATOL_SMART_DROID)
                barcodeHelper.getBarcodeLiveData().observe(this, Observer {
                    barcode.postValue(it)
                })
            }
        }


    }

    override fun getLayout() = R.layout.activity_main
}

