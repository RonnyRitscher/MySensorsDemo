package de.proneucon.mysensorsdemo;
/**
 * Verwenden von Sensoren - zugriff auf die Hardware des gerätes
 */

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //MEMBER
    private static final String TAG = MainActivity.class.getSimpleName(); //LOG-TAG
    private TextView tv_main;
    private SensorManager manager;
    private Sensor sensor;
    private SensorEventListener listener;
    private SensorEventListener2 listener2;
    boolean isDynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_main = findViewById(R.id.tv_main);
    }

    //------------------------------------
    // oR
    @Override
    protected void onResume() {
        super.onResume();
        tv_main.setText("");
        //manager initialisieren:
        manager = (SensorManager) getSystemService(SENSOR_SERVICE); //zugriff auf das System und verwenden des SENSOR_SERVICES
        //Liste der Sensoren erstellen:
        List<Sensor> sensorList = manager.getSensorList(Sensor.TYPE_ALL); // beschafft alle Sensoren
        //anzeigen der Liste lassen:
        for (Sensor sensor : sensorList) {

            isDynamic = false; //explizit auf false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                isDynamic = sensor.isDynamicSensor(); // Dynamische Sensoren sind aufsetzbare/externe Sensoren(zB:Wettersensor)
            }
            //wenn sensor vorhanden, dann infos übergeben:
            tv_main.append(getString(R.string.template,
                    sensor.getName(),
                    sensor.getVendor(),
                    sensor.getVersion(),
                    Boolean.toString(isDynamic)
            ));


        }

    }

    //------------------------------------
    // oP - um manager zu melden
    @Override
    protected void onPause() {
        super.onPause();
    }
}
