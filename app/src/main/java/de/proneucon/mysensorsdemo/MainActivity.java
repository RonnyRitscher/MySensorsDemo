package de.proneucon.mysensorsdemo;
/**
 * Verwenden von Sensoren - zugriff auf die Hardware des gerätes
 */

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //MEMBER
    private static final String TAG = MainActivity.class.getSimpleName(); //LOG-TAG
    private TextView tv_main;
    private TextView tv_sensor1_name;
    private TextView tv_sensor1_value1;
    private TextView tv_sensor1_value2;
    private TextView tv_sensor1_value3;
    private TextView tv_sensor2_name;
    private TextView tv_sensor2_value1;
    private TextView tv_sensor2_value2;
    private TextView tv_sensor2_value3;

    private SensorManager manager;
    private Sensor sensor;
    private Sensor sensor2;
    private SensorEventListener listener;
    private SensorEventListener listener2;
    //private SensorEventListener2 sel_listener2;
    boolean isDynamic;
    private float light;
    private float light2;
    private float light3;
    private float prox1;
    private float prox2;


    private ScrollView sv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialisieren der Member:
        tv_main = findViewById(R.id.tv_main);
        sv_main = findViewById(R.id.sv_main);
        tv_sensor1_name = findViewById(R.id.tv_sensor1_name);
        tv_sensor1_value1 = findViewById(R.id.tv_sensor1_value_1);
        tv_sensor1_value2 = findViewById(R.id.tv_sensor1_value_2);
        tv_sensor1_value3 = findViewById(R.id.tv_sensor1_value_3);
        tv_sensor2_name = findViewById(R.id.tv_sensor2_name);
        tv_sensor2_value1 = findViewById(R.id.tv_sensor2_value_1);
        tv_sensor2_value2 = findViewById(R.id.tv_sensor2_value_2);

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

            //AUSGABE DER ZUR VERFÜGUNG STEHENDEN SENSOREN:
            //ReportingMode:
            int i =-1;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) { //ReportingMode erst ab VERSION L verfügbar
                i = sensor.getReportingMode();
                //wenn sensor vorhanden, dann infos übergeben: (%1$s (Hersteller: %2$s, Version: %3$d, dynamisch: %4$s)\n)
                tv_main.append(getString(R.string.template2,
                        sensor.getName(),               //Name
                        sensor.getVendor(),             //Hersteller
                        sensor.getVersion(),            //Version
                        i,
                        Boolean.toString(isDynamic)     //dynamisch
                ));
            }else{
                //wenn sensor vorhanden, dann infos übergeben: (%1$s (Hersteller: %2$s, Version: %3$d, dynamisch: %4$s)\n)
                tv_main.append(getString(R.string.template,
                        sensor.getName(),               //Name
                        sensor.getVendor(),             //Hersteller
                        sensor.getVersion(),            //Version
                        Boolean.toString(isDynamic)     //dynamisch
                ));
            }
        }

        //der sensor hat hier bereits die gültigkeit verloren

        /* HIER DER CODE FÜR DEN LIGHT_SENSOR */
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
                    //SENSOR-LIGHT : values beinhaltet die Änderungen:
                    if(event.values.length>0){
//                        int i=1;
//                        for(float f : event.values){
//                            //Werte in dem Logcat ausgeben:
//                            /*0.Wert: Lux / Lichteinfall ... 1.Wert: Celvin / Farbtemparatur ...2.Wert:*/
//                            Log.d(TAG, "onSensorChanged: " +i+ ": " + f);
//                            i++;
//                        }
                        //Veränderungen in dem TextView ausgeben:
                        light = event.values[0];
                        light2 = event.values[1];
                        light2 = event.values[2];
                        Log.d(TAG, "onSensorChanged: Event-Value(Lux): " + light);
                        tv_main.append(Float.toString(light) + "\n");
                        tv_sensor1_name.setText("Light_Sensor: ");
                        tv_sensor1_value1.setText(Float.toString(light));
                        tv_sensor1_value2.setText(Float.toString(light2));
                        tv_sensor1_value3.setText(Float.toString(light3));

                        //scrollview immer bis nach unten scollen lassen:
                        sv_main.fullScroll(View.FOCUS_DOWN);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            //registrieren des Listeners:  *in der onPause wieder unregistrieren
            Log.d(TAG, "onPause: Listener wurde angemeldet/registriert");
            manager.registerListener(listener , sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        /* HIER DER CODE FÜR DEN PROXIMITY_SENSOR */

        sensor2 = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY); // wir verwenden den Licht-Sensor

        //zur sicherheit abprüfen ob diesen vorhanden ist:
        if(sensor2 == null){
            Toast.makeText(this, "Kein TYPE_PROXIMITY vorhanden!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "TYPE_PROXIMITY vorhanden", Toast.LENGTH_SHORT).show();

            //EventListener erstellen und verwenden:
            listener2 = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {        //wenn sich im sensor selbst Werte ändern (zB: das Licht wird heller)
                    //Sensor.TYPE_PROXIMITY :
                    tv_sensor2_name.setText("Proximity_Sensor: ");
                    if(event.values.length>0){
                        if(event.values.length>0){
                            if(event.values[0]<sensor2.getMaximumRange()){
                                //Toast.makeText(MainActivity.this, "TYPE_PROXIMITY Sensor geändert", Toast.LENGTH_SHORT).show();
                                tv_main.setBackgroundColor(Color.RED);
                                prox1 =event.values[0]; //Abstand
                                tv_sensor2_value1.setText(Float.toString(prox1));
                                tv_sensor2_value2.setText("ausgelöst");


                            }else{
                                tv_main.setBackgroundColor(Color.WHITE);
                                prox1 =event.values[0];
                                tv_sensor2_value1.setText(Float.toString(prox1));
                                tv_sensor2_value2.setText("nicht ausgelöst");

                            }

                        }
                    }

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            //registrieren des Listeners LIGHT:  *in der onPause wieder unregistrieren
            Log.d(TAG, "onPause: Listener wurde angemeldet/registriert");
            manager.registerListener(listener , sensor, SensorManager.SENSOR_DELAY_NORMAL);
            //registrieren des Listeners LIGHT:  *in der onPause wieder unregistrieren
            Log.d(TAG, "onPause: Listener wurde angemeldet/registriert");
            manager.registerListener(listener2 , sensor2, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    //------------------------------------
    // oP - um manager zu melden
    @Override
    protected void onPause() {
        super.onPause();
        //unregister Listener LIGHT
        Log.d(TAG, "onPause: Listener wurde abgemeldet/unregistriert");
        manager.unregisterListener(listener); //Listener unregistrieren

        //unregister Listener PROXIMITY
        Log.d(TAG, "onPause: Listener wurde abgemeldet/unregistriert");
        manager.unregisterListener(listener2); //Listener unregistrieren
    }
}
