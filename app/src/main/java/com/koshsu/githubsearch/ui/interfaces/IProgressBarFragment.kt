package com.koshsu.githubsearch.ui.interfaces

/** Instead of creating these functions in your nav fragments for manipulating progressbar
 *  it's better to implement and override */
interface IProgressBarFragment {
    fun showProgressBar()
    fun hideProgressBar()
}