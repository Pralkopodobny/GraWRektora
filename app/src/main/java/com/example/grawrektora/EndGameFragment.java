package com.example.grawrektora;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndGameFragment extends Fragment {
    private GameOfRektorMeneger gameMeneger;
    private TextView statsText, endingText;

    public EndGameFragment() {
        // Required empty public constructor
    }

    public static EndGameFragment newInstance(String param1, String param2) {
        return new EndGameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameMeneger = GameOfRektorMeneger.getInstance(getContext());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s, container, false);
        statsText = (TextView)view.findViewById(R.id.stats_text);
        endingText = (TextView)view.findViewById(R.id.ending_text);
        Button button= (Button)view.findViewById(R.id.restart_game_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                restartGame(view);
            }
        });
        statsText.setText("Ustanowiłeś " + gameMeneger.getTurn() + (gameMeneger.getTurn()==1?" Przepis":" Przepisów"));
        switch (gameMeneger.getEnding()){
            case BAD_ENDING:{
                endingText.setText(R.string.bad_ending);
                break;
            }
            case NORMAL_ENDING:{
                endingText.setText(R.string.normal_ending);
                break;
            }
            case GOOD_ENDING:{
                endingText.setText(R.string.great_ending);
                break;
            }
        }
        return view;
    }

    public void restartGame(View v){
        gameMeneger.startNewGame();
        ((MainActivity) Objects.requireNonNull(getActivity())).changeScreen(MainActivity.Screen.GAME_SCREEN);

    }
}