package com.chahinem.cityguide.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.chahinem.cityguide.ui.MainEvent.LoadMain
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val interactor: MainInteractor
) : ViewModel() {

  internal val data: MutableLiveData<MainModel> = MutableLiveData()
  internal val uiEvents: Subject<MainEvent> = PublishSubject.create()

  init {
    uiEvents
        .doOnNext { Timber.d("--> event: ${it.javaClass.simpleName} -- $it") }
        .publish { shared ->
          Observable.mergeDelayError(listOf(
              shared
                  .ofType(LoadMain::class.java)
                  .compose(interactor.calendar())
          ))
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(data::postValue, Timber::e)
  }
}