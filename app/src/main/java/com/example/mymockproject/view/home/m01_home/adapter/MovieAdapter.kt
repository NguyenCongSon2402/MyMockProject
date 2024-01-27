package com.example.mymockproject.view.home.m01_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymockproject.R
import com.example.mymockproject.base.BaseAdapter
import com.example.mymockproject.base.hide
import com.example.mymockproject.base.show
import com.example.mymockproject.constant.Constants
import com.example.mymockproject.databinding.ItemLoadingBinding
import com.example.mymockproject.databinding.ItemMovieBinding
import com.example.mymockproject.databinding.ItemMovieGridBinding
import com.example.mymockproject.model.response.DetailMovieData

class MovieAdapter(
    private val listMovie: ArrayList<DetailMovieData>,
    private val context: Context,
    private val isCallApi: Boolean
    ): BaseAdapter<DetailMovieData>(listMovie){

    var listener: MovieListener? = null
    var isGrid = false

    class MovieHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    class LoadingHolder(val binding: ItemLoadingBinding): RecyclerView.ViewHolder(binding.root)

    class MovieGridHolder(val binding : ItemMovieGridBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_ITEM_LINEAR -> {
                MovieHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            TYPE_ITEM_GRID -> {
                MovieGridHolder(ItemMovieGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                LoadingHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MovieHolder -> {
                val movie = listMovie[position]
                holder.binding.itemName.text = movie.title
                holder.binding.itemReleaseDate.text = movie.release_date
                holder.binding.itemRating.text = "${movie.vote_average}/10"
                holder.binding.itemVoteCount.text = movie.vote_count.toString()
                Glide.with(context).load(Constants.BASE_IMAGE + movie.poster_path)
                    .into(holder.binding.itemThumb)
                if(movie.getSelected()){
                    holder.binding.itemFavourite.setImageResource(R.drawable.ic_select_favourite)
                }
                else
                    holder.binding.itemFavourite.setImageResource(R.drawable.ic_not_favourite_white)

                holder.binding.itemFavourite.setOnClickListener {
                    movie.setSelected(!movie.getSelected())
                    if(movie.getSelected()){
                        holder.binding.itemFavourite.setImageResource(R.drawable.ic_select_favourite)
                        listener?.addFavourite(movie)
                    }
                    else{
                        holder.binding.itemFavourite.setImageResource(R.drawable.ic_not_favourite_white)
                        listener?.deleteFavourite(movie)
                    }
                }

                holder.itemView.setOnClickListener {
                    listener?.onClick(movie)
                }
            }

            is LoadingHolder -> {
                if(!isCallApi){
                    holder.binding.itemLoading.hide()
                } else{
                    if(isFull)
                        holder.binding.itemLoading.hide()
                    else
                        holder.binding.itemLoading.show()
                }
            }

            is MovieGridHolder -> {
                val movie = listMovie[position]
                holder.binding.itemName.text = movie.title
                Glide.with(context).load(Constants.BASE_IMAGE + movie.poster_path)
                    .into(holder.binding.itemThumb)
                holder.itemView.setOnClickListener {
                    listener?.onClick(movie)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listMovie.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if(listMovie.getOrNull(position) == null)
            TYPE_LOADING
        else{
            if(isGrid)
                TYPE_ITEM_GRID
            else
                TYPE_ITEM_LINEAR
        }
    }

    interface MovieListener{
        fun onClick(movie: DetailMovieData)
        fun addFavourite(movie: DetailMovieData)
        fun deleteFavourite(movie: DetailMovieData)
    }
}