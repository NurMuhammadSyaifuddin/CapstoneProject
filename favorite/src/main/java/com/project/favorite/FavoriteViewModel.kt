package com.project.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.core.domain.model.Movie
import com.project.core.domain.usecase.movie.MovieUseCase
import com.project.core.domain.usecase.tvshow.TvShowUseCase

class FavoriteViewModel(private val movieUseCase: MovieUseCase, private val tvShowUseCase: TvShowUseCase): ViewModel() {

    fun getFavoritesMovie(): LiveData<List<Movie>> = movieUseCase.getFavoriteMovies().asLiveData()

    fun getFavoriteTvShow() = tvShowUseCase.getFavoriteTvShow().asLiveData()
}