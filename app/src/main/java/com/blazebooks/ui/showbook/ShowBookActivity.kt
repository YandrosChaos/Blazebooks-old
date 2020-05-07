package com.blazebooks.ui.showbook

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.adapter.ViewPagerAdapter
import com.blazebooks.ui.PreconfiguredActivity
import com.blazebooks.ui.reader.ReaderActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.android.synthetic.main.activity_show_book_item.*
import java.io.File
import kotlin.math.roundToInt


/**
 * @see PreconfiguredActivity
 * @see ChapterFragment
 * @see SynopsisFragment
 * @author Victor Gonzalez
 */
class ShowBookActivity : PreconfiguredActivity() {
    private val adapter by lazy { ViewPagerAdapter(this) }
    private var liked = false

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

        /*
        TODO -> donde guardar si se ha dado like o no a un libro
                            if (liked) {
                                showBookBtnFav.progress = 1f
                            }
         */

    }

    fun addFav(view: View) {
        liked = when (liked) {
            true -> {
                //TODO -> remove from favs
                showBookBtnFav.speed = -1f
                showBookBtnFav.playAnimation()
                Toast.makeText(this, getString(R.string.rm_favs), Toast.LENGTH_SHORT).show()
                false
            }
            false -> {
                //TODO -> add to favs
                showBookBtnFav.speed = 1f
                showBookBtnFav.playAnimation()
                Toast.makeText(this, getString(R.string.add_favs), Toast.LENGTH_SHORT).show()
                true
            }
        }
        view.refreshDrawableState()
    }

    /**
     * Método que al pulsar el botón de descarga obtiene el archivo del firebase storage y lo guarda
     * en una carpeta con el nombre del libro, creando ésta en la carpeta del proyecto
     *
     * @author MounirZbayr
     */
    fun download(view: View) {
        val titleBook = showBookTvTitle.text.toString() //nombre del libro
        val documents = "books/$titleBook" //La carpeta creada irá dentro de la carpeta books
        val documentsFolder = File(this.filesDir, documents)

        //si la carpeta existe solo mostrará el mensaje, si no la creará y descargará el libro
        if (documentsFolder.exists()) Toast.makeText(
            this,
            getString(R.string.already_dwnload),
            Toast.LENGTH_SHORT
        ).show()
        else {
            Toast.makeText(this, getString(R.string.dwnloading), Toast.LENGTH_SHORT).show()

            documentsFolder.mkdirs() // Crea la carpeta en la direccion dada

            val mStorageRef =
                FirebaseStorage.getInstance().reference //Referencia al storage de Firebase
            //Con esto se obtiene la url del libro dependiendo de su nombre
            mStorageRef.child("Epub/$titleBook.epub").downloadUrl.addOnSuccessListener {
                downloadFile(this, titleBook, documents, it.toString())
                Toast.makeText(this, getString(R.string.dwnload_cmplete), Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, getString(R.string.dwnload_error), Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * Recibe la URL y la ruta de destino y descarga el archivo usando DownloadManager
     *
     * @author Mounir Zbayr
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
     * Método de pulsado del boton Read, el cual lleva al libro elegido
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun read(view: View) {

        val titleBook = showBookTvTitle.text.toString()
        val documents = "books/$titleBook"
        val documentsFolder = File(this.filesDir, documents)

        if (documentsFolder.exists()) {
            val i = Intent(this, ReaderActivity::class.java)
            i.putExtra(Constants.PATH_CODE, "$documents/$titleBook.epub")
            startActivity(i)
            overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.not_dwnload_yet), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Returns to previous activity and sets custom animation transition.
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        finish()
    }

}//class
