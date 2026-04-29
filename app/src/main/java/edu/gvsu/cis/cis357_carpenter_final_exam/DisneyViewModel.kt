package edu.gvsu.cis.cis357_carpenter_final_exam.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.gvsu.cis.cis357_carpenter_final_exam.data.db.AppDatabase
import edu.gvsu.cis.cis357_carpenter_final_exam.data.db.StarredCharacterEntity
import edu.gvsu.cis.cis357_carpenter_final_exam.data.model.DisneyCharacter
import edu.gvsu.cis.cis357_carpenter_final_exam.data.network.DisneyApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DisneyViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).starredCharacterDao()

    val starredIds: StateFlow<List<Int>> = MutableStateFlow<List<Int>>(emptyList()).also { flow ->
        viewModelScope.launch {
            dao.getAllStarred().collect { list ->
                (flow as MutableStateFlow).value = list.map { it.id }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _characters = MutableStateFlow<List<DisneyCharacter>>(emptyList())
    val characters: StateFlow<List<DisneyCharacter>> = _characters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _nextPage = MutableStateFlow<String?>(null)
    val nextPage: StateFlow<String?> = _nextPage

    private val _prevPage = MutableStateFlow<String?>(null)
    val prevPage: StateFlow<String?> = _prevPage

    init {
        fetchCharacters()
    }

    fun fetchCharacters(url: String = "https://api.disneyapi.dev/character") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = DisneyApiService.getCharacters(url)
                _characters.value = response.data
                _nextPage.value = response.info?.nextPage
                _prevPage.value = response.info?.previousPage
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            fetchCharacters()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = DisneyApiService.searchByName(query)
                if (response.data.isNotEmpty()) {
                    _characters.value = response.data
                    _nextPage.value = response.info?.nextPage
                    _prevPage.value = response.info?.previousPage
                } else {
                    val filmResponse = DisneyApiService.searchByFilm(query)
                    _characters.value = filmResponse.data
                    _nextPage.value = filmResponse.info?.nextPage
                    _prevPage.value = filmResponse.info?.previousPage
                }
            } catch (e: Exception) {
                _errorMessage.value = "Search failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sortByName() {
        _characters.value = _characters.value.sortedBy { it.name }
    }

    fun sortByFilmCountDesc() {
        _characters.value = _characters.value.sortedByDescending { it.films.size }
    }

    fun toggleStar(character: DisneyCharacter) {
        viewModelScope.launch {
            val isCurrentlyStarred = dao.isStarred(character.id) > 0
            if (isCurrentlyStarred) {
                dao.deleteById(character.id)
            } else {
                dao.insert(
                    StarredCharacterEntity(
                        id = character.id,
                        name = character.name,
                        imageUrl = character.imageUrl
                    )
                )
            }
        }
    }
}