package com.blazebooks.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.blazebooks.R
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.dialog_filter.view.*
import java.lang.ClassCastException

/**
 * Custom filter dialog
 *
 * @see com.blazebooks.ui.search.SearchActivity
 * @author Victor Gonzalez
 */
class FilterDialog(private val checkboxStoredList: MutableList<CheckBox>) : DialogFragment() {
    private lateinit var listener: FilterDialogListener
    private val filterList: ArrayList<CheckBox> = arrayListOf()
    var filterReturnList: MutableList<CheckBox> = mutableListOf()

    interface FilterDialogListener {
        fun onReturnFilters(dialog: FilterDialog)
        fun onClearFilters(dialog: FilterDialog)
        fun onCloseDialog(dialog: FilterDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_filter, container, false)
    }

    /**
     * After view created, adds checkbox using the information stored into
     * arrays.xml and sets it. Adds onClickListeners. Communicate this view the
     * their host.
     *
     * @see createUI
     * @see setUI
     * @see FilterDialogListener
     *
     * @author Victor Gonzalez
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.createUI(view)
        this.setUI()

        //close button -> actions in host
        view.dialogFilterCloseButton.setOnClickListener {
            listener.onCloseDialog(this)
        }

        //clear button -> clear list and view + actions in host
        view.dialogFilterClearButton.setOnClickListener {
            filterList.forEach {
                it.isChecked = false
            }
            if (filterReturnList.isNotEmpty()) {
                filterReturnList.clear()
            }
            listener.onClearFilters(this)
        }

        //apply button -> adds checked items to filterReturnList + actions in host
        view.dialogFilterApplyButton.setOnClickListener {
            //add checked items to list
            filterList.forEach {
                if (it.isChecked && !filterReturnList.contains(it)) {
                    filterReturnList.add(it)
                }
            }
            listener.onReturnFilters(this)
        }
    }

    /**
     * Instantiates the FilterDialogListener and verifies that
     * host activity implements the callback interface.
     *
     * @author Victor Gonzalez
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as FilterDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FilterDialogListener")
        }
    }

    /**
     * Creates one checkbox per each item into arrays.xml and adds it
     * to UI. Also adds checkbox to filterList.
     *
     * @see filterList
     *
     * @param view The view inflated in onCreateView
     *
     * @author Victor Gonzalez
     */
    private fun createUI(view: View) {
        val dialogFilterGenresLl = view.findViewById<LinearLayout>(R.id.dialogFilterGenresLL)
        val dialogFilterAuthorsLl = view.findViewById<LinearLayout>(R.id.dialogFilterAuthorsLl)
        val dialogFilterPremiumLl = view.findViewById<LinearLayout>(R.id.dialogFilterPremiumLl)
        val dialogFilterLanguageLl = view.findViewById<LinearLayout>(R.id.dialogFilterLanguageLl)

        //add genres
        resources.getStringArray(R.array.genres)
            .forEach {
                createCheckBox(it, resources.getString(R.string.genres))
                    .apply {
                        dialogFilterGenresLl.addView(this)
                        filterList.add(this)
                    }
            }

        //add languages
        resources.getStringArray(R.array.languages)
            .forEach {
                createCheckBox(it, resources.getString(R.string.language))
                    .apply {
                        dialogFilterLanguageLl.addView(this)
                        filterList.add(this)
                    }
            }

        //add premium
        resources.getStringArray(R.array.premium)
            .forEach {
                createCheckBox(it, resources.getString(R.string.premium))
                    .apply {
                        dialogFilterPremiumLl.addView(this)
                        filterList.add(this)
                    }
            }

        //add authors
        resources.getStringArray(R.array.authors).forEach {
            createCheckBox(it, resources.getString(R.string.authors))
                .apply {
                    dialogFilterAuthorsLl.addView(this)
                    filterList.add(this)
                }
        }
    }

    /**
     * Sets the checkbox stored into filterList to checked if checkboxStoredList
     * received contains it.
     *
     * @see filterList
     * @see checkboxStoredList
     *
     * @author Victor Gonzalez
     */
    private fun setUI() {
        if (checkboxStoredList.isNotEmpty()) {
            filterList.forEach { dialogFilterItem ->
                checkboxStoredList.forEach {
                    if (dialogFilterItem.tag == it.tag
                        && dialogFilterItem.text == it.text
                    ) {
                        dialogFilterItem.isChecked = true
                    }
                }
            }
        }
    }

    /**
     * Creates and sets a MaterialCheckBox view.
     *
     * @param text Text into checkBox
     * @param tag Tag into checkBox. It is important for the filtering.
     * @return MaterialCheckBox
     * @author Victor Gonzalez
     */
    private fun createCheckBox(text: String, tag: String): MaterialCheckBox {
        return MaterialCheckBox(context).apply {
            this.text = text
            this.tag = tag
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setTextColor(resources.getColor(R.color.white))
        }


    }

}