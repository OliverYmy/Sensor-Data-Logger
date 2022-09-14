package com.example.helloworld;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "sensor data logger";
    private TextView mTimestampTextView;
    private TextView mAccelerometerSensorTextView;
    private TextView mLinearAccelerometerSensorTextView;
    private TextView mBarometerSensorTextView;
    private TextView mMagneticSensorTextView;
    private TextView mGyroscopeSensorTextView;
    private TextView mOrientationSensorTextView;
    private TextView mModeTextView;
    private Button btn_start;
    private Button btn_stop;
    private SensorManager mSensorManager;
    private MySensorEventListener mMySensorEventListener;
    private long logtimestamp = 0;
    private String filePath = "";
    private String accelfileName = "";
    private String linearaccelfileName = "";
//    private String barofileName = "";
//    private String accelData = "";
//    private String linearAccelData = "";
//    private String baroData = "";
    private float currentBaro = 0;
    private float[] mAccelerometerReading = new float[3];
//    private float[] mLinearAccelerometerReading = new float[3];
    private float[] mMagneticFieldReading = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimestampTextView = findViewById(R.id.timestamp);
        mAccelerometerSensorTextView = findViewById(R.id.accelerometer_sensor);
        mLinearAccelerometerSensorTextView = findViewById(R.id.linear_accelerometer_sensor);
        mBarometerSensorTextView = findViewById(R.id.barometer_sensor);
//        mMagneticSensorTextView = findViewById(R.id.magnetic_sensor);
//        mGyroscopeSensorTextView = findViewById(R.id.gyroscope_sensor);
//        mOrientationSensorTextView = findViewById(R.id.orientation_sensor);
        mModeTextView = findViewById(R.id.current_mode);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_start.setEnabled(false);
        btn_start.setTextColor(Color.GRAY);
        btn_stop.setEnabled(false);
        btn_stop.setTextColor(Color.GRAY);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        findViewById(R.id.btn_walk).setOnClickListener(this);
        findViewById(R.id.btn_sit).setOnClickListener(this);
        findViewById(R.id.btn_run).setOnClickListener(this);
        findViewById(R.id.btn_upstairs).setOnClickListener(this);
        findViewById(R.id.btn_downstairs).setOnClickListener(this);
        findViewById(R.id.btn_subway).setOnClickListener(this);

        try {
            filePath = getSdCardDir();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public String getSdCardDir() throws IOException {
        boolean exist = isSdCardExist();
        String filePath = "";
        if(exist) {
            filePath = getExternalFilesDir("").getAbsolutePath();
        } else {
            Log.d(TAG, "SD card do not exist!");
            filePath = "SD card do not exist";
        }
        return filePath;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager == null) {
            return;
        }

//        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        if (accelerometerSensor != null) {
//            //register accelerometer sensor listener
//            mSensorManager.registerListener(mMySensorEventListener, accelerometerSensor,
//                                            SensorManager.SENSOR_DELAY_GAME);
//        } else {
//            Log.d(TAG, "Accelerometer sensors are not supported on current devices.");
//        }
//
//        Sensor linearaccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        if (accelerometerSensor != null) {
//            //register accelerometer sensor listener
//            mSensorManager.registerListener(mMySensorEventListener, linearaccelerometerSensor,
//                                            SensorManager.SENSOR_DELAY_GAME);
//        } else {
//            Log.d(TAG, "Linear Accelerometer sensors are not supported on current devices.");
//        }
//
//        Sensor barometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
//        if (barometerSensor != null) {
//            //register barometer sensor listener
//            mSensorManager.registerListener(mMySensorEventListener, barometerSensor,
//                    SensorManager.SENSOR_DELAY_GAME);
//        } else {
//            Log.d(TAG, "Barometer sensors are not supported on current devices.");
//        }

//        Sensor magneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        if (magneticSensor != null) {
//            //register magnetic sensor listener
//            mSensorManager.registerListener(mMySensorEventListener, magneticSensor,
//                                            SensorManager.SENSOR_DELAY_GAME);
//        } else {
//            Log.d(TAG, "Magnetic sensors are not supported on current devices.");
//        }

//        Sensor gyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        if (gyroscopeSensor != null) {
//            //register gyroscope sensor listener
//            mSensorManager.registerListener(mMySensorEventListener, gyroscopeSensor,
//                                            SensorManager.SENSOR_DELAY_GAME);
//        } else {
//            Log.d(TAG, "Gyroscope sensors are not supported on current devices.");
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorManager == null) {
            return;
        }
        //unregister all listener
        mSensorManager.unregisterListener(mMySensorEventListener);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_start:
                btn_start.setEnabled(false);
                btn_start.setTextColor(Color.GRAY);
                btn_stop.setEnabled(true);
                btn_stop.setTextColor(Color.BLACK);

                // create sensor data file
//                logtimestamp = System.currentTimeMillis();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//                Date date = new Date(logtimestamp);
//                accelfileName = simpleDateFormat.format(date) + "_accel_" + mModeTextView.getText() + ".csv";
//                linearaccelfileName = simpleDateFormat.format(date) + "_linearaccel_" + mModeTextView.getText() + ".csv"
                accelfileName = "accel_" + mModeTextView.getText() + ".csv";
                linearaccelfileName = "linearaccel_" + mModeTextView.getText() + ".csv";
//                barofileName = simpleDateFormat.format(date) + "_baro_" + mModeTextView.getText() + ".csv";
                createFile(filePath,accelfileName);
                createFile(filePath, linearaccelfileName);
//                createFile(filePath,barofileName);

                // set sensor event listener to retrieve sensor data during every interval
                this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                this.mMySensorEventListener = new MySensorEventListener();
                startSensor(mSensorManager);
                onResume();
                break;
            case R.id.btn_stop:
                btn_stop.setEnabled(false);
                btn_stop.setTextColor(Color.GRAY);
                btn_start.setEnabled(true);
                btn_start.setTextColor(Color.BLACK);
                String str = "########################################\n";
                try {
                    Files.write(Paths.get(filePath + File.separator + accelfileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Files.write(Paths.get(filePath + File.separator + linearaccelfileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onPause();
                break;

//            case R.id.btn_export:
//                exportAccelData(logtimestamp);
//                exportLinearAccelData(logtimestamp);
//                exportBaroData(logtimestamp);
//                accelData = "";
//                linearAccelData = "";
//                baroData = "";
//                break;

            case R.id.btn_walk:
                mModeTextView.setText("walk");
                btn_start.setEnabled(true);
                btn_start.setTextColor(Color.BLACK);
                break;
            case R.id.btn_sit:
                mModeTextView.setText("sit");
                btn_start.setEnabled(true);
                btn_start.setTextColor(Color.BLACK);
                break;
            case R.id.btn_run:
                mModeTextView.setText("run");
                btn_start.setEnabled(true);
                btn_start.setTextColor(Color.BLACK);
                break;
            case R.id.btn_upstairs:
                mModeTextView.setText("upstairs");
                btn_start.setEnabled(true);
                btn_start.setTextColor(Color.BLACK);
                break;
            case R.id.btn_downstairs:
                mModeTextView.setText("downstairs");
                btn_start.setEnabled(true);
                btn_start.setTextColor(Color.BLACK);
                break;
            case R.id.btn_subway:
                mModeTextView.setText("subway");
                btn_start.setEnabled(true);
                btn_start.setTextColor(Color.BLACK);
                break;
        }
    }

    private void startSensor(SensorManager mSensorManager) {
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            //register accelerometer sensor listener
            mSensorManager.registerListener(mMySensorEventListener, accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.d(TAG, "Accelerometer sensors are not supported on current devices.");
        }

        Sensor linearaccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (accelerometerSensor != null) {
            //register accelerometer sensor listener
            mSensorManager.registerListener(mMySensorEventListener, linearaccelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.d(TAG, "Linear Accelerometer sensors are not supported on current devices.");
        }

        Sensor barometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (barometerSensor != null) {
            //register barometer sensor listener
            mSensorManager.registerListener(mMySensorEventListener, barometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.d(TAG, "Barometer sensors are not supported on current devices.");
        }
    }

    private void createFile(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
/*
    private void exportLinearAccelData(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date(timestamp);
        String filename = simpleDateFormat.format(date) + "_linearaccel_" + mModeTextView.getText() + ".csv";
        File linearaccelFile = new File(filePath, filename);
        String [] linearaccelArr = linearAccelData.split("\\s+");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(linearaccelFile);
            for(String ss : linearaccelArr) {
                fileOutputStream.write(ss.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write('\r');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void exportAccelData(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date(timestamp);
        String filename = simpleDateFormat.format(date) + "_accel_" + mModeTextView.getText() + ".csv";
        File accelFile = new File(filePath, filename);
        String [] accelArr = accelData.split("\\s+");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(accelFile);
            for(String ss : accelArr) {
                fileOutputStream.write(ss.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write('\r');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void exportBaroData(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date(timestamp);
        String filename = simpleDateFormat.format(date) + "_baro_" + mModeTextView.getText() + ".csv";
        File accelFile = new File(filePath, filename);
        String [] accelArr = baroData.split("\\s+");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(accelFile);
            for(String ss : accelArr) {
                fileOutputStream.write(ss.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write('\r');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/

    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setGroupingUsed(false);
            nf.setMaximumFractionDigits(5);
            nf.setMinimumFractionDigits(5);
            mTimestampTextView.setText(String.valueOf(System.currentTimeMillis()));
            String str = "";
            switch(event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    mAccelerometerReading = event.values;
                    mAccelerometerSensorTextView.setText("[x:" + nf.format(event.values[0])
                                                        + ", y:" + nf.format(event.values[1])
                                                        + ", z:" + nf.format(event.values[2]) + "]");
                    str = String.valueOf(System.currentTimeMillis()) + ","
                                    + nf.format(event.values[0]) + ","
                                    + nf.format(event.values[1]) + ","
                                    + nf.format(event.values[2]) + ","
                                    + nf.format(currentBaro) + "\n";
                    try {
                        Files.write(Paths.get(filePath + File.separator + accelfileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    accelData += String.valueOf(System.currentTimeMillis()) + ","
//                                + nf.format(event.values[0]) + ","
//                                + nf.format(event.values[1]) + ","
//                                + nf.format(event.values[2]) + " ";
                    break;

                case Sensor.TYPE_LINEAR_ACCELERATION:
//                    mLinearAccelerometerReading = event.values;
                    mLinearAccelerometerSensorTextView.setText("[x:" + nf.format(event.values[0])
                                                                + ", y:" + nf.format(event.values[1])
                                                                + ", z:" + nf.format(event.values[2]) + "]");
                    str = String.valueOf(System.currentTimeMillis()) + ","
                            + nf.format(event.values[0]) + ","
                            + nf.format(event.values[1]) + ","
                            + nf.format(event.values[2]) + ","
                            + nf.format(currentBaro) + "\n";
                    try {
                        Files.write(Paths.get(filePath + File.separator + linearaccelfileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    linearAccelData += String.valueOf(System.currentTimeMillis()) + ","
//                            + nf.format(event.values[0]) + ","
//                            + nf.format(event.values[1]) + ","
//                            + nf.format(event.values[2]) + " ";
                    break;

//                case Sensor.TYPE_GYROSCOPE:
//                    mGyroscopeSensorTextView.setText("[x:" + nf.format(event.values[0])
//                                                    + ", y:" + nf.format(event.values[1])
//                                                    + ", z:" + nf.format(event.values[2]) + "]");
//                    break;

//                case Sensor.TYPE_MAGNETIC_FIELD:
//                    mMagneticFieldReading = event.values;
//                    mMagneticSensorTextView.setText("[x:" + nf.format(event.values[0])
//                                                    + ", y:" + nf.format(event.values[1])
//                                                    + ", z:" + nf.format(event.values[2]) + "]");
//                    break;

                case Sensor.TYPE_PRESSURE:
                    mBarometerSensorTextView.setText("[" + nf.format(event.values[0]) + "]");
                    currentBaro = event.values[0];
//                    str = String.valueOf(System.currentTimeMillis()) + ","
//                            + nf.format(event.values[0]) + "\r";
//                    try {
//                        Files.write(Paths.get(filePath + barofileName), str.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    baroData += String.valueOf(System.currentTimeMillis()) + ","
//                                + nf.format(event.values[0]) + " ";
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d(TAG, "onAccuracyChanged:" + sensor.getType() + "->" + accuracy);
        }
    }
}
