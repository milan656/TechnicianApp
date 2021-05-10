package com.walkins.aapkedoorstep.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.walkins.aapkedoorstep.R
import com.walkins.aapkedoorstep.common.onClickAdapter


class ItemRecyclerViewAdapter(
    context: Context, arrayList: ArrayList<String>,onPositionClick: onClickAdapter
) :
    RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder>() {
    var onclick: onClickAdapter? = null

    public inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivInfo: ImageView = itemView.findViewById(R.id.ivInfo)
    }

    private val context: Context
    private val arrayList: ArrayList<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.design_home_adapter_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        holder.itemLabel.text = arrayList[position]

        holder.itemView?.setOnClickListener {

            if (onclick != null) {
                onclick?.onPositionClick(position, 0)
            }

        }

        holder.ivInfo?.setOnClickListener {
            if (onclick != null) {
                onclick?.onPositionClick(position, 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    init {
        this.context = context
        this.arrayList = arrayList
    }
}