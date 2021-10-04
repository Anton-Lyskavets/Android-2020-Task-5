package com.example.android.cats.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.android.cats.databinding.CatItemBinding
import com.example.android.cats.repositories.network.pojo.CatItem

class ListOfCatsAdapter(
    private val listener: ListOfCatsListener
) : PagingDataAdapter<CatItem, ListOfCatsViewHolder>(ListOfCatsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOfCatsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CatItemBinding.inflate(layoutInflater, parent, false)
        return ListOfCatsViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ListOfCatsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
