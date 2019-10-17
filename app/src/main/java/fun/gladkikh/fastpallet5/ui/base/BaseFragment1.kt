package `fun`.gladkikh.fastpallet5.ui.base

import `fun`.gladkikh.fastpallet5.ui.activity.HostActivity
import `fun`.gladkikh.fastpallet5.ui.activity.MainActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

abstract class BaseFragment1 : Fragment() {

    abstract val viewModel: BaseViewModel1
    abstract val layoutRes: Int

    private lateinit var navController: NavController
    lateinit var hostActivity: HostActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hostActivity = activity as MainActivity
        navController = (activity as MainActivity).getNavController()

        initSubscription()

    }

    protected open fun initSubscription() {
        viewModel.message.observe(viewLifecycleOwner, Observer {
            hostActivity.showMessage(it)
        })

        viewModel.messageError.observe(viewLifecycleOwner, Observer {
            hostActivity.showErrorMessage(it)
        })

        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if (it) {
                hostActivity.showProgress()
            } else {
                hostActivity.hideProgress()
            }
        })

    }

}