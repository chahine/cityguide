package com.chahinem.cityguide.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.chahinem.cityguide.R
import com.chahinem.cityguide.ui.MainEvent.LoadMain
import com.chahinem.cityguide.ui.MainModel.MainFailure
import com.chahinem.cityguide.ui.MainModel.MainProgress
import com.chahinem.cityguide.ui.MainModel.MainSuccess
import com.chahinem.cityguide.ui.custom.CustomSlider.PositionChangeListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.activity_main.bar
import kotlinx.android.synthetic.main.activity_main.bistro
import kotlinx.android.synthetic.main.activity_main.cafe
import kotlinx.android.synthetic.main.activity_main.list
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.refreshLayout
import kotlinx.android.synthetic.main.activity_main.slider
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), PositionChangeListener {

  @Inject lateinit var viewModel: MainViewModel

  private lateinit var fusedLocationClient: FusedLocationProviderClient
  private lateinit var geoDataClient: GeoDataClient

  private val mainAdapter = MainAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    injectSelf()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    geoDataClient = Places.getGeoDataClient(this, null)

    bar.isSelected = true
    slider.positionChangeListener = this
    list.layoutManager = LinearLayoutManager(this)
    list.adapter = mainAdapter

    refreshLayout.setOnRefreshListener { viewModel.uiEvents.onNext(LoadMain(true)) }
    viewModel.data.observe(this, Observer {
      if (it != null) {
        onModelEvent(it)
      }
    })

    ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION).let {
      if (it != PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION), RC_LOCATION)
      } else {
        viewModel.uiEvents.onNext(LoadMain())
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String>,
                                          grantResults: IntArray) {
    when (requestCode) {
      RC_LOCATION -> {
        if ((grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)) {
          viewModel.uiEvents.onNext(LoadMain())
        } else {
          Snackbar
              .make(list, getString(R.string.error_location_permission), Toast.LENGTH_LONG)
              .show()
        }
        return
      }
    }
  }

  override fun onPositionChanged(position: Int) {
    bar.isSelected = false
    bistro.isSelected = false
    cafe.isSelected = false
    when (position) {
      0 -> {
        bar.isSelected = true
        mainAdapter.type = PlaceTypeEnum.BAR
        mainAdapter.notifyDataSetChanged()
      }
      1 -> {
        bistro.isSelected = true
        mainAdapter.type = PlaceTypeEnum.BISTRO
        mainAdapter.notifyDataSetChanged()
      }
      2 -> {
        cafe.isSelected = true
        mainAdapter.type = PlaceTypeEnum.CAFE
        mainAdapter.notifyDataSetChanged()
      }
    }
  }

  private fun injectSelf() {
    DaggerActivityComponent.builder()
        .activity(this)
        .app(application)
        .build()
        .inject(this)
  }

  private fun onModelEvent(model: MainModel) {
    Timber.d("--> model: ${model.javaClass.simpleName}")
    when (model) {
      is MainProgress -> onMainProgress(model)
      is MainSuccess -> onMainSuccess(model)
      is MainFailure -> onMainFailure(model)
    }
  }

  private fun onMainProgress(model: MainProgress) {
    progressBar.visibility = View.VISIBLE
  }

  private fun onMainSuccess(model: MainSuccess) {
    progressBar.visibility = View.GONE
    refreshLayout.isRefreshing = false
    mainAdapter.swapData(model.items)
  }

  private fun onMainFailure(model: MainFailure) {
    progressBar.visibility = View.GONE
    refreshLayout.isRefreshing = false
    Snackbar
        .make(list, model.error.message.toString(), Toast.LENGTH_LONG)
        .show()
  }

  companion object {
    private const val RC_LOCATION = 42
  }
}
