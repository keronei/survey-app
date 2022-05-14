/*
 * Copyright 2022 Keronei Lincoln
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.survey.presentation.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.hash.Hashing
import com.keronei.survey.R
import com.keronei.survey.core.Constants.IS_LOGGED_IN_KEY
import com.keronei.survey.core.Constants.PASS_KEY
import com.keronei.survey.core.Constants.PHONE_NUMBER_KEY
import com.keronei.survey.databinding.LoginFragmentBinding
import com.keronei.survey.presentation.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val viewModel: LoginViewModel by activityViewModels()

    private val parser = SimpleDateFormat("dd.MM.yyyy at hh:mm a", Locale.US)

    private lateinit var loginBinding: LoginFragmentBinding

    @Inject
    lateinit var sharedPreference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loginBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        setOnClickListeners()

        return loginBinding.root
    }

    private fun setOnClickListeners() {
        loginBinding.btnLogin.setOnClickListener {
            if (loginBinding.phoneNumberInput.text.isNotEmpty()) {

                val providedPhone = loginBinding.phoneNumberInput.text.trim()

                val matcher =
                    Pattern.compile("^\\+254\\d{9}\$").matcher(providedPhone)

                if (matcher.matches()) {

                    if (loginBinding.passwordInput.text.isNotEmpty()) {

                        val providedPassword = loginBinding.passwordInput.text.trim()

                        val passwordHash =
                            Hashing.sha256().hashString(providedPassword, Charset.defaultCharset())
                                .padToLong()

                        val savedPassword = sharedPreference.getLong(PASS_KEY, 0L)

                        if (passwordHash == savedPassword) {

                            val editor = sharedPreference.edit()

                            editor.putBoolean(IS_LOGGED_IN_KEY, true)

                            editor.putString(PHONE_NUMBER_KEY, providedPhone.toString())

                            editor.apply()

                            notifyUserToSendMail(providedPhone.toString())
                        } else {
                            loginBinding.passwordInput.error = getString(R.string.incorrect_pass)

                            Toast.makeText(
                                context,
                                getString(R.string.incorrect_pass),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    loginBinding.phoneNumberInput.error =
                        getString(R.string.invalid_phone_message)

                    Toast.makeText(
                        context,
                        getString(R.string.invalid_phone_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun notifyUserToSendMail(phone: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(
                getString(R.string.email_sending_rationale)
            )
            .setPositiveButton(getString(R.string.lets_go)) { dialog, _ ->
                sendEmailAndNavigate(phone)
                dialog.dismiss()
            }
            .setTitle(getString(R.string.success))
            .setIcon(R.drawable.ic_baseline_send_24)
            .create()
            .show()
    }

    private fun sendEmailAndNavigate(phoneNumber: String) {
        val emailDispatchIntent = Intent(Intent.ACTION_SEND)
        emailDispatchIntent.type = "message/rfc822"

        emailDispatchIntent.putExtra(Intent.EXTRA_EMAIL, "tech@pula.io")
        emailDispatchIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))

        val emailBody = getString(
            R.string.email_body_template,
            getDeviceName(),
            parser.format(Calendar.getInstance().time),
            phoneNumber
        )

        emailDispatchIntent.putExtra(Intent.EXTRA_TEXT, emailBody)

        try {
            startActivity(Intent.createChooser(emailDispatchIntent, getString(R.string.send_via)))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                getString(R.string.no_email_client),
                Toast.LENGTH_SHORT
            ).show()
        }

        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

    }

    private fun getDeviceName(): String? {
        val manufacturer: String = Build.MANUFACTURER
        val model: String = Build.MODEL
        return if (model.lowercase(Locale.getDefault())
                .startsWith(manufacturer.lowercase(Locale.getDefault()))
        ) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }


    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }
}