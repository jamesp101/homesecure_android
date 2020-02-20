package com.example.homesecure.lists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.homesecure.*
import com.example.homesecure.MainActivity.Companion.userId
import com.example.homesecure.ui.home.DeviceRequest
import com.example.homesecure.ui.home.DevicesJSON
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_device_info.*
import java.lang.Exception


class DeviceActivity : AppCompatActivity() {

    private lateinit var  fragmentManager: FragmentManager


    private lateinit var deviceInfoTab: DeviceInfoFragment
    private lateinit var timeLineTab: TimelineFragment
    private lateinit var transaction: FragmentTransaction
    private lateinit var settingsTab: SettingsFragment
    private lateinit var id: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)


        fragmentManager = supportFragmentManager

        deviceInfoTab = DeviceInfoFragment()
        timeLineTab = TimelineFragment()
        settingsTab = SettingsFragment()


        setTab(deviceInfoTab)

        tablayoutOnClick()
        setActionBar()
        try{
            id=intent.extras.get("id").toString()
        }catch (e: Exception){

        }
        request()
    }

    private fun request(){
        Fuel.get("${Api.host}/devce?id=${id}")
            .responseString { request, response, result ->
                Log.d("POST REQUEST: " , "$request \n $response \n $result")

                val mapper = ObjectMapper().registerModule(KotlinModule())
                val state: DeviceReq = mapper.readValue(result.get())

                deviceInfoTab.deviceName(state.device.alias, state.device.name)
            }


    }



    private fun setTab(fragment: Fragment){
        transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameDevice, fragment, "Info Tab")
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setActionBar(){
         supportActionBar?.also {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "Devices"
        }
    }

    private fun tablayoutOnClick(){
        val tablayout = findViewById<TabLayout>(R.id.tabLayout)
        tablayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                setTab(when(p0?.position){
                    0-> deviceInfoTab
                    1-> timeLineTab
                    2-> settingsTab
                    else -> deviceInfoTab
                })
                request()

            }

        })

    }





}




data class DeviceReq(var code: String,
                     var description: String,
                     var device: DevicesJSON);

