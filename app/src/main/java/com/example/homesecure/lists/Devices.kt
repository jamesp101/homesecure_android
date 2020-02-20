package com.example.homesecure.lists

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.homesecure.R
import kotlinx.android.synthetic.main.layout_device_list_item.view.*

public class Device(
    deviceName: String = "",
    deviceId: String = "",
    deviceStatus: DeviceStatus = DeviceStatus.offline,
    deviceImage: ImageView? = null
) {
    // SECTION: Getters/Setters
    private  var deviceName: String = ""
        get() = field
        set(value){field=value}
    private var deviceId: String = ""
        get() = field
    private  var deviceStatus: DeviceStatus = DeviceStatus.offline
        get() = field
        set(value){field=value}

    private var deviceImage: ImageView? = null
        get() = field
    // END SECTION: Getters/Setters

    init{
        this.deviceName = deviceName
        this.deviceId = deviceId
        this.deviceStatus = deviceStatus
        this.deviceImage = deviceImage
    }

    // SECTION: Companion Object
    companion object{

        private var deviceList: ArrayList<Device> = ArrayList()
            get() = field
            set(value) {field=value}


        fun getDevices() : ArrayList<Device>? {
            return deviceList
        }
        fun addDevice(device: Device){
            Log.i("Device", "Added Device \n" +
                    "ID ${device.deviceId}" + "\n" +
                    "Name ${device.deviceImage}" +"\n" +
                    "Status ${device.deviceStatus}")
            deviceList?.add(device)
        }
        fun getAdapter(context: Context,
                       adapter: Adapter = Adapter(deviceList,context)
        ): Adapter{
            return adapter
        }
        // END SECTION: Companion Object

        // SECTION: Adapter
        class Adapter(private val myDataset: ArrayList<Device>?,
                      private val context: Context):
                    RecyclerView.Adapter<Adapter.MyViewHolder>() {

            override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
                holder.itemView.setOnClickListener{
                    val intent = Intent(context, DeviceActivity::class.java)
                    intent.putExtra("id", myDataset?.get(position)?.deviceId)
                    context.startActivity(intent)
                }
                holder?.deviceId?.text = myDataset?.get(position)?.deviceId
                holder?.deviceName?.text = myDataset?.get(position)?.deviceName
            }

            override fun getItemCount(): Int {
                Log.i("getItemCount()", "getItemCount() ${myDataset}")
                return myDataset?.size ?: 0
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_device_list_item, parent, false)

                // TODO: Bind Device info to layout Views


                return  MyViewHolder(view)
            }

            class MyViewHolder(view: View)
                : RecyclerView.ViewHolder(view){
                val deviceName = view.imageDeviceListItem
                var deviceId = view.imageDeviceListItem2
            }

        }
        // END SECTION: Adapter
    }

}



public enum class DeviceStatus {
    online, loading, offline
}
