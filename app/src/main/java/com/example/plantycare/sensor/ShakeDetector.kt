package com.example.plantycare.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.sqrt

class ShakeDetector(context: Context) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    companion object {
        private const val SHAKE_THRESHOLD_GRAVITY = 2.4f
        private const val SHAKE_SLOP_TIME_MS = 600
    }

    val shakeFlow: Flow<Unit> = callbackFlow {
        if (sensorManager == null || accelerometer == null) {
            awaitClose { }
            return@callbackFlow
        }

        var lastShakeTimestamp = 0L

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val gX = x / SensorManager.GRAVITY_EARTH
                val gY = y / SensorManager.GRAVITY_EARTH
                val gZ = z / SensorManager.GRAVITY_EARTH

                // gForce will be close to 1 when still.
                val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

                if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                    val now = System.currentTimeMillis()
                    if (lastShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                        return
                    }
                    lastShakeTimestamp = now
                    trySend(Unit)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            listener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
