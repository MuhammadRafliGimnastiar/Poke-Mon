package com.gimnastiar.pokemon.ui.screen.auth.regist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.databinding.FragmentRegistBinding
import com.gimnastiar.pokemon.domain.model.User
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RegistFragment : Fragment(R.layout.fragment_regist) {

    private var _binding : FragmentRegistBinding? = null
    private val binding get() = _binding!!
    private var jobSignUp: Job? = null

    private val viewModel: RegistViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegistBinding.bind(view)

        observeValidation()
        onBackPress()
        onSignUp()

    }

    private fun onSignUp() = with(binding) {
        btnRegist.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassowrd.text.toString()
            viewModel.regist(
                user = User(
                    name = fullName,
                    email = email,
                    id = null
                ),
                password = password
            )
        }
        observeSignUp()
    }

    private fun observeSignUp() {
        jobSignUp = lifecycleScope.launchWhenStarted {
            viewModel.registResponse.observe(viewLifecycleOwner) {
                if ( it == (-1).toLong()) {
                    Log.i("REGIST RESPONSE", "EMAIL TERDAFTAR")
                } else {
                    Log.i("REGIST RESPONSE", "SUCCESS")
                }
            }
        }
    }

    private fun onBackPress() = with(binding) {
        ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("CheckResult")
    private fun observeValidation() {
        val fullNameStream = validateFullName()
        fullNameStream.subscribe { isValid ->
            showFullNameAlert(isValid)
        }

        val emailStream = validateEmail()
        emailStream.subscribe { isValid ->
            showEmailAlert(isValid)
        }

        val passwordStream = validatePassword()
        passwordStream.subscribe { isValid ->
            showPasswordAlert(isValid)
        }

        val confirmPasswordStream = validateConfirmPassword()
        confirmPasswordStream.subscribe { isValid ->
            showConfirmPasswordAlert(isValid)
        }

        Observable.combineLatest(
            fullNameStream,
            emailStream,
            passwordStream,
            confirmPasswordStream
        ) { isFullNameValid, isEmailValid, isPasswordValid, isConfirmPasswordValid
            -> isFullNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid
        }
            .subscribe { isValid ->
                with(binding.btnRegist) {
                    isEnabled = isValid
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            if (isValid) R.color.blue_color else R.color.grey
                        )
                    )
                }
            }
    }

    private fun validateFullName(): Observable<Boolean> {
        return RxTextView.textChanges(binding.etFullName)
            .skipInitialValue()
            .map { fullName ->
                fullName.isNotEmpty()
            }
    }

    private fun validatePassword(): Observable<Boolean> {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()

        return RxTextView.textChanges(binding.etPassowrd)
            .skipInitialValue()
            .map { password ->
                passwordPattern.matches(password.toString())
            }
    }

    private fun validateConfirmPassword(): Observable<Boolean> {
        return Observable.combineLatest(
            RxTextView.textChanges(binding.etPassowrd).skipInitialValue(),
            RxTextView.textChanges(binding.etConfirmPass).skipInitialValue()
        ) { password, confirmPassword ->
            confirmPassword.toString() == password.toString() && confirmPassword.isNotEmpty()
        }
    }

    private fun validateEmail(): Observable<Boolean> {
        return RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
    }

    private fun showFullNameAlert(isValid: Boolean) = with(binding) {
        tfFullName.error = if (isValid) null else {
            getString(R.string.invalid_name)
        }
        tfFullName.isErrorEnabled = if (isValid) true else false
    }

    private fun showEmailAlert(isValid: Boolean) = with(binding) {
        tfEmail.error = if (isValid) null else {
            getString(R.string.invalid_email)
        }
        tfEmail.isErrorEnabled = if (isValid) true else false
    }

    private fun showPasswordAlert(isValid: Boolean) = with(binding) {
        tfPassword.error = if (isValid) null else {
            getString(R.string.invalid_password)
        }
        tfPassword.isErrorEnabled = if (isValid) true else false
    }

    private fun showConfirmPasswordAlert(isValid: Boolean) = with(binding) {
        tfConfirmPass.error = if (isValid) null else {
            getString(R.string.invalid_confirm_password)
        }
        tfConfirmPass.isErrorEnabled = if (isValid) true else false
    }

}