package com.example.plantycare.data.repository

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.plantycare.sensor.LightSensorTracker
import com.example.plantycare.sensor.ShakeDetector
import kotlinx.coroutines.flow.Flow

class SensorRepository(private val context: Context) {

    private val lightTracker = LightSensorTracker(context)
    private val shakeDetector = ShakeDetector(context)

    val lightFlow: Flow<Float> = lightTracker.lightFlow
    val shakeFlow: Flow<Unit> = shakeDetector.shakeFlow

    // 1. Getar Konfirmasi Siram Sukses (Single Short Pulse)
    fun triggerSuccessVibration() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(180, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createOneShot(180, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(180)
            }
        } catch (_: Exception) {}
    }

    // 2. Getar Peringatan Jadwal Siram Tiba (Double Pulse Urgent Pattern: bzzz - jeda - bzzz)
    fun triggerWateringAlertVibration() {
        try {
            val pattern = longArrayOf(0, 300, 150, 300)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createWaveform(pattern, -1)
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(pattern, -1)
            }
        } catch (_: Exception) {}
    }

    fun triggerVibration() {
        triggerSuccessVibration()
    }
}
