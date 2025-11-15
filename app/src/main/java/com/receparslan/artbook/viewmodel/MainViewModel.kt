package com.receparslan.artbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.receparslan.artbook.model.Art
import com.receparslan.artbook.model.ImageUrls
import com.receparslan.artbook.repository.ArtRepositoryInterface
import com.receparslan.artbook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val artRepository: ArtRepositoryInterface
) : ViewModel() {
    val artList = artRepository.getAllArts()

    private val _imageUrls = MutableLiveData<Resource<List<ImageUrls>>>(Resource.none())
    val imageUrls: LiveData<Resource<List<ImageUrls>>>
        get() = _imageUrls

    private val _selectedImageUrls = MutableLiveData<ImageUrls>()
    val selectedImageUrls: LiveData<ImageUrls>
        get() = _selectedImageUrls

    private val _status = MutableLiveData(mapOf("field" to "", "message" to ""))
    val status: LiveData<Map<String, String>>
        get() = _status

    fun resetStatus() {
        _status.postValue(mapOf("field" to "", "message" to ""))
    }

    fun resetImageUrls() {
        _imageUrls.postValue(Resource.none())
    }

    fun setSelectedImageUrls(urls: ImageUrls) {
        _selectedImageUrls.postValue(urls)
    }

    fun searchImage(query: String = "") {
        if (query.isEmpty())
            return

        _imageUrls.value = Resource.loading()

        viewModelScope.launch(Dispatchers.IO) {
            _imageUrls.postValue(artRepository.getArtUrlList(query))
        }
    }

    fun makeArt(art: Art) {
        when (true) {
            art.name.isEmpty() -> _status.postValue(mapOf("field" to "name", "message" to "Please enter a name for the art."))

            art.artist.isEmpty() -> _status.postValue(mapOf("field" to "artist", "message" to "Please enter an artist name for the art."))

            art.year.isEmpty() -> _status.postValue(mapOf("field" to "year", "message" to "Please enter a year for the art."))

            art.imageUrl.isEmpty() -> _status.postValue(mapOf("field" to "image", "message" to "Please select an image for the art."))

            else -> {
                _status.postValue(mapOf("field" to "success", "message" to "Art created successfully!"))
                viewModelScope.launch(Dispatchers.IO) { insertArt(art) }
            }
        }
    }

    fun insertArt(art: Art) = viewModelScope.launch(Dispatchers.IO) {
        artRepository.insertArt(art)
    }

    fun deleteArt(art: Art) = viewModelScope.launch(Dispatchers.IO) {
        artRepository.deleteArt(art)
    }
}