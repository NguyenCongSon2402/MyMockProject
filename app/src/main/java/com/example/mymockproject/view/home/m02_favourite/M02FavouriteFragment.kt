package com.example.mymockproject.view.home.m02_favourite

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymockproject.R
import com.example.mymockproject.base.BaseFragment
import com.example.mymockproject.database.dao.FavouriteDao
import com.example.mymockproject.databinding.FragmentM02FavouriteBinding
import com.example.mymockproject.model.response.DetailMovieData
import com.example.mymockproject.utility.Utility
import com.example.mymockproject.view.home.MovieViewModel
import com.example.mymockproject.view.home.detail_movie.DetailMovieActivity
import com.example.mymockproject.view.home.m01_home.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class M02FavouriteFragment : BaseFragment<FragmentM02FavouriteBinding>(FragmentM02FavouriteBinding::inflate) {

    private lateinit var mAdapter: MovieAdapter
    private var mListFavourite: ArrayList<DetailMovieData> = arrayListOf()

    private val movieVM: MovieViewModel by viewModels()
    private var isAddNew = false
    private var isDeleteNew = false
    private var mMovieInDetail : DetailMovieData? = null

    @Inject
    lateinit var movieDao: FavouriteDao
    private var favouriteJob : Job? = null

    companion object{
        fun newInstance() = M02FavouriteFragment()
    }

    override fun initView() {
        mAdapter = MovieAdapter(mListFavourite, requireContext(), isCallApi = false)
        val linearLayout = LinearLayoutManager(requireContext())
        binding.rv.adapter = mAdapter
        binding.rv.layoutManager = linearLayout
        mAdapter.listener = object : MovieAdapter.MovieListener{
            override fun onClick(movie: DetailMovieData) {
                Utility.toast(requireContext(), movie.title)
            }

            override fun addFavourite(movie: DetailMovieData) {}

            override fun deleteFavourite(movie: DetailMovieData) {
                Utility.toast(requireContext(), getString(R.string.delete_favourite))
                movieVM.deleteMovie(movie)
                EventBus.getDefault().post(DeleteFavourite(true, movie))
            }

        }
    }

    override fun getData() {
        favouriteJob = CoroutineScope(Dispatchers.IO).launch{
            val listInDb = movieDao.getAllFavourite()
            withContext(Dispatchers.Main){
                mListFavourite.clear()
                mListFavourite.addAll(listInDb)
                setDefaultFavourite()
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun initObserve() {
        movieVM.stateDbMovie.observe(requireActivity()){
            mListFavourite.clear()
            mListFavourite.addAll(it)
            setDefaultFavourite()
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun setDefaultFavourite(){
        mListFavourite.forEachIndexed { index, _ ->
            mListFavourite[index].setSelected(true)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(EventBusListener())
    }

    override fun onResume() {
        super.onResume()
        if(isAddNew){
            movieVM.saveMovie(mMovieInDetail!!)
        } else if(isDeleteNew){
            movieVM.deleteMovie(mMovieInDetail!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(EventBusListener())
    }

    class DeleteFavourite(val isDelete: Boolean, val movieDelete: DetailMovieData)

    inner class EventBusListener{
       @Subscribe
       fun onEventChangeFavourite(event: DetailMovieActivity.ChangeFavouriteFromDetail){
           isDeleteNew = event.isDelete
           isAddNew = event.isAdd
           mMovieInDetail = event.movie
       }
    }
}