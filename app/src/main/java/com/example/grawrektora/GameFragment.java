package com.example.grawrektora;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.Console;
import java.util.Objects;

import static android.content.Context.SENSOR_SERVICE;


public class GameFragment extends Fragment implements SensorEventListener {
    private GameOfRektorMeneger gameMeneger;
    private Sensor linear;
    private SensorManager sensorManager;
    private TextView eventTextView, eventHeaderView, eventQuestionView;
    private ImageView eventImageView;
    private ProgressBar[] scores;
    private final float THRESHOLD = 10.0f;
    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameMeneger = GameOfRektorMeneger.getInstance(getContext());
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        linear = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        scores = new ProgressBar[3];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_f, container, false);
        eventTextView = (TextView)view.findViewById(R.id.event_text);
        eventQuestionView = (TextView)view.findViewById(R.id.event_question);
        eventHeaderView = (TextView)view.findViewById(R.id.event_header);
        eventImageView = (ImageView)view.findViewById(R.id.event_image);

        scores[0] = (ProgressBar)view.findViewById(R.id.studenci_score);
        scores[1] = (ProgressBar)view.findViewById(R.id.profesorowie_score);
        scores[2] = (ProgressBar)view.findViewById(R.id.doktorzy_score);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateView();

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("TURN ON");
        sensorManager.registerListener(this, linear, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("TURN OFF");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.values[0] > THRESHOLD && gameMeneger.acceptEvent()) {
            Snackbar.make(Objects.requireNonNull(getView()), R.string.s_accept, Snackbar.LENGTH_SHORT).show();
            updateView();
        }
        else if(sensorEvent.values[0] < -THRESHOLD && gameMeneger.rejectEvent()){
            Snackbar.make(Objects.requireNonNull(getView()), R.string.s_reject, Snackbar.LENGTH_SHORT).show();
            updateView();
        }
    }
    private void updateView(){
        eventHeaderView.setText(gameMeneger.getEventHeader());
        eventTextView.setText(gameMeneger.getEventText());
        eventQuestionView.setText(gameMeneger.getEventQuestion());
        eventImageView.setImageResource(getImageId("event"+gameMeneger.getEvent()));

        int[] points = gameMeneger.getPoints();
        for(int i = 0; i < 3; i++){
            scores[i].setProgress(points[i]);
        }
        if(gameMeneger.isFinished()){
            ((MainActivity) Objects.requireNonNull(getActivity())).changeScreen(MainActivity.Screen.END_SCREEN);
        }


    }
    private int getImageId(String aString) {
        String packageName = Objects.requireNonNull(getActivity()).getPackageName();
        return getResources().getIdentifier(aString, "drawable", packageName);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}