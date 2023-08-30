package com.example.bottomnavigation

import AlertDialogHelper
import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient

class PermissionsHelper {
    lateinit var fragment: Fragment
    lateinit var launcher: ActivityResultLauncher<Array<String>>

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestLocation(fragment: Fragment) {
        this.fragment = fragment
        this.launcher = registerPermissionsLauncherCallback()
        if (!hasFineLocationPermission() || !hasStorageWritePermission()) {
            requestPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun registerPermissionsLauncherCallback(): ActivityResultLauncher<Array<String>> {
        return fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissions() {
        val showRationaleFineloc =
            fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        val showRationaleWriteStorage =
            fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (showRationaleFineloc || showRationaleWriteStorage) {
            showPermissionNeededAlert(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } else {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun showPermissionNeededAlert(permissions: Array<String>) {
        val dialog = AlertDialogHelper(fragment.requireContext()).create(
            ("Permissions needed:"),
            ("Device location"),
            false
        )
            .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                launcher.launch(permissions)
            }.create()
        dialog.show()
    }


    private fun hasFineLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasStorageWritePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


}