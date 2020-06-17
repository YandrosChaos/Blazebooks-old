package com.blazebooks.ui.customdialogs.forgotpassword

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.blazebooks.R
import com.blazebooks.databinding.DialogForgotPasswdBinding
import com.blazebooks.util.hideKeyboard
import com.blazebooks.util.snackbar
import com.google.android.gms.common.api.ApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_forgot_passwd.*
import kotlinx.android.synthetic.main.dialog_forgot_passwd.view.*
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * Creates a new instance of ForgotPasswdDialog.
 *
 * @author Victor Gonzalez
 */
class ForgotPasswdDialog : DialogFragment(), KodeinAware {

    override val kodein by kodein()
    private val factory by instance<ForgotPasswdViewModelFactory>()
    private lateinit var viewModel: ForgotPasswdViewModel
    private lateinit var activityListener: ForgotPasswdDialogListener
    private lateinit var binding: DialogForgotPasswdBinding
    private lateinit var dialog: ForgotPasswdDialog

    /**
     * Communicates this dialog with the host activity. If
     * host not implements ForgotPasswdDialogListener, then
     * throws ClassCastException.
     *
     * @author Victor Gonzalez
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityListener = context as ForgotPasswdDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ForgotPasswdDialogListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_forgot_passwd, container, false)
        viewModel = ViewModelProvider(this, factory).get(ForgotPasswdViewModel::class.java)
        return binding.root
    }

    /**
     * Sets the onClickListeners to the buttons.
     *
     * @author Victor Gonzalez
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //close button listener
        view.forgotPasswdBtnExit.setOnClickListener {
            activityListener.onCloseForgotPasswdDialog(this)
        }
        view.forgotPasswdBtn.setOnClickListener {
            sendEmail(it)
            hideKeyboard()
        }
        dialog = this
    }

    /**
     * if EditText is empty or the email is not well-formatted, returns an error message.
     * Else sends the email, and communicate the info to host activity. If it cannot send
     * the email, then communicates a fail message to the host.
     *
     * @author Victor Gonzalez
     */
    private fun sendEmail(view: View) {
        val email = binding.forgotpwdDialogUserName.text.toString().trim()

        //validations
        if (email.isEmpty()) {
            forgotpwdDialogUserName.error = getString(R.string.log_email_error)
            forgotpwdDialogUserName.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgotpwdDialogUserName.error = getString(R.string.signin_email_not_valid)
            forgotpwdDialogUserName.requestFocus()
            return
        }
        dialogLoadingSKV.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                viewModel.sendEmail(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //success
                        activityListener.onForgotPasswdSent(dialog)
                    }, {
                        //failure
                        view.snackbar(it.message!!)
                    })
            } catch (e: ApiException) {
                view.snackbar("Check your internet connection please.")
            }
        }
        dialogLoadingSKV.visibility = View.GONE
    }

}