package com.example.homesecure.lists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.homesecure.DeviceInfoFragment
import com.example.homesecure.R
import com.example.homesecure.SettingsFragment
import com.example.homesecure.TimelineFragment
import com.google.android.material.tabs.TabLayout


class DeviceActivity : AppCompatActivity() {

    private lateinit var  fragmentManager: FragmentManager


    private lateinit var deviceInfoTab: DeviceInfoFragment
    private lateinit var timeLineTab: TimelineFragment
    private lateinit var transaction: FragmentTransaction
    private lateinit var settingsTab: SettingsFragment

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

            }

        })

    }





}





