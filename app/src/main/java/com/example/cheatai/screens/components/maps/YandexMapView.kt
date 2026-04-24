package com.example.cheatai.screens.components.maps
import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

@Composable
fun YandexMapView(
    modifier: Modifier = Modifier,
    searchQuery: String? = null,
    onPlaceFound: (Point?, String?) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var searchSession by remember { mutableStateOf<Session?>(null) }
    val mapView = remember { MapView(context) }
    LaunchedEffect(Unit) {
        mapView.mapWindow.map.move(
            CameraPosition(Point(55.751574, 37.573856),
                12f, 0f, 0f)
        )
    }
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNullOrBlank()) return@LaunchedEffect

        searchSession?.cancel()

        val searchManager = SearchFactory.getInstance()
            .createSearchManager(SearchManagerType.COMBINED)


        val searchOptions = SearchOptions().apply {
            searchTypes = SearchType.GEO.value
            resultPageSize = 1
        }

        val searchListener = object : Session.SearchListener {
            override fun onSearchResponse(response: Response) {
                val results = response.collection.children
                if (results.isNotEmpty()) {
                    val topResult = results[0].obj
                    val point = topResult?.geometry?.firstOrNull()?.point
                    val name = topResult?.name
                    if (point != null) {
                        mapView.mapWindow.map.move(CameraPosition(point, 15f, 0f, 0f))
                        addPlacemark(mapView, point, name ?: searchQuery)
                        onPlaceFound(point, name)
                        return
                    }
                }
                onPlaceFound(null, null)
            }

            override fun onSearchError(error: Error) {
                println("Ошибка поиска: $error")
                onPlaceFound(null, null)
            }

        }
        val visibleRegion = mapView.mapWindow.map.visibleRegion
        val boundingBox = VisibleRegionUtils.getBounds(visibleRegion)
        val geometry = Geometry.fromBoundingBox(boundingBox)

        searchSession = searchManager.submit(
            searchQuery,
            geometry,
            searchOptions,
            searchListener
        )
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                androidx.lifecycle.Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}

private fun addPlacemark(mapView: MapView, point: Point, title: String) {

    val imageProvider = ImageProvider.fromResource(
        mapView.context,
        com.example.cheatai.R.drawable.ic_pin
    )

    val iconStyle = IconStyle().apply {
        scale = 0.1f
        anchor = PointF(0.5f, 1.0f)
    }

    val placemarkCollection = mapView.mapWindow.map.mapObjects.addCollection()
    val styler = placemarkCollection.placemarksStyler()
    styler.setScaleFunction(listOf(
        PointF(10f, 0.35f),
        PointF(15f, 0.7f),
        PointF(19f, 1.0f)
    ))


    val placemark = placemarkCollection.addPlacemark()
    placemark.geometry = point
    placemark.setIcon(imageProvider, iconStyle)

    placemark.userData = title
    placemark.setText(title)

    val textStyle = TextStyle().apply {
        size = 13f
        color = android.graphics.Color.BLACK
        outlineColor = android.graphics.Color.WHITE
        placement = TextStyle.Placement.BOTTOM
        offset = 5f
    }
    placemark.setTextStyle(textStyle)
}