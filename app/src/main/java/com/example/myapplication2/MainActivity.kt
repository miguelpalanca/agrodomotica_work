package com.example.myapplication2

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback

class MainActivity : AppCompatActivity() {

    private var currentHomeId: Long = -1

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission Granted! You can now implement scanning.", Toast.LENGTH_LONG).show()
            // TODO: The actual device scan logic would go here.
        } else {
            Toast.makeText(this, "Location permission is required to scan for devices.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scanDevicesButton = findViewById<Button>(R.id.scanDevicesButton)

        scanDevicesButton.setOnClickListener {
            if (currentHomeId == -1L) {
                Toast.makeText(this, "Please wait, preparing home...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        checkAndSetHome()
    }

    private fun checkAndSetHome() {
        ThingHomeSdk.getHomeManagerInstance().queryHomeList(object : IThingGetHomeListCallback {
            override fun onSuccess(homes: MutableList<HomeBean>?) {
                if (homes.isNullOrEmpty()) {
                    createHome()
                } else {
                    currentHomeId = homes[0].homeId
                    Log.d("MainActivity", "Home found, ID: $currentHomeId")
                    Toast.makeText(this@MainActivity, "Home ready!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                Log.e("MainActivity", "Failed to get home list: $errorMsg")
                Toast.makeText(this@MainActivity, "Error getting home: $errorMsg", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createHome() {
        ThingHomeSdk.getHomeManagerInstance().createHome("My First Home", 0.0, 0.0, "", emptyList(), object : IThingHomeResultCallback {
            override fun onSuccess(home: HomeBean?) {
                if (home != null) {
                    currentHomeId = home.homeId
                    Log.d("MainActivity", "Home created, ID: $currentHomeId")
                    Toast.makeText(this@MainActivity, "New home created!", Toast.LENGTH_SHORT).show()
                } else {
                     Log.e("MainActivity", "Failed to create home, bean is null")
                }
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                Log.e("MainActivity", "Failed to create home: $errorMsg")
                 Toast.makeText(this@MainActivity, "Error creating home: $errorMsg", Toast.LENGTH_LONG).show()
            }
        })
    }
}