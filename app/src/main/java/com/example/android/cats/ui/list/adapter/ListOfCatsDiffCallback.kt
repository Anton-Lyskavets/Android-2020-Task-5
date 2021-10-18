package com.example.android.cats.ui.list.adapter

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.example.android.cats.repositories.network.pojo.CatItem

class ListOfCatsDiffCallback :
    DiffUtil.ItemCallback<CatItem>() {

    override fun areItemsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
        return oldItem == newItem
    }

    @Nullable
    @Override
    override fun getChangePayload(oldItem: CatItem, newItem: CatItem): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}
