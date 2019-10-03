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
import androidx.navigation.NavController

abstract class BaseFragment<T, S : BaseViewState<T>> : Fragment() {

    abstract val viewModel: BaseViewModel<T, S>
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

        viewModel.getViewState().observe(viewLifecycleOwner, Observer<S> {
            if (it == null) return@Observer
            if (it.error != null) {
                renderError(it.error)
                return@Observer
            }
            renderData(it.data)
        })

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

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable) {
        error.message?.let { showError(it) }
    }

    protected fun showError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}