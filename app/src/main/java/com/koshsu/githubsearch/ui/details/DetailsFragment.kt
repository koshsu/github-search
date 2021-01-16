package com.koshsu.githubsearch.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.koshsu.githubsearch.R
import com.koshsu.githubsearch.data.Status
import com.koshsu.githubsearch.databinding.FragmentDetailsBinding
import com.koshsu.githubsearch.ui.interfaces.IProgressBarFragment
import com.koshsu.githubsearch.ui.interfaces.IProgressBarActivity
import com.koshsu.githubsearch.ui.interfaces.ISearchListenerActivity
import com.koshsu.githubsearch.utils.toastS
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DetailsFragment : Fragment(), KodeinAware,
    IProgressBarFragment {

    // DI
    override val kodein by kodein()
    private val factory: DetailsViewModelFactory by instance()

    // Navigation
    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: FragmentDetailsBinding
    private var repoId: Long = 0L
    private var ownerLogin: String = ""
    private var repoName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoId = args.repoId
        ownerLogin = args.ownerLogin
        repoName = args.repoName
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(DetailsViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@DetailsFragment
            vm = viewModel
        }

        viewModel.getRepo(repoId, ownerLogin, repoName)

        initObserver()

    }

    // Observe data & update UI
    private fun initObserver() {
        viewModel.repo.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                is Status.Loading -> showProgressBar()
                is Status.Success -> {
                    binding.repo = status.data
                    viewModel.isWatchersVisible.value = status.data.watchers != null
                    hideProgressBar()
                }
                is Status.Error -> {
                    requireContext().toastS(status.errorMessage)
                    hideProgressBar()
                }
            }
        })
    }

    // Show or hide activity's progressbar
    override fun showProgressBar() = (requireActivity() as IProgressBarActivity).show()
    override fun hideProgressBar() = (requireActivity() as IProgressBarActivity).hide()

    override fun onResume() {
        super.onResume()
        (requireActivity() as ISearchListenerActivity).showSearchView(false)
    }

}