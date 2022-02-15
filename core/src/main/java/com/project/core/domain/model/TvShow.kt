package com.project.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@Keep
data class TvShow(
    val id: Int? = null,
    val name: String? = null,
    val overview: String? = null,
    val isFavorite: Boolean? = null,
    val type: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val voteAverage: String? = null,
    val firstAirDate: String? = null,
    val genres: @RawValue List<Genre>? = emptyList()
): Parcelable
