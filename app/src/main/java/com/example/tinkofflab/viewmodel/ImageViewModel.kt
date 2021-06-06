package com.example.tinkofflab.viewmodel

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tinkofflab.ContentModel
import com.example.tinkofflab.R
import com.example.tinkofflab.utils.RetrofitSingleton
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ImageViewModel:ViewModel() {

    companion object{
        const val ERROR_NETWORK_KEY = 0
        const val GONE_DOWNLOAD_BAR_KEY = 1
        const val SHOW_VIEW_KEY = 2
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val liveData = MutableLiveData<Int>()
    fun getData():LiveData<Int> = liveData

    fun downloadImage(context:Context,positionTabLayout: Int,imageView: ImageView,
                      currentPosition:Int,contentList:MutableList<ContentModel>) {
        val flowable = when (positionTabLayout) {
            0 -> {
                RetrofitSingleton.getAppApi().getLatestGif(currentPosition)}
            1 ->{
                RetrofitSingleton.getAppApi().getHotGif(currentPosition)
            }
            else -> {
                RetrofitSingleton.getAppApi().getTopGif(currentPosition)}
        }
        compositeDisposable.add(flowable
            .flatMapIterable {
                contentList.addAll(it.result)
                it.result
            }
            .take(1)
            .map {
                Glide.with(context).asDrawable().load(it.url).diskCacheStrategy(
                    DiskCacheStrategy.ALL).submit().get()
                it.url
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ url ->
                Glide.with(context).asGif().load(url)
                    .into(imageView)
            },
                {
                    liveData.value = ERROR_NETWORK_KEY
                },
                {
                    liveData.value = SHOW_VIEW_KEY
                })
        )
    }

    fun downloadNextGif(contentModel: ContentModel, context:Context, imageView:ImageView){
        compositeDisposable.add(Observable.just(contentModel)
            .map { contentModel ->
                Glide.with(context).asDrawable().load(contentModel.url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).submit().get()
                contentModel.url
            }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ url ->
                Glide.with(context).asGif().load(url)
                    .into(imageView)
            }, {error->
                liveData.value = ERROR_NETWORK_KEY
            },
                {
                    liveData.value = GONE_DOWNLOAD_BAR_KEY
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}