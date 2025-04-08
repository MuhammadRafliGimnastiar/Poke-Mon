package com.gimnastiar.pokemon.ui.screen.auth.login

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.databinding.FragmentLoginBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        observeValidation()
        onLogin()
        onRegistClick()
    }

    private fun onRegistClick() {
        val fullText = getString(R.string.move_to_regist_text)
        val spannable = SpannableString(fullText)

        val targetWord = "Register"
        val startIndex = fullText.indexOf(targetWord)
        val endIndex = startIndex + targetWord.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // TODO: Pindah ke halaman register
//                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false // remove underline
                ds.color = ContextCompat.getColor(requireContext(), R.color.blue_500)
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
            //check to room, move to core act
        }
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
                        if(isValid) androidx.appcompat.R.attr.colorPrimary else ContextCompat.getColor(requireContext(), R.color.gray)
                    )
                }
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
    }

    private fun showPasswordAlert(isValid: Boolean) = with(binding) {
        tfPassword.error = if (isValid) null else {
            getString(R.string.invalid_password)
        }
    }

}