package com.bryanherbst.fragmentstate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import timber.log.Timber

/**
 * Containts navhost that is used to navigate in-window
 * Stores all data of its child fragments including the state (both view and navigation) of
 * its inner navHostFragment
 */
class SampleHostFragment : Fragment() {
    // Mutable debugging data
    var debugInt = 0

    val SAVED_STATE: String = "SampleHostFragment_NAV_HOST_SAVED_STATE"
    val FRAGMENT_TAG : String = "SampleHostFragment_FRAGMENT_TAG"

    init {
        Timber.i("FavoritesNavHost constructor constructor, debugInt: $debugInt")
    }

    private var navHostFragment: NavHostFragment? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("Fragment onAttach childFragmentManager: $childFragmentManager in $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // here we restoring navHost... or not here?
        Timber.i("Fragment onCreate: $childFragmentManager in $this")
        //Setting to navHostFragment previous saved state
        if (savedInstanceState != null) {
            savedInstanceState.classLoader = this.javaClass.classLoader
            val state = savedInstanceState.getParcelable<Fragment.SavedState>(SAVED_STATE)
            navHostFragment = NavHostFragment.create(R.navigation.hosting_sample_navigation, savedInstanceState)
            navHostFragment!!.setInitialSavedState(state)
            childFragmentManager.beginTransaction()
                    .add(navHostFragment!!, FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
        }
    }

    // Here a fragment must be created
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.hosting_fragment_sample, container, false)

        // Finding fragment
        if (navHostFragment == null) {
            navHostFragment =
                    childFragmentManager.findFragmentById(R.id.favorites_nav_host_fragment) as NavHostFragment
        }

        Timber.i("Fragment onCreateView: $childFragmentManager in $this")
        // setupCallbacks()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*childFragmentManager.setPrimaryNavigationFragment(navHostFragment)*/
        Timber.i("In onViewCreated (navController is currently unavailable")
//        Timber.i("Nav controller: ${navHostFragment!!.findNavController()}")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("in onResume")
        childFragmentManager.beginTransaction().setPrimaryNavigationFragment(navHostFragment)
                .commit()
    }

    override fun onStart() {
        super.onStart()
        Timber.i("in onStart")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        //navHostFragment.setInitialSavedState()

        // Getting saved instance state of fragment
        val state = childFragmentManager!!.saveFragmentInstanceState(navHostFragment)
        outState.putParcelable(SAVED_STATE, state)
        Timber.i("In onSaveInstanceState of SampleHostFragment")
        super.onSaveInstanceState(outState)
        //childFragmentManager.saveFragmentInstanceState(navHostFragment!!)
    }

    override fun onPause() {
        super.onPause()
        Timber.i("in onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("in onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("Fragment childFragmentManager: $childFragmentManager")

        Timber.i("in onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("in onDestroy")
    }
}