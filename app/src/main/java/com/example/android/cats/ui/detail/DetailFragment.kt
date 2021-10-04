package com.example.android.cats.ui.detail

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.android.cats.R
import com.example.android.cats.checkPermission
import com.example.android.cats.databinding.DetailFragmentBinding
import com.example.android.cats.repositories.network.pojo.BreedItem
import com.example.android.cats.repositories.network.pojo.CatItem
import com.example.android.cats.saveImageToGallery
import com.example.android.cats.setPicture
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val REQUEST_CODE = 111

class DetailFragment : Fragment() {
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val args by lazy { DetailFragmentArgs.fromBundle(requireArguments()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.cat.collectLatest {
                setPicture(binding.image, it.url)
                setValues(it)
            }
        }
        viewModel.setCat(args.id.toString())
    }

    private fun setValues(catItem: CatItem) {
        val info = catItem.breeds?.get(0)
        binding.apply {
            valueName.text = info?.name ?: ""
            valueDescription.text = info?.description ?: ""
            valueOrigin.text = info?.origin ?: ""
            valueTemperament.text = info?.temperament ?: ""
            valueLifeSpan.text = info?.lifeSpan ?: ""
            ratingBarAdaptability.rating = info?.adaptability?.toFloat() ?: 0f
            ratingBarChildFriendly.rating = info?.childFriendly?.toFloat() ?: 0f
            ratingBarGrooming.rating = info?.grooming?.toFloat() ?: 0f
            ratingBarHealthIssues.rating = info?.healthIssues?.toFloat() ?: 0f
            ratingBarIntelligence.rating = info?.intelligence?.toFloat() ?: 0f
            ratingBarStrangerFriendly.rating = info?.strangerFriendly?.toFloat() ?: 0f
            imageButtonWikipedia.setOnClickListener { toWikipedia(info) }
            toolbar.apply {
                title = info?.name ?: ""
                setOnMenuItemClickListener { onItemMenuClick(it, catItem) }
                setNavigationOnClickListener { requireActivity().onBackPressed() }
            }
        }
    }

    private fun onItemMenuClick(menuItem: MenuItem, catItem: CatItem): Boolean {
        if (menuItem.itemId == R.id.download) {
            saveCatImage(catItem)
        }
        return true
    }

    private fun saveCatImage(catItem: CatItem) {
        val bitmapDrawable: BitmapDrawable = binding.image.drawable as BitmapDrawable
        val bitmap: Bitmap = bitmapDrawable.bitmap

        if (checkPermission(requireContext())) {
            saveImageToGallery(bitmap, catItem.id, requireContext().applicationContext)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE
            )
        }
    }

    private fun toWikipedia(info: BreedItem?) {
        val uri = Uri.parse(info?.wikipediaUrl)
        uri.let {
            val intents = Intent(Intent.ACTION_VIEW, uri)
            requireContext().startActivity(intents)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
