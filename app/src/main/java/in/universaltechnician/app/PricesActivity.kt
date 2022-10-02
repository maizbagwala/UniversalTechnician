package `in`.universaltechnician.app

import `in`.universaltechnician.app.utils.Functions.myDialog
import `in`.universaltechnician.app.viewmodel.PriceViewModel
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider

class PricesActivity : AppCompatActivity() {
    lateinit var btnSubmit: TextView
    lateinit var viewModel: PriceViewModel
    lateinit var dialog: Dialog

    lateinit var etGas2Burner: EditText
    lateinit var etGas3Burner: EditText
    lateinit var etGas4Burner: EditText
    lateinit var etGasHob: EditText
    lateinit var etRo: EditText
    lateinit var etWaterFilter: EditText
    lateinit var etGeezerElectrical3To6: EditText
    lateinit var etGeezerElectrical10To15: EditText
    lateinit var etGeezerElectrical20To25: EditText
    lateinit var etGeezerElectrical50To100: EditText
    lateinit var etGeezerSolar: EditText
    lateinit var etGeezerGas: EditText
    lateinit var etAc1Pic: EditText
    lateinit var etAc2Pic: EditText
    lateinit var etAc3Pic: EditText
    lateinit var etAc4Pic: EditText
    lateinit var etFlourMill: EditText
    lateinit var etfreeze: EditText
    lateinit var etWashingMachine: EditText
    lateinit var etMicrowave: EditText
    lateinit var etChimney: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prices)
        viewModel = ViewModelProvider(this)[PriceViewModel::class.java]

        etGas2Burner = findViewById(R.id.et_gas_2burner)
        etGas3Burner = findViewById(R.id.et_gas_3burner)
        etGas4Burner = findViewById(R.id.et_gas_4burner)
        etGasHob = findViewById(R.id.et_gas_hob)
        etRo = findViewById(R.id.et_ro)
        etWaterFilter = findViewById(R.id.et_waterfilter)
        etGeezerElectrical3To6 = findViewById(R.id.et_geezer_electrical_3_6)
        etGeezerElectrical10To15 = findViewById(R.id.et_geezer_electrical_10_15)
        etGeezerElectrical20To25 = findViewById(R.id.et_geezer_electrical_20_25)
        etGeezerElectrical50To100 = findViewById(R.id.et_geezer_electrical_50_100)
        etGeezerSolar = findViewById(R.id.et_geezer_solar)
        etGeezerGas = findViewById(R.id.et_geezer_gas)
        etAc1Pic = findViewById(R.id.et_ac_1pic)
        etAc2Pic = findViewById(R.id.et_ac_2pic)
        etAc3Pic = findViewById(R.id.et_ac_3pic)
        etAc4Pic = findViewById(R.id.et_ac_4pic)
        etFlourMill = findViewById(R.id.et_flourmill)
        etfreeze = findViewById(R.id.et_freeze)
        etWashingMachine = findViewById(R.id.et_washing_machine)
        etMicrowave = findViewById(R.id.et_microwave)
        etChimney = findViewById(R.id.et_chimney)

        dialog = this.myDialog()
        getPrices()
        btnSubmit = findViewById(R.id.tv_submit)
        btnSubmit.setOnClickListener {

            if (
                etGas2Burner.text.isNotEmpty() &&
                etGas3Burner.text.isNotEmpty() &&
                etGas4Burner.text.isNotEmpty() &&
                etGasHob.text.isNotEmpty() &&
                etRo.text.isNotEmpty() &&
                etWaterFilter.text.isNotEmpty() &&
                etGeezerElectrical3To6.text.isNotEmpty() &&
                etGeezerElectrical10To15.text.isNotEmpty() &&
                etGeezerElectrical20To25.text.isNotEmpty() &&
                etGeezerElectrical50To100.text.isNotEmpty() &&
                etGeezerSolar.text.isNotEmpty() &&
                etGeezerGas.text.isNotEmpty() &&
                etAc1Pic.text.isNotEmpty() &&
                etAc2Pic.text.isNotEmpty() &&
                etAc3Pic.text.isNotEmpty() &&
                etAc4Pic.text.isNotEmpty() &&
                etFlourMill.text.isNotEmpty() &&
                etfreeze.text.isNotEmpty() &&
                etWashingMachine.text.isNotEmpty() &&
                etMicrowave.text.isNotEmpty() &&
                etChimney.text.isNotEmpty()
            ) {


                val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
                dialog.setTitle("Are You Sure?")
                dialog.setMessage("This Will Change Prices To Customers.")
                dialog.setPositiveButton("Update") { di, _ ->
                    updatePrices(
                        etGas2Burner.text.toString(),
                        etGas3Burner.text.toString(),
                        etGas4Burner.text.toString(),
                        etGasHob.text.toString(),
                        etRo.text.toString(),
                        etWaterFilter.text.toString(),
                        etGeezerElectrical3To6.text.toString(),
                        etGeezerSolar.text.toString(),
                        etGeezerGas.text.toString(),
                        etAc1Pic.text.toString(),
                        etAc2Pic.text.toString(),
                        etAc3Pic.text.toString(),
                        etAc4Pic.text.toString(),
                        etFlourMill.text.toString(),
                        etfreeze.text.toString(),
                        etWashingMachine.text.toString(),
                        etMicrowave.text.toString(),
                        etChimney.text.toString(),
                        etGeezerElectrical10To15.text.toString(),
                        etGeezerElectrical20To25.text.toString(),
                        etGeezerElectrical50To100.text.toString()

                    )
                }
                dialog.setNegativeButton("Cancel") { di, _ ->
                    di.cancel()
                }
                val alertDialog = dialog.create()
                alertDialog.show()
            } else {
                Toast.makeText(this, "Price can not be empty", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun getPrices() {
        dialog.show()
        viewModel.getPrice()?.observe(this) {
            dialog.dismiss()
            if (it.success) {
                if (!it.data.isNullOrEmpty()) {
                    etGas2Burner.setText(it.data[0].price)
                    etGas3Burner.setText(it.data[1].price)
                    etGas4Burner.setText(it.data[2].price)
                    etGasHob.setText(it.data[3].price)
                    etRo.setText(it.data[4].price)
                    etWaterFilter.setText(it.data[5].price)
                    etGeezerElectrical3To6.setText(it.data[6].price)
                    etGeezerSolar.setText(it.data[7].price)
                    etGeezerGas.setText(it.data[8].price)
                    etAc1Pic.setText(it.data[9].price)
                    etAc2Pic.setText(it.data[10].price)
                    etAc3Pic.setText(it.data[11].price)
                    etAc4Pic.setText(it.data[12].price)
                    etFlourMill.setText(it.data[13].price)
                    etfreeze.setText(it.data[14].price)
                    etWashingMachine.setText(it.data[15].price)
                    etMicrowave.setText(it.data[16].price)
                    etChimney.setText(it.data[17].price)
                    etGeezerElectrical10To15.setText(it.data[18].price)
                    etGeezerElectrical20To25.setText(it.data[19].price)
                    etGeezerElectrical50To100.setText(it.data[20].price)

                }
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePrices(
        p_gas_2burner: String,
        p_gas_3burner: String,
        p_gas_4burner: String,
        p_gas_hob: String,
        p_ro: String,
        p_waterfilter: String,
        p_geezer_electrical_3_5: String,
        p_geezer_solar: String,
        p_geezer_gas: String,
        p_ac_1pic: String,
        p_ac_2pic: String,
        p_ac_3pic: String,
        p_ac_4pic: String,
        p_flourmill: String,
        p_refrigerator: String,
        p_washingmachine: String,
        p_microwave: String,
        p_chimany: String,
        p_geezer_electrical_10_15: String,
        p_geezer_electrical_20_25: String,
        p_geezer_electrical_50_100: String
    ) {
        dialog.show()
        viewModel.updatePrice(
            p_gas_2burner,
            p_gas_3burner,
            p_gas_4burner,
            p_gas_hob,
            p_ro,
            p_waterfilter,
            p_geezer_electrical_3_5,
            p_geezer_solar,
            p_geezer_gas,
            p_ac_1pic,
            p_ac_2pic,
            p_ac_3pic,
            p_ac_4pic,
            p_flourmill,
            p_refrigerator,
            p_washingmachine,
            p_microwave,
            p_chimany,
            p_geezer_electrical_10_15,
            p_geezer_electrical_20_25,
            p_geezer_electrical_50_100
        )?.observe(this) {
            dialog.dismiss()
            if (it.success) {
                if (!it.data.isNullOrEmpty()) {
                    Toast.makeText(this, "Prices Updated", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getPrices()
    }
}