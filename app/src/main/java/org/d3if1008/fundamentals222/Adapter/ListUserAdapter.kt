package org.d3if1008.fundamentals222.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_user.view.*
import org.d3if1008.fundamentals222.Model.UserItems
import org.d3if1008.fundamentals222.R

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val mData = ArrayList<UserItems>()


    interface OnItemClickCallback {
        fun onItemClicked(data: UserItems)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserItems>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(userItems: UserItems){
            with(itemView){
                tv_item_username.text = userItems.username
                tv_item_url.text = userItems.html_url

                Glide.with(itemView.context)
                    .load(userItems.avatar_url)
                    .apply(RequestOptions())
                    .into(img_avatar)

                itemView.setOnClickListener{
                    onItemClickCallback.onItemClicked(userItems)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(listViewHolder: ListViewHolder, position: Int) {
        listViewHolder.bind(mData[position])
    }


}