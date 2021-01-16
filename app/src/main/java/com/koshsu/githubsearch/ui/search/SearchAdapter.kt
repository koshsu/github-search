package com.koshsu.githubsearch.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.koshsu.githubsearch.R
import com.koshsu.githubsearch.data.database.model.Repo
import com.koshsu.githubsearch.databinding.ItemSearchRepositoryBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var repos: List<Repo>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = repos?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_search_repository,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.repo = repos!![position]
        holder.binding.searchItem.setOnClickListener {
            val action =
                SearchFragmentDirections.actionDetails(
                    repos!![position].id,
                    repos!![position].owner.login,
                    repos!![position].name
                )
            Navigation.findNavController(it).navigate(action)
        }

    }

    class SearchViewHolder(val binding: ItemSearchRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}