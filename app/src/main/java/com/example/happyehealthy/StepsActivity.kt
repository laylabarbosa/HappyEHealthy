package com.example.happyehealthy

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StepsActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private lateinit var stepsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steps)

        // Initialize views
        stepsTextView = findViewById(R.id.stepsTextView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // Device does not support step counter sensor
            stepsTextView.text =
                getString(R.string.step_counter_sensor_not_available_on_this_device)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val steps = it.values[0].toInt()
                stepsTextView.text = "Steps: $steps"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
