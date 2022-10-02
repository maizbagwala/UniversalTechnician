package `in`.universaltechnician.app

import `in`.universaltechnician.app.adapter.ImageAdapter
import `in`.universaltechnician.app.model.Image
import `in`.universaltechnician.app.utils.Const
import `in`.universaltechnician.app.utils.Functions.checkPermission
import `in`.universaltechnician.app.utils.Functions.myDialog
import `in`.universaltechnician.app.viewmodel.OrderViewModel
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class SingleServiceActivity : AppCompatActivity() {
    private lateinit var rbGas: RadioButton
    private lateinit var rbRo: RadioButton
    private lateinit var rbGeezer: RadioButton
    private lateinit var rbAc: RadioButton
    private lateinit var rbFlourMill: RadioButton
    private lateinit var rbRefrigerator: RadioButton
    private lateinit var rbWashingMachine: RadioButton
    private lateinit var rbMicrowave: RadioButton
    private lateinit var rbChimney: RadioButton
    private lateinit var rvImage: RecyclerView

    private lateinit var imageAdapter: ImageAdapter

    private lateinit var orderViewModel: OrderViewModel

    private lateinit var dialog: Dialog
    private lateinit var dialogWithText: Dialog

    private lateinit var sharedPreferences: SharedPreferences

    private val cameraCode: Int = 123
    private lateinit var btnTakePhoto: TextView
    private lateinit var ivPhoto: ImageView

    private val imageList = arrayListOf<Image>()
    private val urlList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_service)
        dialog = this.myDialog()
        dialogWithText = this.myDialog(true)

        sharedPreferences = getSharedPreferences(Const.PREFERENCE_NAME, Context.MODE_PRIVATE)

        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        rbGas = findViewById(R.id.rb_gas)
        rbRo = findViewById(R.id.rb_ro)
        rbGeezer = findViewById(R.id.rb_geezer)
        rbAc = findViewById(R.id.rb_ac)
        rbFlourMill = findViewById(R.id.rb_flour_mill)
        rbRefrigerator = findViewById(R.id.rb_freeze)
        rbWashingMachine = findViewById(R.id.rb_washing_machine)
        rbMicrowave = findViewById(R.id.rb_microwave)
        rbChimney = findViewById(R.id.rb_chimney)
        rvImage = findViewById(R.id.rv_image)

        imageAdapter = ImageAdapter(imageList) {
            imageList.remove(it)
            imageAdapter.notifyDataSetChanged()
            btnTakePhoto.isVisible = imageList.size < 10
        }
        rvImage.layoutManager = GridLayoutManager(this, 3)
        rvImage.adapter = imageAdapter

        cbList.addAll(
            arrayOf(
                rbGas,
                rbRo,
                rbGeezer,
                rbAc,
                rbFlourMill,
                rbRefrigerator,
                rbWashingMachine,
                rbMicrowave,
                rbChimney
            )
        )
        rbGas.setOnCheckedChangeListener(Listener)
        rbRo.setOnCheckedChangeListener(Listener)
        rbGeezer.setOnCheckedChangeListener(Listener)
        rbAc.setOnCheckedChangeListener(Listener)
        rbFlourMill.setOnCheckedChangeListener(Listener)
        rbRefrigerator.setOnCheckedChangeListener(Listener)
        rbWashingMachine.setOnCheckedChangeListener(Listener)
        rbMicrowave.setOnCheckedChangeListener(Listener)
        rbChimney.setOnCheckedChangeListener(Listener)

        ivPhoto = findViewById(R.id.iv_photo)
        btnTakePhoto = findViewById(R.id.btn_take_photo)
        btnTakePhoto.setOnClickListener {
            if(this.checkPermission(Manifest.permission.CAMERA)){
                takePhoto()
            }else{
                Toast.makeText(this, "Grant Permission For Camera", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<TextView>(R.id.tv_submit).setOnClickListener {
            onSubmit()
        }

    }

    private fun onSubmit() {
        if (imageList.size > 0) {
            for ((index, i) in cbList.withIndex()) {
                if (i.isChecked) {
                    uploadImages()
                    return
                }else if(index==cbList.size-1){
                    Toast.makeText(this, "Please select one item.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        } else {
            Toast.makeText(this, "Please Take at least one photo", Toast.LENGTH_SHORT)
                .show()
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

    private fun takePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraCode)
    }

    object Listener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if (p1) {
                for (i in cbList) {
                    if (p0!!.id != i.id) {
                        i.isChecked = false
                    }
                }
            }
        }

    }

    private fun createOrder() {
        val stove = if (rbGas.isChecked) {
            "1"
        } else "-1"
        val ro = if (rbRo.isChecked) {
            "1"
        } else "-1"
        val geezer = if (rbGeezer.isChecked) {
            "1"
        } else "-1"
        val ac = if (rbAc.isChecked) {
            "1"
        } else "-1"

        dialog.show()
        orderViewModel.createOrder(
            sharedPreferences.getInt(Const.USER_ID, -1).toString(),
            stove,
            ro,
            geezer,
            ac,
            if (rbFlourMill.isChecked) "1" else "-1",
            if (rbRefrigerator.isChecked) "1" else "-1",
            if (rbWashingMachine.isChecked) "1" else "-1",
            if (rbMicrowave.isChecked) "1" else "-1",
            if (rbChimney.isChecked) "1" else "-1",
            "0",
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

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        var path = ""
        if (contentResolver != null) {
            val cursor: Cursor? = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    companion object {
        val cbList = arrayListOf<RadioButton>()
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