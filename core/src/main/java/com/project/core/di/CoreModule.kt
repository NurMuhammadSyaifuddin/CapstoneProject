package com.project.core.di

import androidx.room.Room
import com.project.core.BuildConfig
import com.project.core.data.source.local.MovieLocalDataSource
import com.project.core.data.source.local.MovieDatabase
import com.project.core.data.source.local.TvShowLocalDataSource
import com.project.core.data.source.remote.MovieRemoteDataSource
import com.project.core.data.source.remote.TvShowRemoteDataSource
import com.project.core.data.source.remote.network.ApiService
import com.project.core.data.source.repository.MovieRepository
import com.project.core.data.source.repository.TvShowRepository
import com.project.core.domain.repository.IMovieRepository
import com.project.core.domain.repository.ITvShowRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<MovieDatabase>().movieDao() }
    factory { get<MovieDatabase>().tvShowDao() }
    single {
        val passPhrase: ByteArray = SQLiteDatabase.getBytes(BuildConfig.DATABASE_KEY.toCharArray())
        val factory = SupportFactory(passPhrase)
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java, "movie.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = BuildConfig.HOSTNAME
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, BuildConfig.CERTIFICATE_PINNING_1)
            .add(hostname, BuildConfig.CERTIFICATE_PINNING_2)
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }
}

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { MovieLocalDataSource(get()) }
    single { MovieRemoteDataSource(get()) }
    single { TvShowLocalDataSource(get()) }
    single { TvShowRemoteDataSource(get()) }
    single<IMovieRepository> {
        MovieRepository(
            get(),
            get()
        )
    }
    single<ITvShowRepository> {
        TvShowRepository(
            get(),
            get()
        )
    }
}