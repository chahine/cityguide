package com.chahinem.cityguide.ui

sealed class MainModel {
  class MainProgress : MainModel()
  class MainSuccess(val items: List<MainAdapter.Item>) : MainModel()
  class MainFailure(val error: Throwable) : MainModel()
}