package com.example.android.cats.ui.list.adapter

import com.example.android.cats.repositories.network.pojo.CatItem

interface ListOfCatsListener {
    fun onClickItem(cat: CatItem)
}
