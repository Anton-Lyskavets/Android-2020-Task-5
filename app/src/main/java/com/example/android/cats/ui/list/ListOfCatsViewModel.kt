package com.example.android.cats.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.cats.repositories.CatsRepository
import com.example.android.cats.repositories.network.pojo.CatItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ListOfCatsViewModel : ViewModel() {

    private val repository by lazy { CatsRepository() }

    private var _cats = repository.getCats()
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    val cats: StateFlow<PagingData<CatItem>>
        get() = _cats
}
