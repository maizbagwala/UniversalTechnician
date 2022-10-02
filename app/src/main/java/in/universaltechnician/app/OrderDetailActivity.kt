package `in`.universaltechnician.app

import `in`.universaltechnician.app.model.OrderResponse
import `in`.universaltechnician.app.utils.Const
import `in`.universaltechnician.app.utils.Functions.checkPermission
import `in`.universaltechnician.app.utils.Functions.makeACall
import `in`.universaltechnician.app.utils.Functions.myDialog
import `in`.universaltechnician.app.viewmodel.OrderViewModel
import `in`.universaltechnician.app.viewmodel.PriceViewModel
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var carouselView: CarouselView
    lateinit var viewModel: PriceViewModel
    lateinit var orderViewModel: OrderViewModel
    lateinit var dialog: Dialog


    private lateinit var tvGas: TextView
    private lateinit var tvRo: TextView
    private lateinit var tvGeezer: TextView
    private lateinit var tvAc: TextView
    private lateinit var tvFlourMill: TextView
    private lateinit var tvRefrigerator: TextView
    private lateinit var tvWashingMachine: TextView
    private lateinit var tvMicrowave: TextView
    private lateinit var tvChimney: TextView

    private lateinit var tvGasPrice: TextView
    private lateinit var tvRoPrice: TextView
    private lateinit var tvGeezerPrice: TextView
    private lateinit var tvAcPrice: TextView
    private lateinit var tvFlourMillPrice: TextView
    private lateinit var tvRefrigeratorPrice: TextView
    private lateinit var tvWashingMachinePrice: TextView
    private lateinit var tvMicrowavePrice: TextView
    private lateinit var tvChimneyPrice: TextView

    private lateinit var tvOrderStatus: TextView
    private lateinit var tvOrderType: TextView
    private lateinit var tvTotal: TextView

    private lateinit var tvOrderTime: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvPhone: TextView

    private lateinit var tvPaymentStatus: TextView
    private lateinit var btnPay: TextView

    private lateinit var layoutBtn: LinearLayout
    private lateinit var btnCompletePayment: TextView
    private lateinit var btnOrderComplete: TextView

    private var orderId: String = ""

    private var tvList = arrayListOf<TextView>()

    private var imageList = arrayListOf<String>()

    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        viewModel = ViewModelProvider(this)[PriceViewModel::class.java]
        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        sp = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)

        carouselView = findViewById(R.id.carouselView)
        tvGas = findViewById(R.id.gasTv)
        tvRo = findViewById(R.id.roTv)
        tvGeezer = findViewById(R.id.geezerTv)
        tvAc = findViewById(R.id.acTv)
        tvFlourMill = findViewById(R.id.flourMillTv)
        tvRefrigerator = findViewById(R.id.refrigeratorTv)
        tvWashingMachine = findViewById(R.id.washingMachineTv)
        tvMicrowave = findViewById(R.id.microwaveTv)
        tvChimney = findViewById(R.id.chimneyTv)

        tvGasPrice = findViewById(R.id.gasTv_price)
        tvRoPrice = findViewById(R.id.roTv_price)
        tvGeezerPrice = findViewById(R.id.geezerTv_price)
        tvAcPrice = findViewById(R.id.acTv_price)
        tvFlourMillPrice = findViewById(R.id.flourMillTv_price)
        tvRefrigeratorPrice = findViewById(R.id.refrigeratorTv_price)
        tvWashingMachinePrice = findViewById(R.id.washingMachineTv_price)
        tvMicrowavePrice = findViewById(R.id.microwaveTv_price)
        tvChimneyPrice = findViewById(R.id.chimneyTv_price)

        tvOrderStatus = findViewById(R.id.tv_order_status)
        tvOrderType = findViewById(R.id.tv_order_type)
        tvTotal = findViewById(R.id.totalTv)

        tvOrderTime = findViewById(R.id.tvOrderTime)
        tvAddress = findViewById(R.id.tvAddress)
        tvPhone = findViewById(R.id.tvPhone)

        tvPaymentStatus = findViewById(R.id.tvPaymentStatus)
        btnPay = findViewById(R.id.btn_pay)

        layoutBtn = findViewById(R.id.layout_btn)
        btnCompletePayment = findViewById(R.id.btn_complete_payment)
        btnOrderComplete = findViewById(R.id.btn_complete_order)

        tvList.add(tvGasPrice)
        tvList.add(tvRoPrice)
        tvList.add(tvGeezerPrice)
        tvList.add(tvAcPrice)
        tvList.add(tvFlourMillPrice)
        tvList.add(tvRefrigeratorPrice)
        tvList.add(tvWashingMachinePrice)
        tvList.add(tvMicrowavePrice)
        tvList.add(tvChimneyPrice)


        dialog = this.myDialog()


        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            onBackPressed()
        }
        btnPay.setOnClickListener { startActivity(Intent(this, PaymentDetailActivity::class.java)) }
        layoutBtn.isVisible = sp.getBoolean(Const.IS_ADMIN, false)

        btnCompletePayment.setOnClickListener { completePayment() }
        btnOrderComplete.setOnClickListener { orderComplete() }

        if (intent.extras != null) {
            val data = intent.getSerializableExtra("model") as OrderResponse.Data
            getPrices(data)
            orderId = data.oid
            setCarousel(data.order_image)
            when (data.order_status) {
                "0" -> {
                    tvOrderStatus.text = "Pending"
                    tvOrderStatus.setBackgroundResource(R.color.yellow)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
                "1" -> {
                    tvOrderStatus.text = "Complete"
                    tvOrderStatus.setBackgroundResource(R.color.green)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(this, R.color.black))
                }
                "2" -> {
                    tvOrderStatus.text = "Cancel"
                    tvOrderStatus.setBackgroundResource(R.color.red)
                    tvOrderStatus.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
            tvOrderType.text =
                if (data.order_type == "0") "Order Type: Single Time" else if (data.order_type == "1") "Order Type: One Year" else ""
            tvGas.isVisible = data.stove_type != "-1"
            tvRo.isVisible = data.ro_waterfilter_type != "-1"
            tvGeezer.isVisible = data.geezer_type != "-1"
            tvAc.isVisible = data.ac_type != "-1"
            tvFlourMill.isVisible = data.homemill != "-1"
            tvRefrigerator.isVisible = data.refrigerator != "-1"
            tvWashingMachine.isVisible = data.washing_machine != "-1"
            tvMicrowave.isVisible = data.microwave != "-1"
            tvChimney.isVisible = data.chimney != "-1"

            if (data.order_type == "0") {
                tvGas.text = when (data.stove_type) {
                    "1" -> "Gas Stove"
                    else -> {
                        ""
                    }
                }
                tvRo.text = when (data.ro_waterfilter_type) {
                    "1" -> "RO/Water Filter"
                    else -> {
                        ""
                    }
                }
                tvGeezer.text = when (data.geezer_type) {
                    "1" -> "Geezer"
                    else -> {
                        ""
                    }
                }
                tvAc.text = when (data.ac_type) {
                    "1" -> "AC"
                    else -> {
                        ""
                    }
                }
            } else if (data.order_type == "1") {
                tvGas.text = when (data.stove_type) {
                    "1" -> "1 Burner Stove"
                    "2" -> "2 Burner Stove"
                    "3" -> "3 Burner Stove"
                    "4" -> "Burner Hob"
                    else -> {
                        ""
                    }
                }
                tvRo.text = when (data.ro_waterfilter_type) {
                    "1" -> "R.O + UV + UF + Mineral"
                    "2" -> "Water Filter + UV/UF"
                    else -> {
                        ""
                    }
                }
                tvGeezer.text = when (data.geezer_type) {
                    "1" -> "Electrical Geezer 3 To 6 Ltr"
                    "2" -> "Electrical Geezer 10 To 15 Ltr"
                    "3" -> "Electrical Geezer 20 To 25 Ltr"
                    "4" -> "Electrical Geezer 50 To 100 Ltr"
                    "5" -> "Solar Geezer"
                    "6" -> "Gas Geezer"
                    else -> {
                        ""
                    }
                }
                tvAc.text = when (data.ac_type) {
                    "1" -> "1 AC"
                    "2" -> "2 AC"
                    "3" -> "3 AC"
                    "4" -> "4 AC"
                    else -> {
                        ""
                    }
                }
            }
            tvFlourMill.text = when (data.homemill) {
                "1" -> "Flour Mill"
                else -> {
                    ""
                }
            }
            tvRefrigerator.text = when (data.refrigerator) {
                "1" -> "Refrigerator"
                else -> {
                    ""
                }
            }
            tvWashingMachine.text = when (data.washing_machine) {
                "1" -> "Washing Machine"
                else -> {
                    ""
                }
            }
            tvMicrowave.text = when (data.microwave) {
                "1" -> "Microwave"
                else -> {
                    ""
                }
            }
            tvChimney.text = when (data.chimney) {
                "1" -> "Chimney"
                else -> {
                    ""
                }
            }

            tvOrderTime.text = "Order Time : ${data.order_time}"
            tvAddress.text = "Address : ${data.address}"
            findViewById<TextView>(R.id.tvAddressBtn).isVisible =
                sp.getBoolean(Const.IS_ADMIN, false)
            findViewById<TextView>(R.id.tvAddressBtn).setOnClickListener {
                val uri: String =
                    java.lang.String.format(Locale.ENGLISH, "geo:0,0?q=${data.address}")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(intent)
            }
            tvPhone.text = "Phone : ${data.phone}"
            if (sp.getBoolean(Const.IS_ADMIN, false)) {

                tvPhone.setOnClickListener {
                    if (this.checkPermission(Manifest.permission.CALL_PHONE)) {
                        this.makeACall(data.phone)
                    } else {
                        Toast.makeText(this, "Grant Permission For Call", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            tvPaymentStatus.text = when (data.payment_type) {
                "0" -> "Payment Status : Incomplete"
                "1" -> "Payment Status : Completed"
                else -> ""
            }
            if (!sp.getBoolean(Const.IS_ADMIN, false)) {
                btnPay.isVisible = data.payment_type == "0"
            }

            btnCompletePayment.isVisible =
                sp.getBoolean(Const.IS_ADMIN, false) && data.payment_type == "0"

            btnOrderComplete.isVisible =
                sp.getBoolean(Const.IS_ADMIN, false) && data.order_status == "0"

        }
    }

    private fun openDialog(url: String) {
        val dialog = Dialog(this, R.style.ThemeOverlay_MaterialComponents)
        dialog.setContentView(R.layout.fullscreen_image)
        val photoview = dialog.findViewById<PhotoView>(R.id.photo_view)
        val close = dialog.findViewById<ImageView>(R.id.iv_close)
        close.setOnClickListener { dialog.dismiss() }
        Glide.with(this).load(url).into(photoview)
        dialog.show()
    }

    private fun getPrices(data: OrderResponse.Data) {
        dialog.show()
        viewModel.getPrice()?.observe(this) {
            dialog.dismiss()
            if (it.success) {
                if (!it.data.isNullOrEmpty()) {

                    if (data.order_type == "1") {
                        if (tvGas.isVisible) {
                            tvGasPrice.text = when (data.stove_type) {
                                "1" -> it.data[0].price
                                "2" -> it.data[1].price
                                "3" -> it.data[2].price
                                "4" -> it.data[3].price
                                else -> {
                                    ""
                                }
                            }
                        } else tvGasPrice.isVisible = false
                        if (tvRo.isVisible) {
                            tvRoPrice.text = when (data.ro_waterfilter_type) {
                                "1" -> it.data[4].price
                                "2" -> it.data[5].price
                                else -> {
                                    ""
                                }
                            }
                        } else tvRoPrice.isVisible = false
                        if (tvGeezer.isVisible) {
                            tvGeezerPrice.text = when (data.geezer_type) {
                                "1" -> it.data[6].price
                                "2" -> it.data[18].price
                                "3" -> it.data[19].price
                                "4" -> it.data[20].price
                                "5" -> it.data[7].price
                                "6" -> it.data[8].price
                                else -> {
                                    ""
                                }
                            }
                        } else tvGeezerPrice.isVisible = false
                        if (tvAc.isVisible) {
                            tvAcPrice.text = when (data.ac_type) {
                                "1" -> it.data[9].price
                                "2" -> it.data[10].price
                                "3" -> it.data[11].price
                                "4" -> it.data[12].price
                                else -> {
                                    ""
                                }
                            }
                        } else tvAcPrice.isVisible = false
                        if (tvFlourMill.isVisible) {
                            tvFlourMillPrice.text = it.data[13].price
                        } else tvFlourMillPrice.isVisible = false
                        if (tvRefrigerator.isVisible) {
                            tvRefrigeratorPrice.text = it.data[14].price
                        } else tvRefrigeratorPrice.isVisible = false
                        if (tvWashingMachine.isVisible) {
                            tvWashingMachinePrice.text = it.data[15].price
                        } else tvWashingMachinePrice.isVisible = false
                        if (tvMicrowave.isVisible) {
                            tvMicrowavePrice.text = it.data[16].price
                        } else tvMicrowavePrice.isVisible = false
                        if (tvChimney.isVisible) {
                            tvChimneyPrice.text = it.data[17].price
                        } else tvChimneyPrice.isVisible = false
                        tvTotal.text = getTotalPrice(tvList).toString()
                    } else {
                        if (tvGas.isVisible) {
                            tvGasPrice.text = "-"
                        } else tvGasPrice.isVisible = false
                        if (tvRo.isVisible) {
                            tvRoPrice.text = "-"
                        } else tvRoPrice.isVisible = false
                        if (tvGeezer.isVisible) {
                            tvGeezerPrice.text = "-"
                        } else tvGeezerPrice.isVisible = false
                        if (tvAc.isVisible) {
                            tvAcPrice.text = "-"
                        } else tvAcPrice.isVisible = false
                        if (tvFlourMill.isVisible) {
                            tvFlourMillPrice.text = "-"
                        } else tvFlourMillPrice.isVisible = false
                        if (tvRefrigerator.isVisible) {
                            tvRefrigeratorPrice.text = "-"
                        } else tvRefrigeratorPrice.isVisible = false
                        if (tvWashingMachine.isVisible) {
                            tvWashingMachinePrice.text = "-"
                        } else tvWashingMachinePrice.isVisible = false
                        if (tvMicrowave.isVisible) {
                            tvMicrowavePrice.text = "-"
                        } else tvMicrowavePrice.isVisible = false
                        if (tvChimney.isVisible) {
                            tvChimneyPrice.text = "-"
                        } else tvChimneyPrice.isVisible = false
                        tvTotal.textSize = 14.0F
                        tvTotal.text = "Min charge 200 + as per service"
                    }

                }
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTotalPrice(tvList: ArrayList<TextView>): Int {
        var total = 0
        for (tv in tvList) {
            if (tv.isVisible) {
                total += tv.text.toString().toInt()
            }
        }
        return total
    }

    private fun completePayment() {
        dialog.show()
        orderViewModel.completePayment(orderId)?.observe(this) {
            dialog.dismiss()
            if (it.success) {
                onBackPressed()
                Toast.makeText(this, "Payment is Completed", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setCarousel(orderImage: String) {
        val arr = orderImage.replace("[", "").replace(" ", "").split(",")
        imageList.clear()
        imageList.addAll(arr)
        carouselView.apply {
            size = imageList.size
            resource = R.layout.image_carousel_item
            autoPlay = false
            indicatorAnimationType = IndicatorAnimationType.THIN_WORM
            carouselOffset = OffsetType.CENTER
            setCarouselViewListener { view, position ->
                val imageView = view.findViewById<ImageView>(R.id.iv)
                imageView.setOnClickListener {
                    openDialog(imageList[position])
                }
                Glide.with(this).load(imageList[position]).into(imageView)
            }
            // After you finish setting up, show the CarouselView
            show()
        }
    }

    private fun orderComplete() {
        dialog.show()
        orderViewModel.completeOrder(orderId)?.observe(this) {
            dialog.dismiss()
            if (it.success) {
                onBackPressed()
                Toast.makeText(this, "This order is Completed", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }

}