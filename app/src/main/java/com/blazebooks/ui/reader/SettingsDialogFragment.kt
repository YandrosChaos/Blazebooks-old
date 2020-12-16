package com.blazebooks.ui.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.R
import com.blazebooks.util.toast
import kotlinx.android.synthetic.main.activity_book_style.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class SettingsDialogFragment(override val kodein: Kodein, private val cssPath: String) :
    DialogFragment(), KodeinAware {
    private val factory2 by instance<BookStyleViewModelFactory>()
    private lateinit var viewModel2: BookStyleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Light_NoTitleBar
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.activity_book_style, container, false)
        options(rootView)
        return rootView
    }

    private fun options(rootView: View) {

        val btnAcceptStyleChanges = rootView.findViewById<Button>(R.id.btn_acceptStyleChanges)
        val tvFontSizeNumber = rootView.findViewById<TextView>(R.id.tv_fontSizeNumber)
        val tvMarginNumber = rootView.findViewById<TextView>(R.id.tv_MarginNumber)
        val btnIncreaseSize = rootView.findViewById<ImageButton>(R.id.btn_increaseSize)
        val btnDecreaseSize = rootView.findViewById<ImageButton>(R.id.btn_decreaseSize)
        val btnIncreaseMargin = rootView.findViewById<ImageButton>(R.id.btn_increaseMargin)
        val btnDecreaseMargin = rootView.findViewById<ImageButton>(R.id.btn_decreaseMargin)
        val ibtnWhite = rootView.findViewById<ImageButton>(R.id.ibtn_white)
        val ibtnBlack = rootView.findViewById<ImageButton>(R.id.ibtn_black)
        val ibtnSepia = rootView.findViewById<ImageButton>(R.id.ibtn_sepia)
        val spinner2 = rootView.findViewById<Spinner>(R.id.spinner2)

        viewModel2 = ViewModelProvider(this, factory2).get(BookStyleViewModel::class.java)

        val cssFile = File(cssPath)
        var inputAsString = FileInputStream(cssFile).bufferedReader().use { it.readText() }
        var fontSize = viewModel2.getFontSize(cssPath + "_fontSize")
        tvFontSizeNumber.text = fontSize.toString()
        var margin = viewModel2.getMargin(cssPath + "_margin")
        tvMarginNumber.text = margin.toString()

        //Incrementa el tamaño de fuente con el tope en 80px
        btnIncreaseSize.setOnClickListener {
            fontSize += 2
            if (fontSize > 80) fontSize = 80
            tvFontSizeNumber.text = fontSize.toString()
        }

        //Decrementa el tamaño de fuente con el tope en 14px
        btnDecreaseSize.setOnClickListener {
            fontSize -= 2
            if (fontSize < 14) fontSize = 14
            tvFontSizeNumber.text = fontSize.toString()
        }

        //Incrementa el tamaño de fuente con el tope en 80px
        btnIncreaseMargin.setOnClickListener {
            margin += 2
            if (margin > 16) margin = 16
            tv_MarginNumber.text = margin.toString()
        }

        //Decrementa el tamaño de fuente con el tope en 0px
        btnDecreaseMargin.setOnClickListener {
            margin -= 2
            if (margin < 0) margin = 0
            tv_MarginNumber.text = margin.toString()
        }

        //Selecciona la fuente de la lista elegida por el usuario y la cambia en el archivo CSS
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {
                val item = adapterView.getItemAtPosition(position).toString()

                inputAsString = inputAsString.replace(
                    "font-family:.*;".toRegex(),
                    "font-family:\"$item\", sans-serif;"
                )
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        //Changes the background color to white
        ibtnWhite.setOnClickListener {
            inputAsString = inputAsString.replace(
                "/\\*background-color body\\*/background-color:.*;".toRegex(),
                "/\\*background-color body\\*/background-color: #FFFFFF;"
            )

            //changes the font color to black
            inputAsString = inputAsString.replace(
                "/\\*color body\\*/color:.*;".toRegex(),
                "/\\*color body\\*/color: #000000;"
            )
        }

        //Changers the background color to sepia color
        ibtnSepia.setOnClickListener {
            inputAsString = inputAsString.replace(
                "/\\*background-color body\\*/background-color:.*;".toRegex(),
                "/\\*background-color body\\*/background-color: #e3ceb9;"
            )
        }

        //Changers the background color to blkack
        ibtnBlack.setOnClickListener {
            inputAsString = inputAsString.replace(
                "/\\*background-color body\\*/background-color:.*;".toRegex(),
                "/\\*background-color body\\*/background-color: #222222;"
            )

            //changes the font color to white
            inputAsString = inputAsString.replace(
                "/\\*color body\\*/color:.*;".toRegex(),
                "/\\*color body\\*/color: #FFFFFF;"
            )
        }

        //Al pulsar se confirman los cambios y se cierra la actividad
        btnAcceptStyleChanges.setOnClickListener {

            inputAsString = inputAsString.replace(
                "/\\*font-size p\\*/font-size:.*;".toRegex(),
                "/\\*font-size p\\*/font-size:$fontSize" + "px;"
            )

            inputAsString = inputAsString.replace(
                "/\\*margin\\*/margin:.*;".toRegex(),
                "/\\*margin\\*/margin:$margin" + "px;"
            )

            FileOutputStream(cssFile).use {
                it.write(inputAsString.toByteArray())
            }

            viewModel2.setFontSize(cssPath + "_fontSize", fontSize)
            viewModel2.setMargin(cssPath + "_margin", margin)
            (activity as ReaderActivity).refresh()
            dismiss()
        }

    }

}//class