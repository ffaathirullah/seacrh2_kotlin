package org.d3if1008.fundamentals222.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_following.*
import org.d3if1008.fundamentals222.Adapter.FollowingAdapter
import org.d3if1008.fundamentals222.R
import org.d3if1008.fundamentals222.ViewModel.FollowingViewModel

class FollowingFragment : Fragment() {
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var adapter: FollowingAdapter

    companion object {
        private val ARG_USERNAME = "username"
        fun newInstance(username:String): FollowingFragment {
            val fragment =
                FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter()
        showRecyclerList()

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        if(arguments != null){
            val username = arguments?.getString(ARG_USERNAME) //menerima data
            showLoading(true)
            followingViewModel.setUsers(username.toString())
        }
        followingViewModel.getUser().observe(viewLifecycleOwner, Observer{userItems->
            if(userItems != null){
                adapter.setData(userItems)
                showLoading(false)
            }
        })
        followingViewModel.navigateToDetails.observe(viewLifecycleOwner, Observer{
            it.getContentIfNotHandled()?.let{
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.adapter = adapter

        adapter.notifyDataSetChanged()
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFollowing.visibility = View.VISIBLE
        } else {
            progressBarFollowing.visibility = View.GONE
        }
    }

}