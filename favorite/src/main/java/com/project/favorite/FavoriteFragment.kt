package com.project.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.project.favorite.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding as FragmentFavoriteBinding

    private var mediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(
            LayoutInflater.from(container?.context),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        binding.apply {
            viewPager.isSaveEnabled = false
            viewPager.adapter = SectionPagerAdapter(activity as AppCompatActivity)
            mediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getStringArray(R.array.tab_title)[position]
            }

            mediator?.attach()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mediator?.detach()
        mediator = null
        binding.viewPager.adapter = null
        _binding = null
    }

}