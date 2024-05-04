package com.yusufaltas.caseapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yusufaltas.caseapp.data.repository.main.CaseRepository
import com.yusufaltas.caseapp.data.repository.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val apiRepository: CaseRepository) : ViewModel() {

    private val _maps = MutableLiveData<Resource<Boolean>>()
    val maps: LiveData<Resource<Boolean>> = _maps
}