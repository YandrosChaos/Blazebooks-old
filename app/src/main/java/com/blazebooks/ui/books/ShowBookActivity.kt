package com.blazebooks.ui.books

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blazebooks.R
import com.blazebooks.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.activity_show_book_item.*
import java.io.File


class ShowBookActivity : AppCompatActivity() {
    private val adapter by lazy { ViewPagerAdapter(this) }
    private var liked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_book)
        activityShowBookViewPager.adapter = adapter

        //crear las diferentes pestañas
        val tabLayoutMediator =
        TabLayoutMediator(activityShowBookTabLayout, activityShowBookViewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = getString(R.string.synopsis)
                    }
                    1 -> {
                        tab.text = getString(R.string.chapters)
                    }
                }
            })
        tabLayoutMediator.attach()
        //default option
    }

    fun addFav(view: View) {
        liked = when (liked) {
            true -> {
                //TODO -> remove from favs
                //showBookBtnFav.load(R.drawable.ic_like_remove)
                showBookBtnFav.background = ContextCompat.getDrawable(this, R.drawable.ic_like_remove)
                false
            }
            false -> {
                //TODO -> add to favs
                //showBookBtnFav.load(R.drawable.ic_like_add)
                showBookBtnFav.background = ContextCompat.getDrawable(this, R.drawable.ic_like_add)
                true
            }
        }
        view.refreshDrawableState()
    }

    /**
     * Método que al pulsar el botón de descarga obtiene el archivo del firebase storage y lo guarda
     * en una carpeta con el nombre del libro, creando ésta en la carpeta del proyecto
     */
    fun download(view: View) {

        val titleBook= showBookTvTitle.text.toString() //nombre del libro
        val documents = "books/$titleBook" //La carpeta creada irá dentro de la carpeta books
        val documentsFolder = File(this.filesDir, documents)

        //si la carpeta existe solo mostrará el mensaje, si no la creará y descargará el libro
        if(documentsFolder.exists()) Toast.makeText(this, "Already downloaded", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()

            documentsFolder.mkdirs() // Crea la carpeta en la direccion dada

            val mStorageRef = FirebaseStorage.getInstance().reference //Referencia al storage de Firebase
            //Con esto se obtiene la url del libro dependiendo de su nombre
            mStorageRef.child("Epub/$titleBook.epub").downloadUrl.addOnSuccessListener {
                downloadFile(this, titleBook, documents, it.toString())
                Toast.makeText(this, "Download completed", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * Recibe la URL y la ruta de destino y descarga el archivo usando DownloadManager
     */
    private fun downloadFile(
        context: Context,
        fileName: String,
        destinationDirectory: String?,
        uri: String?
    ) {
        val downloadManager =
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val link = Uri.parse(uri)
        val request = DownloadManager.Request(link)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(
            context,
            destinationDirectory,
            "$fileName.epub"
        )
        downloadManager.enqueue(request)
    }

    /**
     * Método de pulsado del boton leer, de momento indica si esta descargado o no
     */
    fun read(view: View) {

        val titleBook= showBookTvTitle.text.toString()
        val documents = "books/$titleBook"
        val documentsFolder = File(this.filesDir, documents)

        if(documentsFolder.exists()){
            Toast.makeText(this, documentsFolder.path.toString(), Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "Not downloaded yet", Toast.LENGTH_SHORT).show()
        }
    }
}//class
