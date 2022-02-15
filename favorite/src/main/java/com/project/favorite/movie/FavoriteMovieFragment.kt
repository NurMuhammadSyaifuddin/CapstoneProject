package com.project.favorite.movie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.capstoneproject.ui.detail.DetailActivity
import com.project.capstoneproject.utils.*
import com.project.core.domain.model.Movie
import com.project.core.ui.MovieAdapter
import com.project.core.utils.Type
import com.project.favorite.databinding.FragmentFavoriteMovieBinding
import com.project.favorite.di.favoriteModule
import org.koin.core.context.loadKoinModules

class FavoriteMovieFragment : Fragment() {

    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding as FragmentFavoriteMovieBinding

    private lateinit var movieAdapter: MovieAdapter

    private val db by lazy { Firebase.firestore }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(favoriteModule)

        // init
        movieAdapter = MovieAdapter(Type.ALL)

        onAction()
    }

    private fun onAction() {
        movieAdapter.onClick {
            Intent(activity, DetailActivity::class.java).also { intent ->
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                intent.putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE[0])
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {
        binding.apply {

            collectionMovie()
                .get()
                .addOnSuccessListener { query ->
                    val data = query.documents.map { snapshot ->
                        snapshot.toObject(Movie::class.java)
                    }

                    if (data.isNullOrEmpty()) {
                        rvFavoriteMovie.gone()
                        showEmptyFavorite(
                            imgEmptyState,
                            titleEmptyState,
                            descEmptyState
                        )
                    } else {
                        rvFavoriteMovie.visible()
                        hideEmptyFavorite(
                            imgEmptyState,
                            titleEmptyState,
                            descEmptyState
                        )
                        movieAdapter.listMovies = data as MutableList<Movie>

                    }
                }

            binding.rvFavoriteMovie.setHasFixedSize(true)
            binding.rvFavoriteMovie.adapter = movieAdapter

        }
    }

    private fun collectionMovie() =
        db.collection(PATH_MOVIE).document(PATH_FAVORITE)
            .collection(user?.uid.toString())

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvFavoriteMovie.adapter = null
        _binding = null
    }

}