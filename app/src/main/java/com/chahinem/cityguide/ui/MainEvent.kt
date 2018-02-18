package com.chahinem.cityguide.ui

sealed class MainEvent {
  class LoadMain(val skipCache: Boolean = false) : MainEvent()
}
