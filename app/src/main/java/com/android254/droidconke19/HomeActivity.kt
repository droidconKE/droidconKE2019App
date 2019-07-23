package com.android254.droidconke19

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.android254.droidconke19.utils.SharedPref.TOKEN_SENT
import com.android254.droidconke19.utils.isSignedIn
import com.android254.droidconke19.utils.toast
import com.android254.droidconke19.viewmodels.HomeViewModel
import com.android254.droidconke19.viewmodels.SessionDetailsViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeActivity : AppCompatActivity() {
    val sharedPreferences: SharedPreferences by inject { parametersOf(this) }
    private val firebaseRemoteConfig: FirebaseRemoteConfig by inject()
    private val firebaseMessaging: FirebaseMessaging by inject()
    private val firebaseAuth: FirebaseAuth by inject()
    private lateinit var params: AppBarLayout.LayoutParams
    private val homeViewModel: HomeViewModel by viewModel()
    private val sessionDetailsViewModel: SessionDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)


        setupNavigation()
//        setupActionBarWithNavController(findNavController(R.id.navHostFragment), drawer_layout)

        setupNotifications()

        //observe live data emitted by view model
        observeLiveData()

    }

    private fun setupNotifications() {
        firebaseMessaging.subscribeToTopic("all")

        if (BuildConfig.DEBUG) {
            firebaseMessaging.subscribeToTopic("debug")
        }
    }

    private fun setupNavigation() {
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        // Update action bar to reflect navigation
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        NavigationUI.setupWithNavController(nav_view, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.sessionDetailsFragment -> {
                    toolbar.visibility = View.GONE
                }
                else -> toolbar.visibility = View.VISIBLE
            }
        }

    }

    private fun observeLiveData() {
        homeViewModel.getUpdateTokenResponse().observe(this, Observer {
            when {
                it -> {
                    sharedPreferences.edit().putInt(TOKEN_SENT, 1).apply()
                }
                else -> {
                    sharedPreferences.edit().putInt(TOKEN_SENT, 0).apply()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                if (!firebaseAuth.isSignedIn()) {
                    findNavController(R.id.navHostFragment).navigate(R.id.signInDialogFragment)
                } else {
                    AlertDialog.Builder(this@HomeActivity)
                            .setMessage("Do you want to logout?")
                            .setPositiveButton("Yes") { _, _ ->
                                unsubscribeNotifications()
                                firebaseAuth.signOut()
                                toast("Success")
                            }.setNegativeButton("No", null)
                            .show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun unsubscribeNotifications() = lifecycleScope.launch {
        firebaseMessaging.unsubscribeFromTopic("all").await()
        if (BuildConfig.DEBUG) {
            firebaseMessaging.unsubscribeFromTopic("debug").await()
        }
        sessionDetailsViewModel.removeAllFavourites(sharedPreferences)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onSupportNavigateUp() = NavigationUI.navigateUp(findNavController(R.id.navHostFragment), drawer_layout)

    companion object {
        var navItemIndex = 1 //controls toolbar titles and icons
        var fabVisible = true
    }
}
