package com.gimnastiar.pokemon.ui.screen.core.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.data.Resource
import com.gimnastiar.pokemon.databinding.FragmentDetailBinding
import com.gimnastiar.pokemon.domain.model.Pokemon
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

//    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemon: Pokemon? = arguments?.getParcelable("pokemon")
        val url: String? = arguments?.getString("url")

        if (url != null) {
            //hitViewModel
            setupDataUrl(url)
        } else if (pokemon != null) {
            setupDataPokemon(pokemon)
        } else {
            findNavController().navigateUp()
        }

    }

    private fun setupDataUrl(url: String) {
        viewModel.getPokemon(url).observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null)
                    setupDataPokemon(it.data)
                }
                is Resource.Error -> {}
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDataPokemon(pokemon: Pokemon) = with(binding.includedDetail) {
        tvName.text = pokemon.name
        Glide.with(requireContext())
            .load(pokemon.imageUrl)
            .into(imgPokemon)
        tvHeight.text = pokemon.height.toString() + " cm"
        tvWeight.text = pokemon.weight.toString() + " kg"

        //setup adapter untuk abilities
    }

}