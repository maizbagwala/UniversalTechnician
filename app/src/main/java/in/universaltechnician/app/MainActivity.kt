package `in`.universaltechnician.app

import `in`.universaltechnician.app.adapter.OrderAdapter
import `in`.universaltechnician.app.model.Order
import `in`.universaltechnician.app.model.OrderResponse
import `in`.universaltechnician.app.utils.Const
import `in`.universaltechnician.app.utils.Functions.myDialog
import `in`.universaltechnician.app.viewmodel.OrderViewModel
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var tvName: TextView
    lateinit var btnGetService: TextView
    lateinit var btnSetting: ImageView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var rvOrder: RecyclerView
    lateinit var orderAdapter: OrderAdapter
    lateinit var btnAll: TextView
    lateinit var btnComplete: TextView
    lateinit var btnPending: TextView
    private val orderList = arrayListOf<OrderResponse.Data>()

    lateinit var viewModel: OrderViewModel
    lateinit var dialog: Dialog
    lateinit var noData: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = this.myDialog()
        noData = findViewById(R.id.no_data)
        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        tvName = findViewById(R.id.tv_user_name)
        btnSetting = findViewById(R.id.iv_setting)
        btnGetService = findViewById(R.id.tv_get_service)
        rvOrder = findViewById(R.id.rv_orders)

        orderAdapter = OrderAdapter(orderList)
        rvOrder.layoutManager = LinearLayoutManager(this)
        rvOrder.adapter = orderAdapter

        btnAll = findViewById(R.id.btn_all)
        btnComplete = findViewById(R.id.btn_completed)
        btnPending = findViewById(R.id.btn_pending)

        btnAll.setOnClickListener {
            btnAll.setBackgroundResource(R.color.my_red)
            btnComplete.setBackgroundResource(R.color.white)
            btnPending.setBackgroundResource(R.color.white)
            btnAll.setTextColor(ContextCompat.getColor(this, R.color.white))
            btnComplete.setTextColor(ContextCompat.getColor(this, R.color.black))
            btnPending.setTextColor(ContextCompat.getColor(this, R.color.black))
            getOrders()
        }
        btnComplete.setOnClickListener {
            btnAll.setBackgroundResource(R.color.white)
            btnComplete.setBackgroundResource(R.color.my_red)
            btnPending.setBackgroundResource(R.color.white)
            btnAll.setTextColor(ContextCompat.getColor(this, R.color.black))
            btnComplete.setTextColor(ContextCompat.getColor(this, R.color.white))
            btnPending.setTextColor(ContextCompat.getColor(this, R.color.black))
            getOrders("completed")
        }
        btnPending.setOnClickListener {
            btnAll.setBackgroundResource(R.color.white)
            btnComplete.setBackgroundResource(R.color.white)
            btnPending.setBackgroundResource(R.color.my_red)
            btnAll.setTextColor(ContextCompat.getColor(this, R.color.black))
            btnComplete.setTextColor(ContextCompat.getColor(this, R.color.black))
            btnPending.setTextColor(ContextCompat.getColor(this, R.color.white))
            getOrders("pending")
        }
        sharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)
        if (!sharedPreferences.getString(Const.USER_NAME, "").equals("")) {
            tvName.text = "\uD83D\uDC4B Hi, ${sharedPreferences.getString(Const.USER_NAME, "")}"
        }

        btnSetting.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
        }

        btnGetService.setOnClickListener {
            showDialogForService()
//            startActivity(Intent(this@MainActivity, ServiceActivity::class.java))
        }
    }

    private fun showDialogForService() {
        val d = Dialog(this,R.style.myfullscreendialog)
        d.setContentView(R.layout.dialog_service_selection)
        d.findViewById<TextView>(R.id.tv_one_time).setOnClickListener {
            d.dismiss()
            startActivity(Intent(this, SingleServiceActivity::class.java))
        }
        d.findViewById<TextView>(R.id.tv_contract_time).setOnClickListener {
            d.dismiss()
            startActivity(Intent(this, ServiceActivity::class.java))
        }
        d.create()
        d.show()
    }

    override fun onResume() {
        super.onResume()
        getOrders()
    }

    private fun getOrders(type: String = "all") {
        dialog.show()
        viewModel.getUserOrders(sharedPreferences.getInt(Const.USER_ID, -1).toString())
            ?.observe(this) {
                dialog.dismiss()
                if (it.success) {
                    if (!it.data.isNullOrEmpty()) {
                        orderList.clear()
                        for (i in it.data) {
                            if (type == "all") {
                                orderList.add(i)
                            } else if (type == "pending") {
                                if (i.order_status == "0") {
                                    orderList.add(i)
                                }
                            } else {
                                if (i.order_status == "1") {
                                    orderList.add(i)
                                }
                            }
                        }
                        orderAdapter.notifyDataSetChanged()
                        noData.isVisible = false
                    } else {
                        noData.isVisible = true
                    }
                } else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }

}