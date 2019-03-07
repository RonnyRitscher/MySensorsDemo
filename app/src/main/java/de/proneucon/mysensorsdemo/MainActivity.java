package de.proneucon.mysensorsdemo;
/**
 * Verwenden von Sensoren - zugriff auf die Hardware des gerätes
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

        //initialisieren der Member:
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
            //wenn sensor vorhanden, dann infos übergeben: (%1$s (Hersteller: %2$s, Version: %3$d, dynamisch: %4$s)\n)
            tv_main.append(getString(R.string.template,
                    sensor.getName(),               //Name
                    sensor.getVendor(),             //Hersteller
                    sensor.getVersion(),            //Version
                    Boolean.toString(isDynamic)     //dynamisch
            ));
        }

        //der sensor hat hier bereits die gültigkeit verloren
        //daher noch einmal direkt ansteuern
        sensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT); // wir verwenden den Licht-Sensor
        //zur sicherheit abprüfen ob diesen vorhanden ist:
        if(sensor == null){
            Toast.makeText(this, "Kein Lichtsensor vorhanden!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Lichtsensor vorhanden", Toast.LENGTH_SHORT).show();

            //EventListener erstellen und verwenden:
            listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {        //wenn sich im sensor selbst Werte ändern (zB: das Licht wird heller)
                    //values beinhaltet die Änderungen:
                    if(event.values.length>0){

                        int i=1;
                        for(float f : event.values){
                            Log.d(TAG, "onSensorChanged: " +i+ ": " + f);
                            i++;
                            /*
                            1.Wert: Lux / Lichteinfall
                            2.Wert: Celvin / Farbtemparatur
                            3.Wert:
                             */
                        }


                    }
                }
                //
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            };
            //registrieren des Listeners:  *in der onPause wieder unregistrieren
            manager.registerListener(listener , sensor, SensorManager.SENSOR_DELAY_NORMAL); //


        }
    }

    //------------------------------------
    // oP - um manager zu melden
    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener); //Listener unregistrieren
    }
}
