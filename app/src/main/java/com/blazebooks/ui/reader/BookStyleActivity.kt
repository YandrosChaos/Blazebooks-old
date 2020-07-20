package com.blazebooks.ui.reader

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import kotlinx.android.synthetic.main.activity_book_style.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class BookStyleActivity : PreconfiguredActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<BookStyleViewModelFactory>()
    private lateinit var viewModel: BookStyleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_style)

        viewModel = ViewModelProvider(this, factory).get(BookStyleViewModel::class.java)

        val cssPath= intent.getStringExtra("cssPath")
        val cssFile= File(cssPath)
        var inputAsString = FileInputStream(cssFile).bufferedReader().use { it.readText() }
        var fontSize= viewModel.getFontSize(cssPath+"_fontSize")
        tv_fontSizeNumber.text= fontSize.toString()

        //Incrementa el tamaño de fuente con el tope en 80px
        btn_increaseSize.setOnClickListener {
            fontSize+=2
            if(fontSize>80) fontSize= 80
            tv_fontSizeNumber.text= fontSize.toString()
        }

        //Decrementa el tamaño de fuente con el tope en 14px
        btn_decreaseSize.setOnClickListener {
            fontSize-=2
            if(fontSize<14) fontSize= 14
            tv_fontSizeNumber.text= fontSize.toString()
        }

        //inputAsString= inputAsString.replace("color:.*;".toRegex(), "color:black;")

        //Selecciona la fuente de la lista elegida por el usuario y la cambia en el archivo CSS
        spinner2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                val item = adapterView.getItemAtPosition(position).toString()

                inputAsString= inputAsString.replace("font-family:.*;".toRegex(), "font-family:$item;")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        //Al pulsar se confirman los cambios y se cierra la actividad
        btn_acceptStyleChanges.setOnClickListener {

            inputAsString= inputAsString.replace("font-size:.*;".toRegex(), "font-size:$fontSize"+"px;")
            FileOutputStream(cssFile).use {
                it.write(inputAsString.toByteArray())
            }

            viewModel.setFontSize(cssPath+"_fontSize",fontSize)
            finish()
        }

    }//onCreate

}//class
