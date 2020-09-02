package co.id.moviemvvmrestapi.ui.single_movie_details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import co.id.moviemvvmrestapi.R
import co.id.moviemvvmrestapi.data.api.POSTER_BASE_URL
import co.id.moviemvvmrestapi.data.api.TheMovieDBClient
import co.id.moviemvvmrestapi.data.api.TheMovieDBInterface
import co.id.moviemvvmrestapi.data.repository.NetworkState
import co.id.moviemvvmrestapi.data.vo.MovieDetails
import co.id.moviemvvmrestapi.databinding.ActivitySingleMovieBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_movie.*

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    private lateinit var mBinding: ActivitySingleMovieBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)
        mBinding.viewModel = viewModel

        viewModel.movieDetails.observe(this, Observer {
            bindUi(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })
    }

    private fun getViewModel(movieId:Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }

    fun setImage(url: String){
        Glide.with(this)
            .load(url)
            .into(mBinding.ivMoviePoster);
    }

    fun bindUi(it: MovieDetails) {
        viewModel.setData(it)
        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        setImage(moviePosterURL)
    }
}