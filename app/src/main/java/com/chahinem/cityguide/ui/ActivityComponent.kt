package com.chahinem.cityguide.ui

import android.app.Activity
import android.app.Application
import com.chahinem.cityguide.api.ApiModule
import com.chahinem.cityguide.api.DistanceMatrixApi
import com.chahinem.cityguide.di.ViewModelModule
import com.chahinem.cityguide.repositories.LastLocationRepo
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, ViewModelModule::class])
interface ActivityComponent {

  fun inject(activity: MainActivity)

  fun distanceMatrixApi(): DistanceMatrixApi
  fun lastLocationRepo(): LastLocationRepo
  fun viewModel(): MainViewModel

  @Component.Builder interface Builder {
    fun apiModule(module: ApiModule): Builder
    @BindsInstance fun activity(activity: Activity): Builder
    @BindsInstance fun app(app: Application): Builder
    fun build(): ActivityComponent
  }
}
