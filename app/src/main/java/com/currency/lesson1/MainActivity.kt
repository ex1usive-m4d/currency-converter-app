package com.currency.lesson1

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.net.URL
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import org.json.JSONObject
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    private var spinnerFrom: Spinner? = null
    private var spinnerTo: Spinner? = null
    private var currencyInput: EditText? = null
    private var convertBtn: Button? = null
    private var resultInfo: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        currencyInput = findViewById(R.id.editInput)
        resultInfo = findViewById(R.id.resultValue)
        convertBtn = findViewById(R.id.convertBtn)

        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //your codes here
        }

        convertBtn?.setOnClickListener {
            var apiResponse: String? = null;
            if (currencyInput?.text?.toString()?.trim()?.isEmpty()!!) {
                Toast.makeText(this, "Введите значение", Toast.LENGTH_LONG).show()
            } else {
                var currency: String = spinnerFrom?.selectedItem.toString().plus('_').plus(spinnerTo?.selectedItem.toString())
                var url: String = "https://free.currconv.com/api/v7/convert?q=${currency}&compact=ultra&apiKey=27aa03909cf691819561"
                do {
                    apiResponse = URL(url.toString()).readText()
                    val currencyRate = JSONObject(apiResponse).get(currency.toString()).toString()
                    Log.d("currency", currencyRate)
                    val calculation = currencyInput?.text.toString().toDouble() * currencyRate.toString().toDouble()
                    resultInfo?.text = "Результат:".plus(calculation.toString())
                } while (apiResponse.isNullOrBlank())
            }
        }
    }
}