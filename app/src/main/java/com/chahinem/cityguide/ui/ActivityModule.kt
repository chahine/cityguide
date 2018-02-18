package com.chahinem.cityguide.ui

import android.arch.lifecycle.ViewModel
import com.chahinem.cityguide.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class ActivityModule {
  @Binds
  @IntoMap
  @ViewModelKey(MainViewModel::class)
  abstract fun bindMainViewModel(vm: MainViewModel): ViewModel
}