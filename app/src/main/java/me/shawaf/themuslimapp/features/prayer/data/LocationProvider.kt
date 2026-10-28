package me.shawaf.themuslimapp.features.prayer.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/** Default fallback location (Cairo) used when permission is denied or no fix is available. */
val DEFAULT_LOCATION = GeoPoint(30.0444, 31.2357, "Cairo")

data class GeoPoint(val latitude: Double, val longitude: Double, val label: String = "")

class LocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    /** Returns the last known location, or null if unavailable / not permitted. */
    @SuppressLint("MissingPermission")
    suspend fun lastKnownLocation(): GeoPoint? {
        if (!hasLocationPermission()) return null
        val client = LocationServices.getFusedLocationProviderClient(context)
        return suspendCancellableCoroutine { cont ->
            client.lastLocation
                .addOnSuccessListener { location ->
                    cont.resume(
                        location?.let { GeoPoint(it.latitude, it.longitude) },
                    )
                }
                .addOnFailureListener { cont.resume(null) }
        }
    }
}
