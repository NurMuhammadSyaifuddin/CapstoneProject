package com.project.favorite.tvshow

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
import com.project.core.domain.model.TvShow
import com.project.core.ui.TvShowAdapter
import com.project.core.utils.Type
import com.project.favorite.databinding.FragmentFavoriteTvShowBinding
import com.project.favorite.di.favoriteModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FavoriteTvShowFragment : Fragment() {

    private var _binding: FragmentFavoriteTvShowBinding? = null
    private val binding get() = _binding as FragmentFavoriteTvShowBinding

    private lateinit var tvshowAdapter: TvShowAdapter

    private val db by lazy { Firebase.firestore }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadKoinModules(favoriteModule)

        // init
        tvshowAdapter = TvShowAdapter(Type.ALL)

        onAction()
    }

    private fun onAction() {
        tvshowAdapter.onClick {
            Intent(activity, DetailActivity::class.java).also { intent ->
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                intent.putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE[1])
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getFavoriteTvShow()
    }

    private fun getFavoriteTvShow() {
        binding.apply {

            collectionTvShow()
                .get()
                .addOnSuccessListener { query ->
                    val data = query.documents.map { snapshot ->
                        snapshot.toObject(TvShow::class.java)
                    }

                    if (data.isNullOrEmpty()) {
                        rvFavoriteTvshow.gone()
                        showEmptyFavorite(
                            imgEmptyState,
                            titleEmptyState,
                            descEmptyState
                        )

                    } else {
                        rvFavoriteTvshow.visible()
                        hideEmptyFavorite(
                            imgEmptyState,
                            titleEmptyState,
                            descEmptyState
                        )
                        tvshowAdapter.tvshow = data as MutableList<TvShow>
                    }
                }


            rvFavoriteTvshow.setHasFixedSize(true)
            rvFavoriteTvshow.adapter = tvshowAdapter
        }
    }

    private fun collectionTvShow() =
        db.collection(PATH_TVSHOW).document(PATH_FAVORITE)
            .collection(user?.uid.toString())

    override fun onDestroyView() {
        super.onDestroyView()
        unloadKoinModules(favoriteModule)
        binding.rvFavoriteTvshow.adapter = null
        _binding = null
    }
}