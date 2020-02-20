package com.example.homesecure.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homesecure.Api
import com.example.homesecure.LoginActivity
import com.example.homesecure.MainActivity
import com.example.homesecure.R
import com.example.homesecure.lists.Device
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            //            textView.text = it
        })


        request(root)

        return root
    }

    // SECTION: setupRecycler
    private fun setupRecycler(view: View, devices: List<DevicesJSON>?){
        Device.getDevices()?.clear()

        devices?.forEach {
            Device.addDevice( Device(
                deviceId = it.id.toString(),
                deviceName = it.alias
            ))
        }

        val viewManager = LinearLayoutManager(activity)
        val viewAdapter = Device.getAdapter(context = view.context)

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerMainDevices)
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager=viewManager
        }


        Log.i("No of Devices : " , "${Device.getDevices()}")
    }

    private fun request(view: View){
        val userId: String = MainActivity.userId;
        Fuel.get("${Api.host}/devices?user=${userId}")
            .responseString { request, response, result ->
                Log.d("POST REQUEST: " , "$request \n $response \n $result")

                textView3.text = MainActivity.email
                textView2.text = MainActivity.username

                val mapper = ObjectMapper().registerModule(KotlinModule())
                val state: DeviceRequest = mapper.readValue(result.get())

                Log.d("state", state.toString())

                MainActivity.mnActivity.runOnUiThread{
                    setupRecycler(view, state.device)
                    textView4.text = state.device?.count().toString()
                }


            }
    }
    // END SECTION: setupRecycler
}

data class DeviceRequest(var code: String,
                         var description: String,
                         var device: List<DevicesJSON>?)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class DevicesJSON(var id: Int,
                       var name: String,
                       var alias: String,
                       var deviceId: String,
                       var streamUrl: String,
                       var status: Int,
                       var userId: Int,
                       @JsonProperty("createdAt")
                       var createdAt: Any = "",
                       @JsonProperty("updatedAt")
                       var updatedAt: Any = "")
