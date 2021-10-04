package com.example.android.cats.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.example.android.cats.R
import com.example.android.cats.databinding.ListOfCatsFragmentBinding
import com.example.android.cats.isInternetAvailable
import com.example.android.cats.repositories.network.pojo.CatItem
import com.example.android.cats.ui.list.adapter.ListOfCatsAdapter
import com.example.android.cats.ui.list.adapter.ListOfCatsListener
import com.example.android.cats.ui.list.adapter.ListOfCatsLoaderStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListOfCatsFragment : Fragment(), ListOfCatsListener {
    private var _binding: ListOfCatsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListOfCatsViewModel by viewModels()
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { ListOfCatsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListOfCatsFragmentBinding.inflate(inflater, container, false)

        adapter.addLoadStateListener { state ->
            with(binding) {
                recyclerView.isVisible = state.refresh != LoadState.Loading
                progressBar.isVisible = state.refresh == LoadState.Loading
            }
        }

        binding.apply {
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = ListOfCatsLoaderStateAdapter(),
                footer = ListOfCatsLoaderStateAdapter()
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isNotInternetDialog()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(
                requireContext(),
            )
                .setMessage(resources.getString(R.string.alert_dialog_on_back_message))
                .setNegativeButton(resources.getString(R.string.alert_dialog_botton_yes))
                { _, _ ->
                    ActivityCompat.recreate(requireActivity())
                }
                .setPositiveButton(resources.getString(R.string.alert_dialog_botton_no))
                { _, _ ->
                    ActivityCompat.finishAffinity(requireActivity())
                }
                .show()
        }
    }

    override fun onClickItem(cat: CatItem) {
        if (!isInternetAvailable(requireContext().applicationContext)) {
            isNotInternetDialog()
        } else {
            view?.findNavController()
                ?.navigate(
                    ListOfCatsFragmentDirections
                        .actionListOfCatsFragmentToDetailFragment(cat.id)
                )
        }
    }

    private fun isNotInternetDialog() {
        if (!isInternetAvailable(requireContext().applicationContext)) {
            MaterialAlertDialogBuilder(
                requireContext(),
            )
                .setMessage(resources.getString(R.string.alert_dialog_check_internet_message))
                .setNegativeButton(resources.getString(R.string.alert_dialog_botton_left))
                { _, _ ->
                    ActivityCompat.recreate(requireActivity())
                }
                .setPositiveButton(resources.getString(R.string.alert_dialog_botton_right))
                { _, _ ->
                    ActivityCompat.finishAffinity(requireActivity())
                }
                .show()

            onPause()
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.cats.collectLatest(adapter::submitData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
