package com.blazebooks.ui.reader


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import com.blazebooks.util.toast
import kotlinx.android.synthetic.main.activity_book_style.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class BookStyleActivity : PreconfiguredActivity(), KodeinAware {
    override val kodein by kodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_style)





        val i= intent.getStringExtra("cssPath")
        val cssFile= File(i)

        var inputAsString = FileInputStream(cssFile).bufferedReader().use { it.readText() }

        var sad= Regex("font-size:.*;").find(inputAsString)

        var fontSize= Integer.parseInt(tv_fontSizeNumber.text.toString());

        if (sad != null) {
            toast(sad.value)
        }


        btn_increaseSize.setOnClickListener {
            fontSize+=2
            tv_fontSizeNumber.text= fontSize.toString()
        }

        btn_decreaseSize.setOnClickListener {
            fontSize-=2
            tv_fontSizeNumber.text= fontSize.toString()
        }

        inputAsString= inputAsString.replace("color:.*;".toRegex(), "color:black;")

        spinner2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                val item = adapterView.getItemAtPosition(position).toString()

                toast(item)
                inputAsString= inputAsString.replace("font-family:.*;".toRegex(), "font-family:$item;")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }



        btn_acceptStyleChanges.setOnClickListener {

            inputAsString= inputAsString.replace("font-size:.*;".toRegex(), "font-size:$fontSize"+"px;")
            FileOutputStream(cssFile).use {
                it.write(inputAsString.toByteArray())
            }

            finish()
        }


    }

    fun increaseFontSize(){

    }

}//class
