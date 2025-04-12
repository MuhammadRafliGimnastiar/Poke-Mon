package com.gimnastiar.pokemon.ui.screen.core.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
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
import com.gimnastiar.pokemon.ui.adapter.LocalPokemonAdapter
import com.gimnastiar.pokemon.ui.adapter.PokemonListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var isScrollingDown = false
    private var doubleBackPressed = false

    private lateinit var pokemonLocalList: ArrayList<Pokemon>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupScrollToTopButton()
        backAction()
        observeConnection()
        bottomNavShowHandler()
        viewModel.checkConnectingData(requireContext(), viewLifecycleOwner)
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkConnectingData(requireContext(), viewLifecycleOwner)
    }

    private fun observeConnection() {
        viewModel.isConnected.observe(viewLifecycleOwner) {
            if(it) {
                showLoading(false)
                observePokemon()
            } else {
                showLoading(false)
                observeLocalPokemon()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun observeLocalPokemon() {
        lifecycleScope.launchWhenStarted {
            viewModel.getLocalPokemon.collectLatest {
                if (it.isNotEmpty()) {
                    setupListLocal(it)
                } else {
                    showLoading(true)
                    Toast.makeText(requireContext(),
                        getString(R.string.please_open_some_data), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(b: Boolean) = with(binding) {
        shimmer.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun setupListLocal(data: List<Pokemon>) = with(binding) {
        val adapterLocal = LocalPokemonAdapter()
        adapterLocal.setOnItemClickCallback(object: LocalPokemonAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Pokemon) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(url = null, pokemon = data)
                findNavController().navigate(action)
            }
        })
        adapterLocal.submitList(data)

        rvPokemon.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvPokemon.adapter = adapterLocal

        val adapterSearch = LocalPokemonAdapter()
        adapterSearch.setOnItemClickCallback(object: LocalPokemonAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Pokemon) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(url = null, pokemon = data)
                findNavController().navigate(action)
            }
        })

        // SETUP SEARCH FOR LOCAL DATA
        pokemonLocalList = data as ArrayList
        rvSearch.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvSearch.adapter = adapterSearch

        searchView.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()

                val filteredList: ArrayList<Pokemon> = ArrayList()
                if (text != null)
                    for (item in pokemonLocalList) {
                        if (item.name.lowercase(Locale.getDefault())
                                .contains(text.lowercase(Locale.getDefault()))
                        ) filteredList.add(item)
                    }

                if (filteredList.isNotEmpty()) adapterSearch.filterList(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    @Suppress("DEPRECATION")
    private fun observePokemon() = with(binding) {
        val pokemonAdapter = PokemonListAdapter()
        val pokemonAdapterSearch = PokemonListAdapter()
        pokemonAdapter.setOnItemClickCallback(object: PokemonListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PokemonList) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(url = data.url, pokemon = null)
                findNavController().navigate(action)
            }
        })
        pokemonAdapterSearch.setOnItemClickCallback(object: PokemonListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PokemonList) {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(url = data.url, pokemon = null)
                findNavController().navigate(action)
            }
        })

        //home rv
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

        //search rv
        binding.searchView.editText.doOnTextChanged { text, _, _, _ ->
            viewModel.setSearchQuery(text.toString())
        }
        rvSearch.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvSearch.adapter = pokemonAdapterSearch.withLoadStateFooter(
            footer = LoadingStateAdapter{
                pokemonAdapter.retry()
            }
        )

        lifecycleScope.launchWhenStarted {
            viewModel.getPokemonList.collectLatest {
                pokemonAdapterSearch.submitData(it)
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

    private fun backAction() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                with(binding) {
                    if (searchView.isShowing) {
                        searchView.hide()

                    } else if (doubleBackPressed) {
                        requireActivity().finish()
                    } else {
                        doubleBackPressed = true
                        Toast.makeText(requireContext(),
                            getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            doubleBackPressed = false
                        }, 2000)
                    }
                }
            }
        })
    }

    private fun bottomNavShowHandler() = with(binding) {
        searchView.addTransitionListener { _, _, newState ->
            when (newState) {
                SearchView.TransitionState.SHOWN -> hideBottomNav(true)
                SearchView.TransitionState.HIDDEN -> hideBottomNav(false)
                else -> {}
            }

        }
    }

    private fun hideBottomNav(isHide: Boolean) {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        if (isHide) {
            bottomNav.animate()
                .translationY(bottomNav.height.toFloat())
                .alpha(0f)
                .setDuration(200)
                .withEndAction { bottomNav.visibility = View.GONE }
                .start()
        } else {
            bottomNav.visibility = View.VISIBLE
            bottomNav.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(200)
                .start()
        }
    }

}