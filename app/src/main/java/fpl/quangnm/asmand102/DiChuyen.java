package fpl.quangnm.asmand102;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.List;


import fpl.quangnm.asmand102.data.DbHelper;

public class DiChuyen extends AppCompatActivity  implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private int stepCount = 0;


    private DbHelper dbHelper;
    Button btnShow;


    private TextView stepCountView;
    ListView lvLog;


    private int initialStepCount = 0;
    private boolean isInitialStepCountSet = false;
    ArrayAdapter adapter;
    List<String> ls;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


//        startActivity(new Intent(this, DemoFirebaseActivity.class));


        // ánh xạ
        btnShow = findViewById(R.id.btn_show);
        stepCountView = findViewById(R.id.step_count_view);
        lvLog = findViewById(R.id.lv_log);
        Button resetButton = findViewById(R.id.reset_button);
        dbHelper = new DbHelper(this);


        // Khởi tạo SensorManager và Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);




        if (stepCounterSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor!", Toast.LENGTH_SHORT).show();
        }


        //------------------------
        ls = dbHelper.getAllSteps(); // chạy lệnh này thì mới có totalStep


        adapter = new ArrayAdapter(DiChuyen.this,
                android.R.layout.simple_list_item_1,ls);


        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ls.clear();
                ls.addAll( dbHelper.getAllSteps() );
                adapter.notifyDataSetChanged();
                lvLog.setAdapter(adapter);


            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStepCount();
            }
        });
    }




    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {


            if (!isInitialStepCountSet) {
                initialStepCount = (int) event.values[0];
                isInitialStepCountSet = true;
            }
            int currentStepCount = (int) event.values[0] - initialStepCount;


//            stepCount = (int) event.values[0];
            dbHelper.insertStepCount(currentStepCount);
            stepCountView.setText("Step Count: " + currentStepCount);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {


    }






    @Override
    protected void onResume() {
        super.onResume();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }


    private void resetStepCount() {
        initialStepCount = 0;
        isInitialStepCountSet = false;
        stepCountView.setText("Step Count: 0");
        dbHelper.resetTable();
        ls.clear();
        ls.addAll( dbHelper.getAllSteps() );
        adapter.notifyDataSetChanged();
    }


}
