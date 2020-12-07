package com.blazebooks.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blazebooks.R
import com.blazebooks.ui.auth.LoginActivity
import com.blazebooks.ui.auth.SignUpActivity
import com.blazebooks.ui.becomepremium.BecomePremiumActivity
import com.blazebooks.ui.main.MainActivity
import com.blazebooks.ui.search.SearchActivity
import com.google.android.material.snackbar.Snackbar

fun Context.startMainActivity() =
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Fragment.startLoginActivity() =
    Intent(this.context, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startSignUpActivity() =
    Intent(this, SignUpActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startBecomePremiumActivity() =
    Intent(this, BecomePremiumActivity::class.java).also {
        startActivity(it)
    }

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun Context.positiveAlertDialog(title: String, message: String, buttonText: String): AlertDialog {
    return with(AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))) {
        setTitle(title)
        setMessage(message)
        setPositiveButton(buttonText, null)
        create()
    }
}

/**
 * Used to scroll to the given view.
 *
 * @param scrollViewParent Parent ScrollView
 * @param view View to which we need to scroll.
 *
 * @author from https://stackoverflow.com/questions/21483188/scroll-to-a-specific-view-in-scroll-view
 */
fun scrollToView(
    scrollViewParent: ScrollView,
    view: View
) {
    // Get deepChild Offset
    val childOffset = Point()
    getDeepChildOffset(scrollViewParent, view.parent, view, childOffset)
    // Scroll to child.
    scrollViewParent.smoothScrollTo(0, childOffset.y)
}

/**
 * Used to get deep child offset.
 *
 *
 * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
 * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
 *
 * @param mainParent        Main Top parent.
 * @param parent            Parent.
 * @param child             Child.
 * @param accumulatedOffset Accumulated Offset.
 *
 * @author from https://stackoverflow.com/questions/21483188/scroll-to-a-specific-view-in-scroll-view
 */
fun getDeepChildOffset(
    mainParent: ViewGroup,
    parent: ViewParent,
    child: View,
    accumulatedOffset: Point
) {
    val parentGroup = parent as ViewGroup
    accumulatedOffset.x += child.left
    accumulatedOffset.y += child.top
    if (parentGroup == mainParent) {
        return
    }
    getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
}