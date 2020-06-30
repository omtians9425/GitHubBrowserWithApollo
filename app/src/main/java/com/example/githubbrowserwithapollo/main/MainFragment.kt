package com.example.githubbrowserwithapollo.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.githubbrowserwithapollo.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var binding: FragmentMainBinding
    private val controller = GitHubRepositoryController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        binding.epoxyRecyclerView.setController(controller)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            Timber.i("$viewState")
            viewState?.repoData?.let { controller.setData(it) }
        })

        mainViewModel.viewEffect.observe(viewLifecycleOwner, Observer { viewEffect ->
            viewEffect ?: return@Observer
            Timber.i("$viewEffect")
            when (viewEffect) {
                is MainScreen.ViewEffect.ErrorSnackbarEffect -> {
                    Snackbar.make(binding.root, viewEffect.error.errorResId, Snackbar.LENGTH_SHORT).show()
                }
            }
        })

    }
}