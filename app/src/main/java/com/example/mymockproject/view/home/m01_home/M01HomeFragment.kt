package com.example.mymockproject.view.home.m01_home

import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymockproject.R
import com.example.mymockproject.base.BaseFragment
import com.example.mymockproject.base.hide
import com.example.mymockproject.base.show
import com.example.mymockproject.constant.Constants
import com.example.mymockproject.database.dao.FavouriteDao
import com.example.mymockproject.databinding.FragmentM01HomeBinding
import com.example.mymockproject.model.response.DetailMovieData
import com.example.mymockproject.model.response.MovieData
import com.example.mymockproject.utility.DialogUtil
import com.example.mymockproject.utility.Utility
import com.example.mymockproject.view.home.MovieState
import com.example.mymockproject.view.home.MovieViewModel
import com.example.mymockproject.view.home.detail_movie.DetailMovieActivity
import com.example.mymockproject.view.home.m01_home.adapter.HotBannerMovieAdapter
import com.example.mymockproject.view.home.m01_home.adapter.MovieAdapter
import com.example.mymockproject.view.home.m02_favourite.M02FavouriteFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class M01HomeFragment : BaseFragment<FragmentM01HomeBinding>(FragmentM01HomeBinding::inflate) {

    private lateinit var mMovieAdapter: MovieAdapter
    private lateinit var mHotBannerAdapter: HotBannerMovieAdapter
    private var listMovie : ArrayList<DetailMovieData> = arrayListOf()
    private var listSearch: ArrayList<DetailMovieData> = arrayListOf()
    private var listFavourite: ArrayList<DetailMovieData> = arrayListOf()

    private val movieVM: MovieViewModel by viewModels()

    private lateinit var linearLayout: LinearLayoutManager
    private lateinit var gridLayout: GridLayoutManager

    private var isLinear = true
    private var jobGetMovieDb: Job? = null
    private var isDelete = false
    private var mTimer: Timer? = null

    @Inject
    lateinit var movieDao: FavouriteDao

    companion object{
        fun newInstance() : Fragment = M01HomeFragment()
    }

    override fun initView() {
        mMovieAdapter = MovieAdapter(listMovie, requireContext(), isCallApi = true)
        linearLayout = LinearLayoutManager(requireContext())
        gridLayout = GridLayoutManager(requireContext(), 2)
        binding.rv.adapter = mMovieAdapter
        binding.rv.layoutManager = linearLayout
        mMovieAdapter.setLoadMore(binding.rv, false){
            loadMore()
        }
        mMovieAdapter.listener = object : MovieAdapter.MovieListener{
            override fun onClick(movie: DetailMovieData) {
                Utility.goToDetail(requireContext(), movie.id, movie.getSelected())
            }

            override fun addFavourite(movie: DetailMovieData) {
                Utility.toast(requireContext(), getString(R.string.add_favourite))
                movieVM.saveMovie(movie)
            }

            override fun deleteFavourite(movie: DetailMovieData) {
                Utility.toast(requireContext(), getString(R.string.delete_favourite))
                movieVM.deleteMovie(movie)
            }
        }
        getListFavourite()
        initEvent()
        setBanner()
    }

    override fun getData() {}

    override fun initObserve() {
        movieVM.stateMovie.observe(viewLifecycleOwner){
            when(it.status){
                MovieState.Status.LOADING ->{}
                MovieState.Status.SUCCESS -> {
                    val data = it.data as MovieData
                    listMovie.addAll(data.results)
                    mMovieAdapter.addData(data.results)
                    setFavourite()
                    DialogUtil.hideLoading()
                }
                MovieState.Status.FAILED -> {
                    DialogUtil.hideLoading()
                    Utility.toast(requireContext(), "${it.message}")
                }

                MovieState.Status.LOADING_SEARCH -> {
                    DialogUtil.showLoadingDialog(requireContext())
                }
                MovieState.Status.SUCCESS_SEARCH -> {
                    DialogUtil.hideLoading()
                    val data = it.data as MovieData
                    listSearch.clear()
                    listSearch.addAll(data.results)
                    mMovieAdapter.resetLoadMore()
                    mMovieAdapter.addData(data.results)
                    setFavourite()
                }
            }
        }
    }

    private fun initEvent(){
        // change view
        binding.changeView.setOnClickListener {
            isLinear = !isLinear
            if(isLinear){
                mMovieAdapter.isGrid = false
                binding.viewPageHotMovie.show()
                binding.indicator.hide()
                binding.rv.layoutManager = linearLayout
                binding.changeView.setImageResource(R.drawable.ic_grid)
                mMovieAdapter.setLoadMore(binding.rv, false){
                    loadMore()
                }
            } else{
                mMovieAdapter.isGrid = true
                binding.viewPageHotMovie.hide()
                binding.indicator.hide()
                binding.rv.layoutManager = gridLayout
                binding.changeView.setImageResource(R.drawable.ic_grid_off)
                mMovieAdapter.setLoadMore(binding.rv, true){
                    loadMore()
                }
            }
        }
        // search
        binding.edtSearch.setOnEditorActionListener { _, idEvent, _ ->
            if(idEvent == EditorInfo.IME_ACTION_SEARCH){
                val keyword = binding.edtSearch.text.toString()
                if(keyword.isNotEmpty()){
                    movieVM.search(keyword)
                    true
                } else false
            } else false
        }
    }

    private fun loadMore(){
        if(mMovieAdapter.fixPaged != mMovieAdapter.page && !isDelete){
            mMovieAdapter.fixPaged = mMovieAdapter.page
            movieVM.getMoviePopular(mMovieAdapter.page)
        }
    }

    private fun getListFavourite(){
        DialogUtil.showLoadingDialog(requireContext())
        jobGetMovieDb = CoroutineScope(Dispatchers.IO).launch {
            val list = movieDao.getAllFavourite()
            withContext(Dispatchers.Main){
                listFavourite.clear()
                listFavourite.addAll(list)
            }
        }
    }

    private fun setFavourite() {
        if (listFavourite.isEmpty()) return
        listMovie.forEachIndexed { index, _ ->
            listFavourite.forEach {
                if (it.id == listMovie[index].id) {
                    listMovie[index].setSelected(true)
                }
            }
            mMovieAdapter.notifyDataSetChanged()
        }
    }

    private fun setBanner(){
        mHotBannerAdapter = HotBannerMovieAdapter(Constants.HotBanner.initListBanner())
        binding.viewPageHotMovie.adapter = mHotBannerAdapter
        binding.indicator.attachTo(binding.viewPageHotMovie)
        autoSlide()
    }

    private fun autoSlide(){
        if(mTimer == null)
            mTimer = Timer()
        mTimer!!.schedule(object : TimerTask(){
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    var postCurrent = binding.viewPageHotMovie.currentItem
                    val total = Constants.HotBanner.initListBanner().size - 1
                    if(postCurrent < total){
                        postCurrent++
                        binding.viewPageHotMovie.currentItem = postCurrent
                    } else
                        binding.viewPageHotMovie.currentItem = 0
                }
            }

        }, 500, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        jobGetMovieDb?.cancel()
        EventBus.getDefault().unregister(EventBusListener())
        mTimer?.cancel()
        mTimer = null
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(EventBusListener())
    }

    private fun ChangwStateFavourite(movieData: DetailMovieData, isDelete: Boolean){
        var index = 0
        for(pos in listMovie.indices){
            if(listMovie[pos].id == movieData.id){
                if(isDelete){
                    listMovie[pos].setSelected(false) // delete movie
                } else
                    listMovie[pos].setSelected(true) // add movie
                index = pos
                break
            }
        }
        mMovieAdapter.notifyItemChanged(index)
    }


    inner class EventBusListener{
        @Subscribe
        fun onEvent(onEvent: M02FavouriteFragment.DeleteFavourite){
            isDelete = onEvent.isDelete
            ChangwStateFavourite(onEvent.movieDelete, isDelete)
        }

        @Subscribe
        fun onEventFromDetail(onEvent: DetailMovieActivity.NotifyChangeToHome){
            if(onEvent.isDelete){
                ChangwStateFavourite(onEvent.movie, true)
            } else if(!onEvent.isDelete && onEvent.isAdd){
                ChangwStateFavourite(onEvent.movie, false)
            }
        }
    }
}