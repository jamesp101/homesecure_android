package com.example.homesecure.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homesecure.R
import com.example.homesecure.lists.Device

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


        setupRecycler(root)

        return root
    }

    // SECTION: setupRecycler
    private fun setupRecycler(view: View){
        for(i in 1..5) {
            Device.addDevice(Device())
        }

//        val viewManager = GridLayoutManager(activity,2)
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
    // END SECTION: setupRecycler
}