package com.gimnastiar.pokemon.ui.screen.auth.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.data.LoginResult
import com.gimnastiar.pokemon.databinding.FragmentLoginBinding
import com.gimnastiar.pokemon.ui.screen.core.CoreActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private var doubleBackPressed = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        observeValidation()
        onLogin()
        onRegistClick()
        backAction()
    }

    private fun onRegistClick() {
        val fullText = getString(R.string.move_to_regist_text)
        val spannable = SpannableString(fullText)

        val targetWord = "Register"
        val startIndex = fullText.indexOf(targetWord)
        val endIndex = startIndex + targetWord.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_loginFragment_to_registFragment)
                Log.i("NAV HOME", "TO REGIST")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(requireContext(), R.color.blue_color)
            }
        }

        spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        with(binding) {
            btnRegist.text = spannable
            btnRegist.movementMethod = LinkMovementMethod.getInstance()
            btnRegist.highlightColor = Color.TRANSPARENT
        }
    }

    private fun onLogin() = with(binding) {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassowrd.text.toString()

            viewModel.login(email, password)
            observeLogin()
        }
    }

    private fun observeLogin() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginResult.collectLatest {
                when (it) {
                    is LoginResult.UserNotFound -> showToast(getString(R.string.account_not_found))
                    is LoginResult.WrongPassword -> showToast(getString(R.string.your_password_is_wrong))
                    is LoginResult.Success -> {
                        viewModel.saveSession(it.user)
                        showToast(getString(R.string.login_success))

                        // navigation
                        val intent = Intent(requireContext(), CoreActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("CheckResult")
    private fun observeValidation() {
        val emailStream = validateEmail()
        emailStream.subscribe { isValid ->
            showEmailAlert(isValid)
        }

        val passwordStream = validatePassword()
        passwordStream.subscribe { isValid ->
            showPasswordAlert(isValid)
        }

        Observable.combineLatest(
            emailStream,
            passwordStream
        ) { isEmailValid, isPasswordValid -> isEmailValid && isPasswordValid}
            .subscribe { isValid ->
                with(binding.btnLogin) {
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

    private fun validatePassword(): Observable<Boolean> {
        return RxTextView.textChanges(binding.etPassowrd)
            .skipInitialValue()
            .map { password ->
                password.isNotEmpty()
            }
    }

    private fun validateEmail(): Observable<Boolean> {
        return RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
    }

    private fun showEmailAlert(isValid: Boolean) = with(binding) {
        tfEmail.error = if (isValid) null else {
            getString(R.string.invalid_email)
        }
        tfEmail.isErrorEnabled = if (isValid) false else true
    }

    private fun showPasswordAlert(isValid: Boolean) = with(binding) {
        tfPassword.error = if (isValid) null else {
            getString(R.string.invalid_password)
        }
        tfPassword.isErrorEnabled = if (isValid) false else true
    }

    private fun backAction() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackPressed) {
                    requireActivity().finish()
                } else {
                    doubleBackPressed = true
                    Toast.makeText(requireContext(), "press again to exit", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        doubleBackPressed = false
                    }, 2000)
                }
            }
        })
    }

}