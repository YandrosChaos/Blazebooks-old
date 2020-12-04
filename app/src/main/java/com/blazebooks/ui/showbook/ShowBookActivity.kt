package com.blazebooks.ui.showbook

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import com.blazebooks.data.db.AppDatabase
import com.blazebooks.databinding.ActivityShowBookBinding
import com.blazebooks.ui.becomepremium.BecomePremiumActivity
import com.blazebooks.ui.reader.ReaderActivity
import com.blazebooks.util.*
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.gms.common.api.ApiException
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_show_book.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File

private const val DELAYED_UI_CONFIG_TIME: Long = 1000

/**
 * @author Mounir Zbayr
 * @author Victor Gonzalez
 */
class ShowBookActivity : PreconfiguredActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<ShowBookViewModelFactory>()

    private val adapter by lazy {
        ShowBookViewPagerAdapter(
            this
        )
    }
    lateinit var viewModel: ShowBookViewModel
    private lateinit var binding: ActivityShowBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_book)
        binding.activityShowBookViewPager.adapter = adapter



        viewModel = ViewModelProvider(this, factory).get(ShowBookViewModel::class.java)
        createTabs()
        isLiked()
        isDownloaded()

        Handler().postDelayed({
            setLikeUI()
            setDownloadUI()
        }, DELAYED_UI_CONFIG_TIME)
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

    /**
     * If the current book is liked, then removes the like. Else, set liked to true the book.
     * Later refresh the drawable state.
     *
     * @see AppDatabase
     *
     * @author Victor Gonzalez
     */
    fun addFav(view: View) {
        if (viewModel.liked) {
            removeFromFav()

        } else {
            addToFav()
        }
        view.refreshDrawableState()
    }

    /**
     * Método que al pulsar el botón de descarga obtiene el archivo del firebase storage y lo guarda
     * en una carpeta con el nombre del libro, creando ésta en la carpeta del proyecto.
     *
     * Si el usuario no es premium, mostrará BecomePremiumActivity.
     *
     * @see BecomePremiumActivity
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun download(view: View) {

        val titleBook = CURRENT_BOOK.title.toString() //nombre del libro
        val documents =
            "books/$titleBook" //La carpeta creada irá dentro de la carpeta books
        val documentsFolder = File(this.filesDir, documents)
        val filesPath = this.getExternalFilesDir(null)?.absolutePath

        when {
            !PREMIUM && CURRENT_BOOK.premium -> {
                startBecomePremiumActivity()
            }

            !viewModel.exist -> {
                //si no está en la base de datos local
                binding.showBookBtnDownload.speed = 1f
                binding.showBookBtnDownload.playAnimation()
                view.refreshDrawableState()

                //Al no estar descargado el botón de lectura se bloquea
                binding.showBookBtnRead.isEnabled = false
                binding.showBookBtnRead.isEnabled = false
                binding.showBookBtnRead.isClickable = false


                toast(getString(R.string.dwnloading))

                documentsFolder.mkdirs() // Crea la carpeta en la direccion dada

                val mStorageRef =
                    FirebaseStorage.getInstance().reference //Referencia al storage de Firebase
                //Con esto se obtiene la url del libro dependiendo de su nombre
                mStorageRef.child("Epub/$titleBook.epub").downloadUrl.addOnSuccessListener {
                    downloadFile(
                        it.toString(),
                        "$titleBook.epub",
                        "$filesPath/$documents"
                    )
                }.addOnFailureListener {
                    toast(getString(R.string.dwnload_error))
                    documentsFolder.delete() //Borra la carpeta creada al dar error
                }

                viewModel.storeBookIntoLocalDatabase(
                    titleBook,
                    documents
                )//almacena la info dentro de la base de datos local

                binding.showBookBtnRead.isEnabled = true
            }
            else -> {

                binding.showBookBtnDownload.playAnimation()
                view.refreshDrawableState()

                val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

                //Borra el libro seleccionado de la base de datos y de la memoria interna
                dialogBuilder.setMessage((getString(R.string.message_delete_book)))
                    .setCancelable(false)
                    .setPositiveButton((getString(R.string.accept_delete_book))) { _, _ ->
                        viewModel.deleteBookFromLocalDatabase(
                            titleBook,
                            documents
                        )

                        File( "$filesPath/$documents" ).deleteRecursively()
                        finish()

                    }
                    .setNegativeButton((getString(R.string.cancel_delete_book))) { dialog, _ ->
                        dialog.cancel()
                    }
                dialogBuilder.create().show()

            }
        }
    }//download

    /**
     * Método de pulsado del boton Read, el cual lleva al libro elegido.
     * Si el usuario no es premium mostrará BecomePremiumActivity. Guarda el
     * libro en sharedPreferences para poder abrirlo directamente desde el main.
     *
     * @see BecomePremiumActivity
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun read(view: View) {

       readChapter(0, view)

    }

    fun readChapter(pag: Int, view: View){
        if (!PREMIUM && CURRENT_BOOK.premium) {
            startBecomePremiumActivity()
        } else {
            if (viewModel.exist) {
                val titleBook = CURRENT_BOOK.title.toString()
                val documents = "books/$titleBook"
                val i = Intent(this, ReaderActivity::class.java).putExtra("CHAPTER", pag)
                val bookUrl = "$documents/$titleBook.epub"

                viewModel.saveIntoSharedPreferences(bookUrl)

                startActivity(i)
                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
                finish()
            } else {
                view.snackbar(getString(R.string.not_dwnload_yet))
            }
        }
    }

    /**
     * Recibe la URL y la ruta de destino y descarga el archivo usando PRDownloader
     *
     * @author Mounir Zbayr
     */
    private fun downloadFile(url: String, fileName: String, dirPath: String) {

        PRDownloader.download(url, dirPath, fileName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    viewModel.saveBookResources(
                        File("$dirPath/$fileName"),
                        dirPath
                    )

                    //Cuando la descarga está completa el botón de Read se desbloquea.
                    showBookBtnRead.isEnabled = true
                    showBookBtnRead.isClickable = true

                    toast(getString(R.string.dwnload_cmplete))
                }

                override fun onError(error: com.downloader.Error?) {
                    toast(getString(R.string.dwnload_error))
                }
            })

    }

    /**
     * Sets the GUI depending user liked books.
     *
     * @author Victor Gonzalez
     */
    private fun setLikeUI() {
        if (viewModel.liked) {
            binding.showBookBtnFav.progress = 1f
            binding.showBookBtnFav.refreshDrawableState()
        }
    }

    /**
     * Sets the GUI depending user stored books.
     *
     * @author Victor Gonzalez
     */
    private fun setDownloadUI() {
        if (viewModel.exist) {
            binding.showBookBtnDownload.apply {
                setAnimation(R.raw.delete)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }.refreshDrawableState()
        }
    }

    /**
     * Crea las diferentes tabs
     */
    private fun createTabs() {
        TabLayoutMediator(binding.activityShowBookTabLayout, binding.activityShowBookViewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.synopsis)
                1 -> tab.text = getString(R.string.chapters)
            }
        }.attach()
    }

    /**
     * Comprueba si el libro está en la lista de favs del user o no.
     */
    private fun isLiked() =
        lifecycleScope.launch {
            try {
                viewModel.isFavBook(CURRENT_BOOK)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        //success
                        viewModel.liked = true
                    }
            } catch (e: ApiException) {
                binding.root.snackbar("Check your internet connection, please.")
            } catch (e: DocumentNotFoundException) {
            }
        }


    private fun isDownloaded() = lifecycleScope.launch {
        viewModel.bookExist()
    }

    private fun addToFav() {
        lifecycleScope.launch {
            try {
                viewModel.insertLikedBook(CURRENT_BOOK)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //success
                        binding.showBookBtnFav.speed = 1f
                        binding.showBookBtnFav.playAnimation()
                        viewModel.liked = true
                    }, {
                        //failure
                        binding.root.snackbar("Something went wrong :/ Try again.")
                    })
            } catch (e: ApiException) {
                binding.root.snackbar("Check your internet connection, please.")
            }
        }
    }

    private fun removeFromFav() {
        lifecycleScope.launch {
            try {
                viewModel.deleteLikedBook(CURRENT_BOOK)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //success
                        binding.showBookBtnFav.speed = -1f
                        binding.showBookBtnFav.playAnimation()
                        viewModel.liked = false
                    }, {
                        //failure
                        binding.root.snackbar("Something went wrong :/ Try again.")
                    })
            } catch (e: ApiException) {
                binding.root.snackbar("Check your internet connection, please.")
            }
        }
    }

}//class
