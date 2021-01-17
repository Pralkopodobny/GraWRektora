package com.example.grawrektora;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public final class GameOfRektorMeneger {
    private static final int EVENTS_NUMBER = 10;
    private static final int MIN_POINTS = 0;
    private static final int MAX_POINTS = 100;
    private static final int NORMAL_ENDING_THRESH = 5;
    private static final int GOOD_ENDING_THRESH = 10;
    public enum Ending {
        BAD_ENDING,
        NORMAL_ENDING,
        GOOD_ENDING;
    }
    private static GameOfRektorMeneger instance;

    Context context;

    private Random rng;
    private int[] points;
    private String[] eventTexts;
    private String[] eventQuestions;
    private String[] eventHeaders;
    private ArrayList<int[]> acceptedValues;
    private ArrayList<int[]> rejectedValues;
    private int turn;
    private int eventNumber;
    private int lastEvent = -1;
    private boolean locked;
    private Timer lockTimer;
    private boolean gameFinished;

    private GameOfRektorMeneger(Context context){
        this.context = context;
        rng = new Random();
        acceptedValues = new ArrayList<>();
        rejectedValues = new ArrayList<>();
        lockTimer = new Timer();
        points = new int[3];
        eventNumber = 0;
        locked = false;
        gameFinished = false;

        eventTexts = context.getResources().getStringArray(R.array.game_of_rektor_event_texts);
        eventQuestions = context.getResources().getStringArray(R.array.game_of_rektor_event_questions);
        eventHeaders = context.getResources().getStringArray(R.array.game_of_rektor_headers);
        readValues(context.getResources().getStringArray(R.array.game_of_rektor_event_accepted_values), acceptedValues);
        readValues(context.getResources().getStringArray(R.array.game_of_rektor_event_rejected_values), rejectedValues);

        startNewGame();
    }
    public static GameOfRektorMeneger getInstance(Context context){
        if(instance == null){
            instance = new GameOfRektorMeneger(context);
        }
        return instance;
    }

    private void readValues(String[] resources, ArrayList<int[]> valuesContainer){
        for (String val: resources) {
            String[] tempStrings = val.split(" ");
            int[] tempInts = new int[3];
            for (int i = 0; i < 3; i++) {
                tempInts[i] = Integer.parseInt(tempStrings[i]);
            }
            valuesContainer.add(tempInts);
        }
    }

    public int[] getPoints(){
        return points;
    }

    public int getTurn(){
        return turn;
    }

    public int getEvent(){
        return eventNumber;
    }

    private void lock(){
        locked = true;
        lockTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                locked = false;
                System.out.println("UNLOCK");
            }
        }, 2000);
    }

    public String getEventText(){
        return eventTexts[eventNumber];
    }

    public String getEventQuestion(){
        return eventQuestions[eventNumber];
    }

    public String getEventHeader(){
        return eventHeaders[eventNumber];
    }

    public void startNewGame(){
        for(int i = 0; i < 3; i++){
            points[i] = 50;
        }
        turn = 0;
        eventNumber = rng.nextInt(EVENTS_NUMBER);
        lastEvent = eventNumber;
        gameFinished = false;
    }
    public boolean acceptEvent(){
        return nextTurn(acceptedValues.get(eventNumber));
    }

    public boolean rejectEvent(){
        return nextTurn(rejectedValues.get(eventNumber));
    }

    private boolean nextTurn(int[] addedPoints) {
        if(locked) return false;
        for(int i = 0; i < 3; i++){
            points[i] += addedPoints[i];
        }
        /*
        for (int point : points) {
            if (point < 0 || point > 100)
                return true;
        }*/
        turn++;
        int tempEventNumber=eventNumber;
        do{
            eventNumber = rng.nextInt(EVENTS_NUMBER);
        }while (lastEvent == eventNumber);
        lastEvent=tempEventNumber;
        lock();
        for (int pointsOfGroup:points) {
            if(pointsOfGroup > MAX_POINTS || pointsOfGroup < MIN_POINTS){
                gameFinished = true;
                break;
            }
        }
        return true;
    }
    public boolean isFinished(){
        return  gameFinished;
    }
    public Ending getEnding(){
        if(turn < NORMAL_ENDING_THRESH) return Ending.BAD_ENDING;
        if (turn < GOOD_ENDING_THRESH) return Ending.NORMAL_ENDING;
        return Ending.GOOD_ENDING;
    }
}
