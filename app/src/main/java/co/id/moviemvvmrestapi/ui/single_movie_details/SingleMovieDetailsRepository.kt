package co.id.moviemvvmrestapi.ui.single_movie_details

import androidx.lifecycle.LiveData
import co.id.moviemvvmrestapi.data.api.TheMovieDBInterface
import co.id.moviemvvmrestapi.data.repository.MovieDetailsNetworkDataSource
import co.id.moviemvvmrestapi.data.repository.NetworkState
import co.id.moviemvvmrestapi.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}