package com.blazebooks.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.blazebooks.R
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.android.synthetic.main.dialog_filter.view.*
import java.lang.ClassCastException

/**
 * Custom filter dialog
 *
 * @see com.blazebooks.ui.search.SearchActivity
 * @author Victor Gonzalez
 */
class FilterDialog(private val storedFilterList: MutableList<Pair<String, String>>) :
    DialogFragment() {
    private lateinit var listener: FilterDialogListener
    private val checkBoxList: ArrayList<CheckBox> = arrayListOf()
    private val radioButtonList: ArrayList<RadioButton> = arrayListOf()
    var filterReturnList: MutableList<Pair<String, String>> = mutableListOf()

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
            checkBoxList.filter { checkBox -> checkBox.isChecked }.forEach {
                it.isChecked = false
            }
            radioButtonList.filter { radButton -> radButton.isChecked }.forEach {
                it.isChecked = false
            }
            if (filterReturnList.isNotEmpty()) {
                filterReturnList.clear()
            }
            listener.onClearFilters(this)
        }

        //apply button -> adds checked items to filterReturnList + actions in host
        view.dialogFilterApplyButton.setOnClickListener {
            //add checkBox items to list
            checkBoxList.filter { checkBox -> checkBox.isChecked }.forEach {
                if (!filterReturnList.contains(
                        Pair(
                            it.tag.toString(),
                            it.text.toString()
                        )
                    )
                ) {
                    filterReturnList.add(
                        Pair(
                            it.tag.toString(),
                            it.text.toString()
                        )
                    )
                }
            }

            //add radiobuttons to list
            radioButtonList.filter { radioButton -> radioButton.isChecked }.forEach {
                if (!filterReturnList.contains(
                        Pair(
                            it.tag.toString(),
                            it.text.toString()
                        )
                    )
                ) {
                    filterReturnList.add(
                        Pair(
                            it.tag.toString(),
                            it.text.toString()
                        )
                    )
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
     * @see checkBoxList
     *
     * @param view The view inflated in onCreateView
     *
     * @author Victor Gonzalez
     */
    private fun createUI(view: View) {
        val dialogFilterGenresLl = view.findViewById<LinearLayout>(R.id.dialogFilterGenresLL)
        val dialogFilterAuthorsLl = view.findViewById<RadioGroup>(R.id.dialogFilterAuthorsRG)
        val dialogFilterPremiumLl = view.findViewById<RadioGroup>(R.id.dialogFilterPremiumRG)
        val dialogFilterLanguageLl = view.findViewById<RadioGroup>(R.id.dialogFilterLanguageRG)

        //add genres
        resources.getStringArray(R.array.genres).sortedArray()
            .forEach {
                createCheckBox(it, resources.getString(R.string.genres))
                    .apply {
                        dialogFilterGenresLl.addView(this)
                        checkBoxList.add(this)
                    }
            }

        //add languages
        resources.getStringArray(R.array.languages).sortedArray()
            .forEach {
                createRadioButton(it, resources.getString(R.string.language))
                    .apply {
                        dialogFilterLanguageLl.addView(this)
                        radioButtonList.add(this)
                    }
            }

        //add premium
        resources.getStringArray(R.array.premium).sortedArray()
            .forEach {
                createRadioButton(it, resources.getString(R.string.premium))
                    .apply {
                        dialogFilterPremiumLl.addView(this)
                        radioButtonList.add(this)
                    }
            }

        //add authors
        resources.getStringArray(R.array.authors).sortedArray()
            .forEach {
                createRadioButton(it, resources.getString(R.string.authors))
                    .apply {
                        dialogFilterAuthorsLl.addView(this)
                        radioButtonList.add(this)
                    }
            }
    }

    /**
     * Sets the checkbox stored into filterList to checked if checkboxStoredList
     * received contains it.
     *
     * @see checkBoxList
     * @see storedFilterList
     *
     * @author Victor Gonzalez
     */
    private fun setUI() {
        if (storedFilterList.isNotEmpty()) {

            //checkBoxList
            checkBoxList.forEach { dialogFilterItem ->
                storedFilterList.filter { pair -> pair.first == resources.getString(R.string.genres) }
                    .forEach {
                        if (dialogFilterItem.tag == it.first
                            && dialogFilterItem.text.toString() == it.second
                        ) {
                            dialogFilterItem.isChecked = true
                        }
                    }
            }

            //radioButtons
            radioButtonList.forEach { radioButton ->
                storedFilterList.filter { pair -> pair.first != resources.getString(R.string.genres) }
                    .forEach {
                        if (radioButton.tag == it.first && radioButton.text.toString() == it.second) {
                            radioButton.isChecked = true
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
        return MaterialCheckBox(requireContext()).apply {
            this.text = text
            this.tag = tag
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setTextColor(resources.getColor(R.color.white))
        }
    }

    /**
     * Creates and sets a MaterialRadioButton view.
     *
     * @param text Text into checkBox
     * @param tag Tag into checkBox. It is important for the filtering.
     * @return MaterialCheckBox
     * @author Victor Gonzalez
     */
    private fun createRadioButton(text: String, tag: String): MaterialRadioButton {
        return MaterialRadioButton(requireContext()).apply {
            this.text = text
            this.tag = tag
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setTextColor(resources.getColor(R.color.white))
        }
    }

}