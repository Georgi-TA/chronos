package com.touchawesome.chronos

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.li_call_log.view.*

/**
 * Created by scelus on 1/22/18
 */
class CallLogAdapter() : RecyclerView.Adapter<CallLogAdapter.CallLogVH> () {

    val data: ArrayList<CallLogEntry> = ArrayList();

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CallLogVH {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.li_call_log, parent, false)
        return CallLogVH(itemView)
    }

    override fun onBindViewHolder(holder: CallLogVH?, position: Int) {
        val item = data.get(position)
        holder?.bind(item)
    }

    class CallLogVH (itemView: View) : RecyclerView.ViewHolder (itemView) {
        fun bind(item: CallLogEntry) {
            itemView.call_log_name.text = item.name
            itemView.call_log_number.text = item.number

            if (item.photo != null)
                Picasso.with(itemView.context)
                    .load(item.photo)
                    .resize(64, 64)
                    .centerCrop()
                    .into(itemView.call_log_image)

            itemView.call_log_date.text = DateUtils.getRelativeTimeSpanString(item.timestamp, System.currentTimeMillis() / 1000, DateUtils.DAY_IN_MILLIS)
        }
    }

    fun setData(data: ArrayList<CallLogEntry>) {
        this.data.clear()
        this.data.addAll(data)
    }
}