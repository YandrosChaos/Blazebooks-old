package com.blazebooks.ui.customdialogs.filter

interface FilterDialogListener {
    fun onReturnFilters(dialog: FilterDialog)
    fun onClearFilters(dialog: FilterDialog)
    fun onCloseDialog(dialog: FilterDialog)
}