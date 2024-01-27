package com.example.mymockproject.view.home.detail_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mymockproject.R
import com.example.mymockproject.constant.Constants
import com.example.mymockproject.databinding.ActivityDetailMovieBinding
import com.example.mymockproject.model.response.CastAndCrewData
import com.example.mymockproject.model.response.CastItemData
import com.example.mymockproject.model.response.DetailMovieData
import com.example.mymockproject.utility.DialogUtil
import com.example.mymockproject.utility.Utility
import com.example.mymockproject.view.home.MovieViewModel
import com.example.mymockproject.view.home.detail_movie.adapter.CastAndCrewAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

@AndroidEntryPoint
class DetailMovieActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailMovieBinding

    private val detailVM: DetailMovieViewModel by viewModels()
    private val movieVM: MovieViewModel by viewModels()

    private var mMovieId: Int = 0
    private var isSelect = false
    private lateinit var mMovieData : DetailMovieData
    private var listCast: ArrayList<CastItemData> = arrayListOf()
    private lateinit var mAdapter : CastAndCrewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mMovieId = intent.getIntExtra(Constants.Intent.MOVIE_ID, 0)
        isSelect = intent.getBooleanExtra(Constants.Intent.IS_FAVOURITE, false)
        initView()
        initObserve()
        initEvent()
        getData()
    }

    private fun initView(){
        mAdapter = CastAndCrewAdapter(listCast, this)
        val linearLayout = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCast.adapter = mAdapter
        binding.rvCast.layoutManager = linearLayout

        if(isSelect){
            binding.imSelect.setImageResource(R.drawable.ic_select_favourite)
        } else
            binding.imSelect.setImageResource(R.drawable.ic_not_favourite_white)
    }

    private fun initObserve(){
        detailVM.detailState.observe(this){
            when(it.status){
                DetailMovieState.Status.LOADING -> {
                    DialogUtil.showLoadingDialog(this)
                }
                DetailMovieState.Status.SUCCESS_CAST_AND_CREW -> {
                    DialogUtil.hideLoading()
                    val data = it.data as CastAndCrewData
                    listCast.clear()
                    listCast.addAll(data.cast)
                    mAdapter.notifyDataSetChanged()
                }
                DetailMovieState.Status.SUCCESS_DETAIL_MOVIE -> {
                    DialogUtil.hideLoading()
                    val data = it.data as DetailMovieData
                    mMovieData = data
                    setUpDetail(data)
                }
                DetailMovieState.Status.FAILED -> {
                    DialogUtil.hideLoading()
                    Utility.toast(this, it.message?:"")
                }
            }
        }
    }

    private fun initEvent(){
        binding.icBack.setOnClickListener {
            finish()
        }

        binding.imSelect.setOnClickListener {
            isSelect = !isSelect
            if(isSelect){
                binding.imSelect.setImageResource(R.drawable.ic_select_favourite)
                movieVM.saveMovie(mMovieData)
                EventBus.getDefault().post(ChangeFavouriteFromDetail(mMovieData,
                    isAdd = true,
                    isDelete = false
                ))
                EventBus.getDefault().post(NotifyChangeToHome(mMovieData,
                    isAdd = true,
                    isDelete = false
                ))
            } else{
                binding.imSelect.setImageResource(R.drawable.ic_not_favourite_white)
                movieVM.deleteMovie(mMovieData)
                EventBus.getDefault().post(ChangeFavouriteFromDetail(mMovieData,
                    isAdd = false,
                    isDelete = true
                ))
                EventBus.getDefault().post(NotifyChangeToHome(mMovieData,
                    isAdd = false,
                    isDelete = true
                ))
            }

        }
    }

    private fun setUpDetail(movie: DetailMovieData){
        binding.tvDate.text = movie.release_date
        binding.tvRate.text = "${movie.vote_average}/10"
        binding.tvOverview.text = movie.overview
        Glide.with(this)
            .load(Constants.BASE_IMAGE + movie.poster_path)
            .error(R.drawable.g7)
            .into(binding.movieThumb)
    }

    private fun getData(){
        detailVM.getDetailMovie(mMovieId)
        detailVM.getCastAndCrew(mMovieId)
    }

    class ChangeFavouriteFromDetail(val movie: DetailMovieData, val isAdd: Boolean, val isDelete: Boolean)
    class NotifyChangeToHome(val movie: DetailMovieData, val isAdd: Boolean, val isDelete: Boolean)
}