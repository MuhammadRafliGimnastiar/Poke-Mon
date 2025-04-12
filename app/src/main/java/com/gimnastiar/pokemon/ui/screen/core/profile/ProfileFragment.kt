package com.gimnastiar.pokemon.ui.screen.core.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.gimnastiar.pokemon.MainActivity
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.databinding.FragmentProfileBinding
import com.gimnastiar.pokemon.domain.model.User
import com.gimnastiar.pokemon.ui.screen.core.CoreActivity
import com.gimnastiar.pokemon.utils.Empty
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        getUserData()
        actionLogout()
    }

    private fun actionLogout() = with(binding.cardProfile) {
        btnLogout.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.confirm_logout))
                .setMessage(resources.getString(R.string.make_sure_logout))
                .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.logout)) { dialog, which ->
                    viewModel.deleteUser()
                    Toast.makeText(requireContext(), "You has been logged out", Toast.LENGTH_SHORT).show()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }.show()
        }

    }

    private fun getUserData() {
        lifecycleScope.launchWhenStarted {
            viewModel.getUser().collectLatest {
                val (token, user) = it
//                this@ProfileFragment.token = token
//                this@ProfileFragment.user = user
                setupDataProfile(user)
            }
        }
    }

    private fun setupDataProfile(user: User) = with(binding.cardProfile) {
        if (user.name != String.Empty){
            tvName.text = user.name
            tvEmail.text = user.email
        }
    }

}