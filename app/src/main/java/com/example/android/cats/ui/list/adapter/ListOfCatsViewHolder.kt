package com.example.android.cats.ui.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.android.cats.databinding.CatItemBinding
import com.example.android.cats.repositories.network.pojo.CatItem
import com.example.android.cats.setPicture

class ListOfCatsViewHolder(
    private val binding: CatItemBinding,
    private val listener: ListOfCatsListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private var imgUrl: String? = ""

    fun bind(cat: CatItem?) {
        imgUrl = cat?.url
        setPicture(binding.image, imgUrl)
        initButtonsListeners(cat)
    }

    private fun initButtonsListeners(cat: CatItem?) {
        binding.cardItem.setOnClickListener {
            cat?.let { it1 -> listener.onClickItem(it1) }
        }
    }
}
