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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.fragment_followers.*
import org.d3if1008.fundamentals222.Adapter.FollowersAdapter
import org.d3if1008.fundamentals222.R
import org.d3if1008.fundamentals222.ViewModel.FollowersViewModel

class FollowersFragment : Fragment() {

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: FollowersAdapter

    companion object {
        private const val ARG_USERNAME = "username" //Jika variable ini digunakan untuk nilai yang pasti (konstanta), silahkan tambahkan cons.
        fun newInstance(username:String): FollowersFragment {
            val fragment =
                FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowersAdapter()
        showRecyclerList()

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowersViewModel::class.java)
        if(arguments != null){
            val username = arguments?.getString(ARG_USERNAME) //menerima data
            showLoading(true)
            followersViewModel.setUsers(username.toString())
        }
        followersViewModel.getUser().observe(viewLifecycleOwner, Observer{userItems->
            if(userItems != null){
                adapter.setData(userItems)
                showLoading(false)
            }
        })
        followersViewModel.navigateToDetails.observe(viewLifecycleOwner, Observer{
            it.getContentIfNotHandled()?.let{
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        rv_followers.layoutManager = LinearLayoutManager(activity)
        rv_followers.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}