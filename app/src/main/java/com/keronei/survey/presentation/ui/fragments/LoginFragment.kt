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

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.common.hash.Hashing
import com.keronei.survey.R
import com.keronei.survey.core.Constants.IS_LOGGED_IN_KEY
import com.keronei.survey.core.Constants.PASS_KEY
import com.keronei.survey.core.Constants.PHONE_NUMBER_KEY
import com.keronei.survey.databinding.LoginFragmentBinding
import com.keronei.survey.presentation.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.nio.charset.Charset
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val viewModel: LoginViewModel by activityViewModels()

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

                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                            sendEmail()
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

    private fun sendEmail() {

    }
}