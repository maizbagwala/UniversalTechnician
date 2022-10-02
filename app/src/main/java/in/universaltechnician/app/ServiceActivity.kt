package `in`.universaltechnician.app

import `in`.universaltechnician.app.adapter.ImageAdapter
import `in`.universaltechnician.app.model.Image
import `in`.universaltechnician.app.utils.Const
import `in`.universaltechnician.app.utils.Functions.checkPermission
import `in`.universaltechnician.app.utils.Functions.myDialog
import `in`.universaltechnician.app.viewmodel.OrderViewModel
import `in`.universaltechnician.app.viewmodel.PriceViewModel
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class ServiceActivity : AppCompatActivity() {
    private lateinit var btnTakePhoto: TextView
    private lateinit var ivPhoto: ImageView
    private val cameraCode: Int = 123

    private lateinit var tvTotal: TextView
    private lateinit var tvSubmit: TextView

    private lateinit var spGas: Spinner
    private lateinit var spRO: Spinner
    private lateinit var spGeezer: Spinner
    private lateinit var spGeezer1: Spinner
    private lateinit var spAC: Spinner

    private lateinit var gasAdapter: ArrayAdapter<String>
    private lateinit var roAdapter: ArrayAdapter<String>
    private lateinit var geezerAdapter: ArrayAdapter<String>
    private lateinit var geezer1Adapter: ArrayAdapter<String>
    private lateinit var acAdapter: ArrayAdapter<String>

    private var gasList = arrayListOf<String>()
    private var roList = arrayListOf<String>()
    private var geezerList = arrayListOf<String>()
    private var geezer1List = arrayListOf<String>()
    private var acList = arrayListOf<String>()

    private lateinit var cbGas: CheckBox
    private lateinit var cbRo: CheckBox
    private lateinit var cbGeezer: CheckBox
    private lateinit var cbAc: CheckBox
    private lateinit var cbFlour: CheckBox
    private lateinit var cbRefrigerator: CheckBox
    private lateinit var cbWashingMachine: CheckBox
    private lateinit var cbMicrowave: CheckBox
    private lateinit var cbChimney: CheckBox

    private lateinit var tvPriceGas: TextView
    private lateinit var tvPriceRo: TextView
    private lateinit var tvPriceGeezer: TextView
    private lateinit var tvPriceAc: TextView
    private lateinit var tvPriceFlourMill: TextView
    private lateinit var tvPriceFreeze: TextView
    private lateinit var tvPriceWashingMachine: TextView
    private lateinit var tvPriceMicrowave: TextView
    private lateinit var tvPriceChimney: TextView

    private lateinit var viewModel: PriceViewModel
    private lateinit var orderViewModel: OrderViewModel

    private lateinit var dialog: Dialog
    private lateinit var dialogWithText: Dialog

    private var priceGas2Burner = 0
    private var priceGas3Burner = 0
    private var priceGas4Burner = 0
    private var priceGasHob = 0
    private var priceRo = 0
    private var priceWaterFilter = 0
    private var priceGeezerElectrical3To6 = 0
    private var priceGeezerSolar = 0
    private var priceGeezerGas = 0
    private var priceAc1Pic = 0
    private var priceAc2Pic = 0
    private var priceAc3Pic = 0
    private var priceAc4Pic = 0
    private var priceFlourMill = 0
    private var priceFreeze = 0
    private var priceWashingMachine = 0
    private var priceMicrowave = 0
    private var priceChimney = 0
    private var priceGeezerElectrical10To15 = 0
    private var priceGeezerElectrical20To25 = 0
    private var priceGeezerElectrical50To100 = 0

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var rvImage: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val imageList = arrayListOf<Image>()
    private val urlList = arrayListOf<String>()

    val cbList = arrayListOf<CheckBox>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        dialog = this.myDialog()
        dialogWithText = this.myDialog(true)

        sharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this)[PriceViewModel::class.java]
        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        tvTotal = findViewById(R.id.tv_total)
        tvSubmit = findViewById(R.id.tv_submit)

        spGas = findViewById(R.id.sp_gas)
        spRO = findViewById(R.id.sp_ro)
        spGeezer = findViewById(R.id.sp_geezer)
        spGeezer1 = findViewById(R.id.sp_geezer1)
        spAC = findViewById(R.id.sp_ac)

        spGas.isVisible = false
        spRO.isVisible = false
        spGeezer.isVisible = false
        spGeezer1.isVisible = false
        spAC.isVisible = false

        cbGas = findViewById(R.id.cb_gas)
        cbRo = findViewById(R.id.cb_ro)
        cbGeezer = findViewById(R.id.cb_geezer)
        cbAc = findViewById(R.id.cb_ac)
        cbFlour = findViewById(R.id.cb_flour_mill)
        cbRefrigerator = findViewById(R.id.cb_freeze)
        cbWashingMachine = findViewById(R.id.cb_washing_machine)
        cbMicrowave = findViewById(R.id.cb_microwave)
        cbChimney = findViewById(R.id.cb_chimney)

        tvPriceGas = findViewById(R.id.price_gas)
        tvPriceRo = findViewById(R.id.price_ro)
        tvPriceGeezer = findViewById(R.id.price_geezer)
        tvPriceAc = findViewById(R.id.price_ac)
        tvPriceFlourMill = findViewById(R.id.price_flour_mill)
        tvPriceFreeze = findViewById(R.id.price_freeze)
        tvPriceWashingMachine = findViewById(R.id.price_washing_machine)
        tvPriceMicrowave = findViewById(R.id.price_microwave)
        tvPriceChimney = findViewById(R.id.price_chimney)

        rvImage = findViewById(R.id.rv_image)

        cbList.addAll(
            arrayOf(
                cbGas,
                cbRo,
                cbGeezer,
                cbAc,
                cbFlour,
                cbRefrigerator,
                cbWashingMachine,
                cbMicrowave,
                cbChimney
            )
        )

        imageAdapter = ImageAdapter(imageList) {
            imageList.remove(it)
            imageAdapter.notifyDataSetChanged()
            btnTakePhoto.isVisible = imageList.size < 10
        }
        rvImage.layoutManager = GridLayoutManager(this, 3)
        rvImage.adapter = imageAdapter

        tvPriceGas.isVisible = false
        tvPriceRo.isVisible = false
        tvPriceGeezer.isVisible = false
        tvPriceAc.isVisible = false
        tvPriceFlourMill.isVisible = false
        tvPriceFreeze.isVisible = false
        tvPriceWashingMachine.isVisible = false
        tvPriceMicrowave.isVisible = false
        tvPriceChimney.isVisible = false

        gasList.add("2 Burner")
        gasList.add("3 Burner")
        gasList.add("4 Burner")
        gasList.add("Hob")

        roList.add("R.O + UV + UF + Mineral")
        roList.add("Water Filter + UV/UF")

        geezerList.add("Electrical")
        geezerList.add("Solar")
        geezerList.add("Gas Geezer")

        geezer1List.add("3 To 6 ltr")
        geezer1List.add("10 To 15 ltr")
        geezer1List.add("20 To 25 ltr")
        geezer1List.add("50 To 100 ltr")

        acList.add("1 pic")
        acList.add("2 pic")
        acList.add("3 pic")
        acList.add("4 pic")

        gasAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            gasList
        )
        roAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            roList
        )
        geezerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            geezerList
        )
        geezer1Adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            geezer1List
        )
        acAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            acList
        )

        spGas.adapter = gasAdapter
        spRO.adapter = roAdapter
        spGeezer.adapter = geezerAdapter
        spGeezer1.adapter = geezer1Adapter
        spAC.adapter = acAdapter

        cbGas.setOnCheckedChangeListener { _, isChecked ->
            spGas.isVisible = isChecked
            tvPriceGas.isVisible = isChecked

            if (tvPriceGas.isVisible) {
                spGas.adapter = gasAdapter
                gasAdapter.notifyDataSetChanged()
                updateTotal()
            } else {
                viewModel.gasPrice.value = 0
                updateTotal()
            }

        }
        cbRo.setOnCheckedChangeListener { _, isChecked ->
            spRO.isVisible = isChecked
            tvPriceRo.isVisible = isChecked
            if (tvPriceRo.isVisible) {
                spRO.adapter = roAdapter
                roAdapter.notifyDataSetChanged()
                updateTotal()
            } else {
                viewModel.roPrice.value = 0
                updateTotal()
            }
        }
        cbGeezer.setOnCheckedChangeListener { _, isChecked ->
            spGeezer.isVisible = isChecked
            spGeezer1.isVisible = isChecked
            spGeezer.setSelection(0)
            tvPriceGeezer.isVisible = isChecked
            if (tvPriceGeezer.isVisible) {
                spGeezer.adapter = geezerAdapter
                geezerAdapter.notifyDataSetChanged()
                updateTotal()
            } else {
                viewModel.geezerPrice.value = 0
                updateTotal()
            }

        }
        cbAc.setOnCheckedChangeListener { _, isChecked ->
            spAC.isVisible = isChecked
            tvPriceAc.isVisible = isChecked
            if (tvPriceAc.isVisible) {
                spAC.setSelection(0)
                spAC.adapter = acAdapter
                acAdapter.notifyDataSetChanged()
                updateTotal()
            } else {
                viewModel.acPrice.value = 0
                updateTotal()

            }
        }
        cbFlour.setOnCheckedChangeListener { _, isChecked ->
            tvPriceFlourMill.isVisible = isChecked
            if (tvPriceFlourMill.isVisible) {
                viewModel.flourmillPrice.value = priceFlourMill
                updateTotal()
            } else {
                viewModel.flourmillPrice.value = 0
                updateTotal()
            }
        }
        cbRefrigerator.setOnCheckedChangeListener { _, isChecked ->
            tvPriceFreeze.isVisible = isChecked
            if (tvPriceFreeze.isVisible) {
                viewModel.freezePrice.value = priceFreeze
                updateTotal()
            } else {
                viewModel.freezePrice.value = 0
                updateTotal()
            }
        }
        cbWashingMachine.setOnCheckedChangeListener { _, isChecked ->
            tvPriceWashingMachine.isVisible = isChecked
            if (tvPriceWashingMachine.isVisible) {
                viewModel.washingmachinePrice.value = priceWashingMachine
                updateTotal()
            } else {
                viewModel.washingmachinePrice.value = 0
                updateTotal()
            }
        }
        cbMicrowave.setOnCheckedChangeListener { _, isChecked ->
            tvPriceMicrowave.isVisible = isChecked
            if (tvPriceMicrowave.isVisible) {
                viewModel.microwavePrice.value = priceMicrowave
                updateTotal()
            } else {
                viewModel.microwavePrice.value = 0
                updateTotal()
            }
        }
        cbChimney.setOnCheckedChangeListener { _, isChecked ->
            tvPriceChimney.isVisible = isChecked
            if (tvPriceChimney.isVisible) {
                viewModel.chimneyPrice.value = priceChimney
                updateTotal()
            } else {
                viewModel.chimneyPrice.value = 0
                updateTotal()

            }
        }

        spGas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        tvPriceGas.text = "₹$priceGas2Burner"
                        viewModel.gasPrice.value = priceGas2Burner
                        updateTotal()
                    }
                    1 -> {
                        tvPriceGas.text = "₹$priceGas3Burner"
                        viewModel.gasPrice.value = priceGas3Burner
                        updateTotal()
                    }
                    2 -> {
                        tvPriceGas.text = "₹$priceGas4Burner"
                        viewModel.gasPrice.value = priceGas4Burner
                        updateTotal()
                    }
                    3 -> {
                        tvPriceGas.text = "₹$priceGasHob"
                        viewModel.gasPrice.value = priceGasHob
                        updateTotal()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        spRO.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        tvPriceRo.text = "₹$priceRo"
                        viewModel.roPrice.value = priceRo
                        updateTotal()
                    }
                    1 -> {
                        tvPriceRo.text = "₹$priceWaterFilter"
                        viewModel.roPrice.value = priceWaterFilter
                        updateTotal()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        spGeezer1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        tvPriceGeezer.text = "₹$priceGeezerElectrical3To6"
                        viewModel.geezerPrice.value = priceGeezerElectrical3To6
                        updateTotal()
                    }
                    1 -> {
                        tvPriceGeezer.text = "₹$priceGeezerElectrical10To15"
                        viewModel.geezerPrice.value = priceGeezerElectrical10To15
                        updateTotal()

                    }
                    2 -> {
                        tvPriceGeezer.text = "₹$priceGeezerElectrical20To25"
                        viewModel.geezerPrice.value = priceGeezerElectrical20To25
                        updateTotal()
                    }
                    3 -> {
                        tvPriceGeezer.text = "₹$priceGeezerElectrical50To100"
                        viewModel.geezerPrice.value = priceGeezerElectrical50To100
                        updateTotal()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        spGeezer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                spGeezer1.isVisible = p2 == 0
                when (p2) {
                    0 -> {
                        spGeezer1.setSelection(0)
                        tvPriceGeezer.text = "₹$priceGeezerElectrical3To6"
                        viewModel.geezerPrice.value = priceGeezerElectrical3To6
                        updateTotal()
                    }
                    1 -> {
                        tvPriceGeezer.text = "₹$priceGeezerSolar"
                        viewModel.geezerPrice.value = priceGeezerSolar
                        updateTotal()
                    }
                    2 -> {
                        tvPriceGeezer.text = "₹$priceGeezerGas"
                        viewModel.geezerPrice.value = priceGeezerGas
                        updateTotal()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        spAC.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> {
                        tvPriceAc.text = "₹$priceAc1Pic"
                        viewModel.acPrice.value = priceAc1Pic
                        updateTotal()
                    }
                    1 -> {
                        tvPriceAc.text = "₹$priceAc2Pic"
                        viewModel.acPrice.value = priceAc2Pic
                        updateTotal()
                    }
                    2 -> {
                        tvPriceAc.text = "₹$priceAc3Pic"
                        viewModel.acPrice.value = priceAc3Pic
                        updateTotal()
                    }
                    3 -> {
                        tvPriceAc.text = "₹$priceAc4Pic"
                        viewModel.acPrice.value = priceAc4Pic
                        updateTotal()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        ivPhoto = findViewById(R.id.iv_photo)
        btnTakePhoto = findViewById(R.id.btn_take_photo)
        btnTakePhoto.setOnClickListener {
            if(this.checkPermission(Manifest.permission.CAMERA)){
                takePhoto()
            }else{
                Toast.makeText(this, "Grant Permission For Camera", Toast.LENGTH_SHORT).show()
            }
        }

        getPrice()

        tvSubmit.setOnClickListener {
            onSubmit()
        }
    }

    private fun onSubmit(){
        if (imageList.size > 0) {
            for ((index, i) in cbList.withIndex()) {
                if (i.isChecked) {
                    uploadImages()
                    return
                }else if(index== cbList.size-1){
                    Toast.makeText(this, "Please select one item.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(this, "Please Take at least one photo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createOrder() {
        val stove = if (cbGas.isChecked) {
            "${spGas.selectedItemPosition + 1}"
        } else "-1"
        val ro = if (cbRo.isChecked) {
            "${spRO.selectedItemPosition + 1}"
        } else "-1"
        val geezer = if (cbGeezer.isChecked) {
            when (spGeezer.selectedItemPosition) {
                0 -> {
                    "${spGeezer1.selectedItemPosition + 1}"
                }
                1 -> "5"
                2 -> "6"
                else -> "-1"
            }
        } else "-1"
        val ac = if (cbAc.isChecked) {
            "${spAC.selectedItemPosition + 1}"
        } else "-1"

        dialog.show()
        orderViewModel.createOrder(
            sharedPreferences.getInt(Const.USER_ID, -1).toString(),
            stove,
            ro,
            geezer,
            ac,
            if (cbFlour.isChecked) "1" else "-1",
            if (cbRefrigerator.isChecked) "1" else "-1",
            if (cbWashingMachine.isChecked) "1" else "-1",
            if (cbMicrowave.isChecked) "1" else "-1",
            if (cbChimney.isChecked) "1" else "-1",
            "1",
            urlList.toString()
        )?.observe(this) {
            dialog.dismiss()
            if (it.success) {
                Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun uploadImages() {

        for ((index, i) in imageList.withIndex()) {
            dialogWithText.findViewById<TextView>(R.id.txt_loader).text =
                "Please wait Uploading ${imageList.size} images."
            dialogWithText.show()
            val isLast = imageList.size == index + 1
            uploadImageToFirebase(i.url, isLast)
        }
    }

    private fun uploadImageToFirebase(bitmap: Bitmap, isLast: Boolean): String {
        var imageUrl = ""
        val uri = getImageUri(this, bitmap)
        val fileName =
            sharedPreferences.getInt(Const.USER_ID, -1).toString() + "_" + UUID.randomUUID()
                .toString() + ".jpg"

//            val database = FirebaseDatabase.getInstance()
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")


        if (uri != null) {
            refStorage.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                        urlList.add(imageUrl)
                        Log.d("yoyoyo", "uploadImageToFirebase: $isLast")
                        if (isLast) {
                            dialogWithText.dismiss()
                            Log.d("yoyoyo", "uploadImageToFirebase: $urlList")
                            createOrder()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    print(e.message)
                    imageUrl = "error"
                }
        }
        return imageUrl
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun updateTotal() {
        val total = viewModel.gasPrice.value!! +
                viewModel.roPrice.value!! +
                viewModel.geezerPrice.value!! +
                viewModel.acPrice.value!! +
                viewModel.flourmillPrice.value!! +
                viewModel.freezePrice.value!! +
                viewModel.washingmachinePrice.value!! +
                viewModel.microwavePrice.value!! +
                viewModel.chimneyPrice.value!!
        tvTotal.text = "Total: $total"
    }

    private fun takePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraCode)
    }

    private fun getPrice() {
        dialog.show()
        viewModel.getPrice()?.observe(this) {
            dialog.dismiss()
            if (it.success) {
                if (!it.data.isNullOrEmpty()) {

                    priceGas2Burner = it.data[0].price.toInt()
                    priceGas3Burner = it.data[1].price.toInt()
                    priceGas4Burner = it.data[2].price.toInt()
                    priceGasHob = it.data[3].price.toInt()
                    priceRo = it.data[4].price.toInt()
                    priceWaterFilter = it.data[5].price.toInt()
                    priceGeezerElectrical3To6 = it.data[6].price.toInt()
                    priceGeezerSolar = it.data[7].price.toInt()
                    priceGeezerGas = it.data[8].price.toInt()
                    priceAc1Pic = it.data[9].price.toInt()
                    priceAc2Pic = it.data[10].price.toInt()
                    priceAc3Pic = it.data[11].price.toInt()
                    priceAc4Pic = it.data[12].price.toInt()
                    priceFlourMill = it.data[13].price.toInt()
                    priceFreeze = it.data[14].price.toInt()
                    priceWashingMachine = it.data[15].price.toInt()
                    priceMicrowave = it.data[16].price.toInt()
                    priceChimney = it.data[17].price.toInt()
                    priceGeezerElectrical10To15 = it.data[18].price.toInt()
                    priceGeezerElectrical20To25 = it.data[19].price.toInt()
                    priceGeezerElectrical50To100 = it.data[20].price.toInt()
                    tvPriceFlourMill.text = "\u20B9$priceFlourMill"
                    tvPriceFreeze.text = "₹$priceFreeze"
                    tvPriceWashingMachine.text = "₹$priceWashingMachine"
                    tvPriceMicrowave.text = "₹$priceMicrowave"
                    tvPriceChimney.text = "₹$priceChimney"
                }
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == cameraCode && data != null) {
            imageList.add(Image(data.extras?.get("data") as Bitmap))
//            imageList.add(Image(data.data!!))
            imageAdapter.notifyDataSetChanged()
            btnTakePhoto.isVisible = imageList.size < 10
//            ivPhoto.setImageBitmap(data.extras?.get("data") as Bitmap?)
        }
    }
}