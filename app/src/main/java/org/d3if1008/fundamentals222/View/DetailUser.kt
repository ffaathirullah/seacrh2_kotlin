package org.d3if1008.fundamentals222.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_user.*
import org.d3if1008.fundamentals222.Adapter.SectionPagerAdapter
import org.d3if1008.fundamentals222.Model.UserItems
import org.d3if1008.fundamentals222.R
import org.json.JSONObject
import java.lang.Exception

class DetailUser : AppCompatActivity() {

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_detail_user)

        val user = intent.getParcelableExtra(EXTRA_USER) as UserItems?
        setActionBarTitle(user?.username)

        Glide.with(this)
            .load(user?.avatar_url)
            .apply(RequestOptions())
            .into(img_detail_avatar)

        getDetail(user?.username)

        val sectionPagerAdapter =
            SectionPagerAdapter(
                this,
                supportFragmentManager
            )
        view_pager.adapter = sectionPagerAdapter
        sectionPagerAdapter.username = user?.username.toString() //setter data
        tabs.setupWithViewPager(view_pager)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getDetail(username :String?) {
        // progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url ="https://api.github.com/users/$username"
        client.addHeader("Authorization",  0.toString())
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val user = UserItems()

                    tv_detail_name.text = responseObject.getString("name")
                    tv_detail_username.text = responseObject.getString("login")
                    user.avatar_url = responseObject.getString("avatar_url")
                    tv_detail_followers.text = responseObject.getInt("followers").toString()
                    tv_detail_following.text = responseObject.getInt("following").toString()
                    tv_detail_repository.text = responseObject.getInt("public_repos").toString()

                }catch (e : Exception){
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailUser, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setActionBarTitle(username: String?) {
        supportActionBar?.title = username
    }

    override fun onCreateOptionsMenu(menu : Menu) :Boolean {
        menuInflater.inflate(R.menu.menubahasa, menu)
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