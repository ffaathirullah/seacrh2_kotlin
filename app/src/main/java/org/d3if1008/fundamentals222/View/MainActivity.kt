package org.d3if1008.fundamentals222.View

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.d3if1008.fundamentals222.Adapter.ListUserAdapter
import org.d3if1008.fundamentals222.Model.UserItems
import org.d3if1008.fundamentals222.R
import org.d3if1008.fundamentals222.ViewModel.MainViewModel

class MainActivity : AppCompatActivity(){

    private var title = "GitHub User's" //kalo nyari title detail cari di recyclerview bedahkode
    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setActionBarTitle(title)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_github_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        mainViewModel.getUser().observe(this, Observer{userItems->
            if(userItems != null){
                adapter.setData(userItems)
            }
        })

        mainViewModel.navigateToDetails.observe(this, Observer{
            it.getContentIfNotHandled()?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItems) {
                showSelectedUSer(data)
                val moveDetailUser = Intent(this@MainActivity, DetailUser::class.java)
                moveDetailUser.putExtra(DetailUser.EXTRA_USER, data)
                startActivity(moveDetailUser)
            }
        })
    }

    private fun showSelectedUSer(data: UserItems) {
        Toast.makeText(this, data.username, Toast.LENGTH_SHORT).show()
    }

    private fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu : Menu) :Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        inflater.inflate(R.menu.menubahasa, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()){
                    return true
                }else{
                    progressBar.visibility=View.INVISIBLE
                    mainViewModel.setUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                if(newText.length!=0){
                if(newText.isNotEmpty()){
                    mainViewModel.setUsers(newText)
                    progressBar.visibility=View.VISIBLE
                }else{
                    progressBar.visibility=View.INVISIBLE
                    return false
                }
                return false
            }
        })
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)

        }
        return super.onOptionsItemSelected(item)
    }

}