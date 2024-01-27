package com.example.mymockproject.view.home.m01_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymockproject.databinding.ItemSlideBinding
import com.example.mymockproject.model.data.HotBannerData

class HotBannerMovieAdapter(private val list: ArrayList<HotBannerData>):
    RecyclerView.Adapter<HotBannerMovieAdapter.HotBannerHolder>() {

    class HotBannerHolder(val binding: ItemSlideBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotBannerHolder {
        return HotBannerHolder(ItemSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: HotBannerHolder, position: Int) {
        holder.binding.itemSlide.setImageResource(list[position].image)
    }
}