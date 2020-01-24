package com.android254.droidconke19.ui.venuemap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android254.droidconke19.BuildConfig
import com.android254.droidconke19.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.map_bottom_sheet.*
import kotlinx.android.synthetic.main.map_bottom_sheet.view.*
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.toast

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {
    private val senteuPlaza = LatLng(-1.289256, 36.783180)
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    lateinit var locationRequest: LocationRequest
    internal var currentLatLng: LatLng? = null
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private val settingsClient: SettingsClient by lazy {
        LocationServices.getSettingsClient(requireActivity())
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                currentLatLng = LatLng(location.latitude, location.longitude)
            }
        }

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when (PackageManager.PERMISSION_GRANTED) {
                grantResults[0] -> {
                    startLocationUpdates()

                }
                else -> {
                    // Permission denied.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null)
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }

        }
    }

    private fun startLocationUpdates() {
        lifecycleScope.launchWhenStarted {
            try {
                settingsClient.checkLocationSettings(locationSettingsRequest).await()
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                        locationCallback, Looper.myLooper())

            } catch (exception: Exception) {
                when ((exception as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            val rae = exception as ResolvableApiException
                            rae.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS)
                        } catch (sie: IntentSender.SendIntentException) {
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                        activity?.toast(errorMessage)

                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //show toolbar if its hidden
        (activity as AppCompatActivity).supportActionBar?.show()
        //bottom sheet view
        val bottomSheetView = view.bottomSheetView
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)

        clickListeners()
        createLocationRequests()
        createLocationSettingsRequest()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    private fun clickListeners() {
        //collapse bottom sheet
        collapseBottomSheetImg.setOnClickListener {
            when (bottomSheetBehavior?.state) {
                BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        //open google maps intent to get directions
        googleDirectionsBtn.setOnClickListener {
            when {
                currentLatLng != null -> {
                    val uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + currentLatLng!!.latitude + "," + currentLatLng!!.longitude + "&daddr=" + senteuPlaza.latitude + "," + senteuPlaza.longitude
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(Intent.createChooser(intent, "Open with"))
                }
                else -> requireActivity().toast(getString(R.string.problem_getting_location))


            }
        }
    }

    private fun createLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()
    }

    private fun createLocationRequests() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 1800000 // 30 minute interval
        locationRequest.fastestInterval = 1200000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap?.apply {
            addMarker(MarkerOptions().position(senteuPlaza))
            animateCamera(CameraUpdateFactory.newLatLngZoom(senteuPlaza, DEFAULT_ZOOM.toFloat()))
            setOnMarkerClickListener {
                when {
                    bottomSheetBehavior?.state != BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
                    else -> bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }
                true
            }
        }
        receiveLocationUpdates()
    }


    private fun receiveLocationUpdates() {
        when {
            checkPermissions() -> startLocationUpdates()
            !checkPermissions() -> requestPermissions()
        }

    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE)

        }
    }


    @SuppressLint("MissingPermission")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        receiveLocationUpdates()
                    }
                    AppCompatActivity.RESULT_CANCELED -> {
                        requireActivity().toast(getString(R.string.permission_denied))
                    }
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_profile)?.isVisible = false
        menu.findItem(R.id.eventFeedbackFragment)?.isVisible = false
    }

    companion object {
        private const val DEFAULT_ZOOM = 17
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_CHECK_SETTINGS = 0x1
    }
}
