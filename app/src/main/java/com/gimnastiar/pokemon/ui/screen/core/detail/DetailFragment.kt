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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.data.Resource
import com.gimnastiar.pokemon.databinding.FragmentDetailBinding
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.ui.adapter.AbilitiesListAdapter
import com.gimnastiar.pokemon.utils.Helper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemon: Pokemon? = arguments?.getParcelable("pokemon")
        val url: String? = arguments?.getString("url")

        if (url != null) {
            //hit ViewModel
            setupDataUrl(url)
        } else if (pokemon != null) {
            setupDataPokemon(pokemon)
        } else {
            findNavController().navigateUp()
        }

        backAction()
    }

    private fun backAction() = with(binding) {
        topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupDataUrl(url: String) {
        viewModel.getPokemon(url).observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Loading -> isLoading(true)
                is Resource.Success -> {
                    if (it.data != null) {
                        setupDataPokemon(it.data)
                        isLoading(false)
                    }
                }
                is Resource.Error -> isLoading(true)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDataPokemon(pokemon: Pokemon) = with(binding.includedDetail) {
        tvName.text = pokemon.name
        tvHeight.text = pokemon.height.toString() + " cm"
        tvWeight.text = pokemon.weight.toString() + " kg"
        if (Helper.isConnected(requireContext())) {
            Glide.with(requireContext())
                .load(pokemon.imageUrl)
                .into(imgPokemon)
        } else {
            imgPokemon.setImageResource(R.drawable.ic_ball_poke)
        }

        val adapter = AbilitiesListAdapter(pokemon.abilities.filterNotNull())
        val layoutManager = GridLayoutManager(requireContext(), 2)

        rvAbilities.adapter = adapter
        rvAbilities.layoutManager = layoutManager

    }

    private fun isLoading(loading: Boolean) = with(binding) {
        if(loading) {
            shimmer.visibility = View.VISIBLE
            includedDetail.cardParent.visibility = View.GONE
        } else {
            shimmer.visibility = View.GONE
            includedDetail.cardParent.visibility = View.VISIBLE
        }
    }

}