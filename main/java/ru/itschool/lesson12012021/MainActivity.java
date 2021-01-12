package ru.itschool.lesson12012021;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_stop, btn_list;
    SensorManager sensorManager;
    Sensor lightsensor;
    TextView textlight, textviewall;
    List<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = findViewById(R.id.button_start);
        btn_stop = findViewById(R.id.button_stop);
        btn_list = findViewById(R.id.button_list);
        textlight = findViewById(R.id.textlight);
        textviewall = findViewById(R.id.textviewall);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        lightsensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightsensor == null) {
            textlight.setText("Датчик освещения не обнаружен!");
        } else {
            textlight.append("Датчик освещения " + lightsensor.getName() + " доступен");
            sensorManager.registerListener(LightSensorListener, lightsensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startService(new Intent(MainActivity.this, MyService.class));
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(LightSensorListener, lightsensor);
                StringBuilder sb = new StringBuilder();
                int i=0;
                for (Sensor sensor : sensors){
                    i++;
                    sb.append(i+" name = ").append(sensor.getName())
                            .append(", type = ").append(sensor.getType())
                            .append("\nvendor = ").append(sensor.getVendor())
                            .append(", version = ").append(sensor.getVersion())
                            .append("\nmax = ").append(sensor.getMaximumRange())
                            .append(", resolution = ").append(sensor.getResolution())
                            .append("\n=============================================\n");
                }
                textviewall.setText(sb);
            }
        });

    }

    private final SensorEventListener LightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                if (event.values[0] > 100) {
                    startService(new Intent(MainActivity.this, MyService.class));
                    textlight.setText("Light \n" + event.values[0]);
                } else {
                    stopService(new Intent(MainActivity.this, MyService.class));
                    textlight.setText("Light \n" + event.values[0]);
                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


}