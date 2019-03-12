package com.example.citytemperature.presentation

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.example.citytemperature.R
import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.service.location.GpsResolutionProvider
import com.example.citytemperature.service.permission.PermissionProvider
import com.example.citytemperature.service.permission.PermissionResult
import com.google.android.gms.common.api.Status
import dagger.android.AndroidInjection
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.citytemperature.util.Formatter
import android.support.v7.widget.SimpleItemAnimator
import io.reactivex.Single

class MainActivity : AppCompatActivity(), PermissionProvider,
    GpsResolutionProvider, CityListScreen {

    private val requestCodePermission = 1
    private val requestCodeGpsResolution = 2
    private var gpsResolutionObservable: BehaviorSubject<Int>? = null
    private var permissionObservable: BehaviorSubject<PermissionResult>? = null

    @Inject
    lateinit var presenter: CityListPresenter
    @Inject
    lateinit var formatter: Formatter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(dividerItemDecoration)

        buttonRetry.setOnClickListener {
            presenter.loadCities()
        }

        presenter.loadCities()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showCities(cities: List<City>) {
        recyclerView.adapter = Adapter(this, cities, presenter, formatter)
    }

    override fun cityUpdated(position: Int) {
        recyclerView.adapter?.notifyItemChanged(position)
    }

    override fun showProgressBar(isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id != null) {
            when (id) {
                R.id.action_map -> presenter.openMapClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showCitiesOnMap(cities: ArrayList<City>) {
        MapActivity.startActivity(this, cities)
    }

    override fun requestPermission(permissions: Array<String>): Single<PermissionResult> {
        permissionObservable = BehaviorSubject.create<PermissionResult>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPermissions(permissions, requestCodePermission)
        }
        return Single.fromObservable { permissionObservable }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == requestCodePermission) {
            val permissionResult =
                PermissionResult(requestCode, permissions, grantResults)
            permissionObservable?.onNext(permissionResult)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun startGpsResolution(status: Status): Single<Int> {
        gpsResolutionObservable = BehaviorSubject.create()
        status.startResolutionForResult(this, requestCodeGpsResolution)
        return gpsResolutionObservable?.firstOrError()!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestCodeGpsResolution) {
            gpsResolutionObservable?.onNext(resultCode)
            gpsResolutionObservable?.onComplete()
        }
    }

    override fun showNoInternetConnectionError() {
        errorLayout.visibility = View.VISIBLE
        textError.setText(R.string.error_no_internet_connection)
    }

    override fun showServerError() {
        errorLayout.visibility = View.VISIBLE
        textError.setText(R.string.error_server)
    }

    override fun showGpsNotEnabledError() {
        errorLayout.visibility = View.VISIBLE
        textError.setText(R.string.error_gps_disabled)
    }

    override fun showLocationPermisssionsNotGrantedError() {
        errorLayout.visibility = View.VISIBLE
        textError.setText(R.string.error_permission_not_granted)
    }

    override fun showNeverAskAgainError() {
        errorLayout.visibility = View.VISIBLE
        textError.setText(R.string.error_never_ask_again_selected)
    }

    override fun showGpsUnavailableError() {
        errorLayout.visibility = View.VISIBLE
        textError.setText(R.string.error_gps_unavailable)
    }

    override fun hideErrors() {
        errorLayout.visibility = View.GONE
    }
}
