package com.koshsu.githubsearch.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.koshsu.githubsearch.R
import com.koshsu.githubsearch.data.Status
import com.koshsu.githubsearch.ui.interfaces.IProgressBarFragment
import com.koshsu.githubsearch.ui.interfaces.IProgressBarActivity
import com.koshsu.githubsearch.ui.interfaces.ISearchListenerActivity
import com.koshsu.githubsearch.ui.interfaces.ISearchListenerFragment
import com.koshsu.githubsearch.utils.toastS
import kotlinx.android.synthetic.main.fragment_search.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SearchFragment : Fragment(), KodeinAware,
    IProgressBarFragment,
    ISearchListenerFragment {

    // DI
    override val kodein by kodein()
    private val factory: SearchViewModelFactory by instance()

    // Navigation
    private val args: SearchFragmentArgs by navArgs()

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchQuery: String
    private lateinit var searchAdapter: SearchAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchQuery = args.searchQuery

        (requireActivity() as ISearchListenerActivity).registerSearchFragment(this)
        (requireActivity() as ISearchListenerActivity).setSearchText(searchQuery)

        // if you want to avoid unnecessary re-fetching data put this initialization here
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)
        getData()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
        initReposObserver()
    }


    // Observe data & update UI
    private fun initReposObserver() {
        viewModel.repos.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                is Status.Loading -> showProgressBar()
                is Status.Success -> {
                    hideProgressBar()
                    if (status.data.isNotEmpty() && status.data != searchAdapter.repos) {
                        searchAdapter.apply {
                            this.repos = status.data
                        }
                    }
                }
                is Status.Error -> {
                    requireContext().toastS(status.errorMessage)
                    hideProgressBar()
                }
            }
        })
    }

    // Request data by search
    private fun getData() = viewModel.getRepos(searchQuery)

    // Show or hide activity's progressbar
    override fun showProgressBar() = (requireActivity() as IProgressBarActivity).show()
    override fun hideProgressBar() = (requireActivity() as IProgressBarActivity).hide()

    // casual ui binds
    private fun bindUI() {
        searchAdapter = SearchAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = searchAdapter
        }
    }

    // do search from activity > toolbar > SearchView
    override fun doSearch(searchQuery: String) {
        this.searchQuery = searchQuery
        getData()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as ISearchListenerActivity).showSearchView(true)
    }

}