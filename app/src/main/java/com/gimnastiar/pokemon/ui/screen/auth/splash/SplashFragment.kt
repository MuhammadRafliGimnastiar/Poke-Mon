package com.gimnastiar.pokemon.ui.screen.auth.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.ui.screen.core.CoreActivity
import com.gimnastiar.pokemon.utils.Empty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launchWhenResumed {
                viewModel.getUser().collectLatest {
                    val (token, user) = it
                    if (token == String.Empty) moveToLogin() else moveToCore()
                }

            }
        }, DURATION_FRAGMENT)
    }

    private fun moveToCore() {
        val intent = Intent(requireContext(), CoreActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun moveToLogin() {
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
    }

    companion object {
        private const val DURATION_FRAGMENT = 2000L
    }
}