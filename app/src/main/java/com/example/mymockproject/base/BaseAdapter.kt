package com.example.mymockproject.base

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class BaseAdapter <T> (private val mList: ArrayList<T>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object{
        const val  TYPE_ITEM_LINEAR = 1
        const val TYPE_LOADING = 2
        const val TYPE_ITEM_GRID = 3
    }

    var page = 1
    var fixPaged = -1
    var currentPage = 0
    var isLoading = false
    var isFull = false
    var morelListener: ((page: Int) -> Unit)? = null

    fun setLoadMore(rv: RecyclerView, isGrid: Boolean,  listener: (page: Int) -> Unit){
        val layoutManager =
            if(!isGrid)
                rv.layoutManager as LinearLayoutManager
            else
                rv.layoutManager as GridLayoutManager
        var totalItem : Int
        var firstVisible: Int
        var visibleItem: Int
        morelListener = listener

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItem = layoutManager.itemCount
                firstVisible = layoutManager.findFirstVisibleItemPosition()
                visibleItem = layoutManager.childCount
                Timber.e("check total item: first + visible: ${firstVisible + visibleItem} & total: ${totalItem}")
                if(totalItem == 0) return
                if(!isLoading && (firstVisible + visibleItem >= totalItem)){
                    if(currentPage != page){
                        morelListener?.invoke(page)
                        isLoading = true
                        currentPage = page
                    }
                }
            }
        })
    }

    fun loadDataSuccess(){
        isLoading = false
        isFull = false
        page += 1
    }

    fun loadDataFull(){
        isLoading = false
        isFull = true
    }

    fun resetLoadMore(){
        isLoading = false
        isFull = false
        page = 1
        currentPage = 0
    }

    fun addData(newList: ArrayList<T>){
        val reset = newList.isNotEmpty() && page == 1
        if(reset)
            mList.clear()

        val oldSize = mList.size
        mList.addAll(newList)

        if(newList.size <= 2)
            loadDataFull()
        else
            loadDataSuccess()

        if(reset)
            notifyDataSetChanged()
        else
            notifyItemRangeChanged(oldSize, mList.size + 1)
    }
}