package com.example.android.cats.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.cats.repositories.CatsRepository
import com.example.android.cats.repositories.network.pojo.CatItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val repository: CatsRepository by lazy { CatsRepository() }

    private var _cat = MutableStateFlow(CatItem())
    val cat: MutableStateFlow<CatItem>
        get() = _cat

    fun setCat(id: String) {
        viewModelScope.launch {
            _cat.value = repository.getCatsForId(id)
        }
    }
}
