package com.example.mymockproject.view.home.detail_movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymockproject.R
import com.example.mymockproject.constant.Constants
import com.example.mymockproject.databinding.ItemCastBinding
import com.example.mymockproject.model.response.CastItemData

class CastAndCrewAdapter(
    private val list: ArrayList<CastItemData>,
    private val context: Context): RecyclerView.Adapter<CastAndCrewAdapter.CastHolder>() {

    class CastHolder(val binding: ItemCastBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastHolder {
        return CastHolder(ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CastHolder, position: Int) {
        val cast = list[position]
        holder.binding.itemName.text = cast.original_name
        Glide.with(context).load(Constants.BASE_IMAGE + cast.profile_path)
            .error(R.drawable.g7)
            .into(holder.binding.itemAvatar)
    }
}