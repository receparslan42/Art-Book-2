package com.receparslan.artbook.view

import com.bumptech.glide.RequestManager
import com.receparslan.artbook.adapter.MainRecyclerAdapter
import com.receparslan.artbook.adapter.SearchRecyclerAdapter
import javax.inject.Inject

class ArtFragmentFactory @Inject constructor(
    private val mainRecyclerAdapter: MainRecyclerAdapter,
    private val searchRecyclerAdapter: SearchRecyclerAdapter,
    private val glide: RequestManager
) : androidx.fragment.app.FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): androidx.fragment.app.Fragment {
        return when (className) {
            MainFragment::class.java.name -> MainFragment(mainRecyclerAdapter)
            DetailFragment::class.java.name -> DetailFragment(glide)
            SearchFragment::class.java.name -> SearchFragment(searchRecyclerAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}