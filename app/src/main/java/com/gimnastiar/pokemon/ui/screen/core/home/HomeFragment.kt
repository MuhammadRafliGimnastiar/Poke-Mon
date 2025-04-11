package com.gimnastiar.pokemon.ui.screen.core.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gimnastiar.pokemon.R
import com.gimnastiar.pokemon.databinding.FragmentHomeBinding
import com.gimnastiar.pokemon.domain.model.Pokemon
import com.gimnastiar.pokemon.domain.model.PokemonList
import com.gimnastiar.pokemon.ui.adapter.LoadingStateAdapter
import com.gimnastiar.pokemon.ui.adapter.PokemonListAdapter
import com.gimnastiar.pokemon.ui.adapter.PokemonResultListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var isScrollingDown = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observePokemon()
        setupScrollToTopButton()
    }

    private fun observePokemon() = with(binding) {
        val pokemonAdapter = PokemonListAdapter()
        pokemonAdapter.setOnItemClickCallback(object: PokemonListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PokemonList) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(url = data.url, pokemon = null)
                findNavController().navigate(action)
                Log.i("HOME FRAGMENT", "Action to DETAIL FRAGMENT")

            }

        })

        rvPokemon.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvPokemon.adapter = pokemonAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                pokemonAdapter.retry()
            }
        )

        lifecycleScope.launchWhenStarted {
            viewModel.getPokemon.collectLatest {
                pokemonAdapter.submitData(it)
            }
        }
    }

    private fun setupScrollToTopButton() = with(binding) {

        rvPokemon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                if (dy > 10 && !isScrollingDown) {
                    isScrollingDown = true
                    btnScrollToTop.show()
                }

                if (dy < -10 && isScrollingDown) {
                    isScrollingDown = false
                    btnScrollToTop.hide()
                }
            }
        })

        btnScrollToTop.setOnClickListener {
            rvPokemon.smoothScrollToPosition(0)
        }
    }

}