package `fun`.gladkikh.fastpallet5.ui.fragment

import `fun`.gladkikh.fastpallet5.ui.activity.HostActivity
import `fun`.gladkikh.fastpallet5.ui.activity.MainActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController


abstract class BaseFragment : Fragment() {
    private lateinit var navController: NavController


    lateinit var hostActivity: HostActivity



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hostActivity = activity as MainActivity
        navController = (activity as MainActivity).getHostNavController()
        initSubscription()
    }

    abstract fun getLayout(): Int
    protected open fun initSubscription(){}
}