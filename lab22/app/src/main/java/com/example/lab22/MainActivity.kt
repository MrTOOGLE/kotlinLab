package com.example.lab22

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var statusText: TextView
    private lateinit var distanceText: TextView
    private lateinit var newPointButton: Button
    private lateinit var settingsButton: Button

    private var targetLatitude: Double = 0.0
    private var targetLongitude: Double = 0.0
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private var hasTargetPoint = false
    private var isLocationFound = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val EARTH_RADIUS = 6371000.0 // метры
        private const val TARGET_RADIUS_KM = 2.5
        private const val WIN_DISTANCE_METERS = 100.0
        private const val DEGREE_TO_KM_APPROX = 0.04 / 5.0 // 0.04 градуса ≈ 5 км
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupLocationManager()
        checkLocationPermissions()
    }

    private fun initViews() {
        statusText = findViewById(R.id.statusText)
        distanceText = findViewById(R.id.distanceText)
        newPointButton = findViewById(R.id.newPointButton)
        settingsButton = findViewById(R.id.settingsButton)

        newPointButton.setOnClickListener {
            if (isLocationFound) {
                generateNewTargetPoint()
            } else {
                Toast.makeText(this, "Сначала определите ваше местоположение", Toast.LENGTH_SHORT).show()
            }
        }

        settingsButton.setOnClickListener {
            openAppSettings()
        }

        updateUI()
    }

    private fun setupLocationManager() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            startLocationUpdates()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this, "Разрешение на геолокацию необходимо для работы игры",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            // Запрашиваем обновления от GPS
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1f,
                    this
                )
            }

            // Запрашиваем обновления от сети
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1f,
                    this
                )
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        currentLatitude = location.latitude
        currentLongitude = location.longitude
        isLocationFound = true

        if (hasTargetPoint) {
            val distance = calculateDistance(
                currentLatitude, currentLongitude,
                targetLatitude, targetLongitude
            )

            if (distance <= WIN_DISTANCE_METERS) {
                // Победа!
                hasTargetPoint = false
                updateUI()
            } else {
                updateDistanceDisplay(distance)
            }
        } else {
            // Автоматически генерируем первую точку при получении местоположения
            generateNewTargetPoint()
        }
    }

    private fun generateNewTargetPoint() {
        if (!isLocationFound) return

        // Генерируем случайную точку в радиусе TARGET_RADIUS_KM
        val angle = Random.nextDouble(0.0, 2 * PI)
        val radius = Random.nextDouble(0.0, TARGET_RADIUS_KM)

        // Приблизительное преобразование км в градусы
        val deltaLat = radius * DEGREE_TO_KM_APPROX * cos(angle)
        val deltaLon = radius * DEGREE_TO_KM_APPROX * sin(angle)

        targetLatitude = currentLatitude + deltaLat
        targetLongitude = currentLongitude + deltaLon
        hasTargetPoint = true

        updateUI()
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val deltaLon = Math.toRadians(lon2 - lon1)

        val distance = EARTH_RADIUS * acos(
            sin(lat1Rad) * sin(lat2Rad) +
                    cos(lat1Rad) * cos(lat2Rad) * cos(deltaLon)
        )

        return distance
    }

    private fun updateUI() {
        if (!isLocationFound) {
            statusText.text = "Определение местоположения..."
            statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
            distanceText.text = ""
        } else if (!hasTargetPoint) {
            statusText.text = "Ура, точка найдена!"
            statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            distanceText.text = ""
        } else {
            statusText.text = "Точка загадана, ищите!"
            statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))

            val distance = calculateDistance(
                currentLatitude, currentLongitude,
                targetLatitude, targetLongitude
            )
            updateDistanceDisplay(distance)
        }
    }

    private fun updateDistanceDisplay(distance: Double) {
        val distanceText = when {
            distance >= 1000 -> String.format("Текущее расстояние до точки: %.1f км", distance / 1000)
            else -> String.format("Текущее расстояние до точки: %.0f м", distance)
        }
        this.distanceText.text = distanceText
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this)
    }

    // Обязательные методы LocationListener
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}
