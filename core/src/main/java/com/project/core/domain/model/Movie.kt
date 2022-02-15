package com.project.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Keep
data class Movie(
    var id: Int? = null,
    var title: String? = null,
    var overview: String? = null,
    var popularity: Float? = null,
    var isFavorite: Boolean? = null,
    var genres: @RawValue List<Genre>? = emptyList(),
    var posterPath: String? = null,
    var backdropPath: String? = null,
    var voteAverage: String? = null,
    var releaseDate: String? = null,
    var type: String?= null
): Parcelable