package co.id.moviemvvmrestapi.ui.single_movie_details

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.id.moviemvvmrestapi.data.repository.NetworkState
import co.id.moviemvvmrestapi.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import java.text.NumberFormat
import java.util.*

class SingleMovieViewModel(private val movieRepository: MovieDetailsRepository, movieId: Int)  : ViewModel() {

    private val TAG: String = "SingleMovieViewModel"
    private val compositeDisposable = CompositeDisposable()

    val title = ObservableField<String>()
    val tagline = ObservableField<String>()
    val releaseDate = ObservableField<String>()
    val rating = ObservableField<String>()
    val runtime = ObservableField<String>()
    val overview = ObservableField<String>()
    val budget = ObservableField<String>()
    val reveneu = ObservableField<String>()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun setData(it: MovieDetails){
        title.set(it.title)
        tagline.set(it.tagline)
        rating.set(it.rating.toString())
        runtime.set(it.runtime.toString() + "minutes")
        overview.set(it.overview)
        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        budget.set(formatCurrency.format(it.budget))
        reveneu.set(formatCurrency.format(it.revenue))
    }
}