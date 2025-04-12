package com.gimnastiar.pokemon.ui.screen.auth.regist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.databinding.FragmentRegistBinding
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.ui.screen.core.CoreActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable

@AndroidEntryPoint
class RegistFragment : Fragment(R.layout.fragment_regist) {

    private var _binding : FragmentRegistBinding? = null
    private val binding get() = _binding!!

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
            val user = User(
                name = fullName,
                email = email,
                id = null
            )

            viewModel.regist(
                user = user,
                password = password
            )
            observeSignUp(user)
        }
    }

    private fun observeSignUp(user: User) {
        viewModel.registResponse.observe(viewLifecycleOwner) {
            if ( it == (-1).toLong()) {
                showToast(getString(R.string.your_email_has_registered))
            } else {
                viewModel.saveSession(user)

                // navigation
                val intent = Intent(requireContext(), CoreActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun onBackPress() = with(binding) {
        topAppBar.setNavigationOnClickListener {
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
        val passwordPattern = getString(R.string.pass_pattern).toRegex()

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
        tfFullName.isErrorEnabled = !isValid
    }

    private fun showEmailAlert(isValid: Boolean) = with(binding) {
        tfEmail.error = if (isValid) null else {
            getString(R.string.invalid_email)
        }
        tfEmail.isErrorEnabled = !isValid
    }

    private fun showPasswordAlert(isValid: Boolean) = with(binding) {
        tfPassword.error = if (isValid) null else {
            getString(R.string.invalid_password)
        }
        tfPassword.isErrorEnabled = !isValid
    }

    private fun showConfirmPasswordAlert(isValid: Boolean) = with(binding) {
        tfConfirmPass.error = if (isValid) null else {
            getString(R.string.invalid_confirm_password)
        }
        tfConfirmPass.isErrorEnabled = !isValid
    }

}