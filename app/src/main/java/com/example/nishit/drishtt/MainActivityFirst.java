package com.example.nishit.drishtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityFirst extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout myLayout =null;
    private TextToSpeech myTTS;
    private SpeechRecognizer mySpeechRecogizer;
    public boolean sayBonce=false;
    Button ReadIt;
    private String currentBattry;
    private BroadcastReceiver mBatInfoReciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);


                    currentBattry = String.valueOf(level + "%");


                if(sayBonce==false) {
                    speak("Your Current Battry Level is " + currentBattry);
                    sayBonce = true;
                }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);





        Button ReadIt=(Button)findViewById(R.id.readit);
        Button call_rs=(Button)findViewById(R.id.call_rs);
        Button messageSOS=(Button)findViewById(R.id.sosbutton);

        ReadIt.setOnClickListener(this);
        call_rs.setOnClickListener(this);
        messageSOS.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeTextToSpeech();
        initializeSpeechRecognizer();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecogizer.startListening(intent);
                Toast.makeText(getApplicationContext(),"I am Being Long Clicked",Toast.LENGTH_LONG).show();
                return true;
            }
        });


    }

    private void initializeSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(getApplicationContext()))
        {

            mySpeechRecogizer=SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecogizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results.get(0));

                }

                @Override
                public void onPartialResults(Bundle bundle) {


                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    private void processResult(String command) {
        command = command.toLowerCase();
        //what is your name
        //what is the time

        if(command.indexOf("what")!= -1)
        {
            if (command.indexOf("your name")!=-1)
            {
                speak("Hello! My name is Koko: I am happy to Help You.");
            }
            if (command.indexOf("time")!= -1)
            {
                Date now =new Date();
                String time = DateUtils.formatDateTime(this,now.getTime(),DateUtils.FORMAT_SHOW_TIME);
                speak("The Time is "+time);
            }
            if(command.indexOf("date")!= -1)
            {
                Calendar calendar=Calendar.getInstance();
                String currentDate= java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime());
                speak("Today is "+currentDate);
            }
            if(command.indexOf("battery")!= -1)
            {
                    this.registerReceiver(this.mBatInfoReciever, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                    sayBonce=false;
            }

        }
        else if(command.indexOf("help")!= -1)
        {
            if (command.indexOf("read")!=-1)
            {


                speak("Opening Your Reading Lense");
                Intent i=new Intent("com.example.nishit.drishtt.ReadForMe");
                startActivity(i);
            }
            if(command.indexOf("call")!=-1)
            {
                speak("Opening Binary Coded Keypad");
                Intent i1=new Intent("com.example.nishit.drishtt.Calling_RS");
                startActivity(i1);
            }
        }
        /*else if(command.indexOf("which") != -1)
        {
            if(command.indexOf("day") != -1)
            {
                Calendar calendar=Calendar.getInstance();
                String currentDate= java.text.DateFormat.getDateInstance(java.text.DateFormat.DAY_OF_WEEK_FIELD).format(calendar.getTime());
            }
            if(command.indexOf("year") != -1)
            {
                Calendar calendar=Calendar.getInstance();
                String currentDate= java.text.DateFormat.getDateInstance(java.text.DateFormat.YEAR_FIELD).format(calendar.getTime());
            }
        }*/
        else if(command.indexOf("how") != -1)
        {
            if(command.indexOf("are you") != -1)
            {
                    speak("Thankyou for asking, I'm very Much Well. Is there Anything i can do for you?");

            }
            if(command.indexOf("battery") != -1)
            {
                this.registerReceiver(this.mBatInfoReciever, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                sayBonce=false;
            }
        }
        else
        {
            speak("Sorry: I Didnt Get You.");
        }

    }

    private void initializeTextToSpeech() {
        myTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
              if(myTTS.getEngines().size()==0){
                  Toast.makeText(MainActivityFirst.this,"There is no TTS Engine on Ypur Device",Toast.LENGTH_LONG).show();
                  finish();
              }
              else
              {
                  myTTS.setLanguage(Locale.ENGLISH);
                  speak("Hello: How may i help you.");
              }
            }
        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT>=21){
            myTTS.speak(message,TextToSpeech.QUEUE_ADD,null,null);

        }
        else
        {
            myTTS.speak(message,TextToSpeech.QUEUE_ADD,null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }
    boolean twice=false;
    boolean once=true;
    @Override
    public void onBackPressed() { //GO TO MAIN PAGE ON BACK PRESSED
        if(twice==true)
        {
            speak("You're in the Main Menu");
            Toast.makeText(this, "You're in the Main Menu", Toast.LENGTH_SHORT).show();
        }
        //super.onBackPressed();
        twice =true;
        if(once==true) {
            speak("You're in the Main Menu");
            Toast.makeText(this, "You're in the Main Menu", Toast.LENGTH_SHORT).show();
            once=false;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice =false;

            }
        },3000);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.readit:
                Intent i=new Intent("com.example.nishit.drishtt.ReadForMe");
                startActivity(i);
                break;

            case R.id.fab:
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak Something");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySpeechRecogizer.startListening(intent);
                Toast.makeText(getApplicationContext(),"I am Being  Clicked",Toast.LENGTH_LONG).show();

                break;
            case R.id.call_rs:
                Intent i1=new Intent("com.example.nishit.drishtt.Calling_RS");
                startActivity(i1);
                break;



                /*
                case R.id.sosbutton:
                Intent i2=new Intent("com.example.nishit.drishtt.MessageSOS");
                startActivity(i2);
                break;
                */


        }

    }
}
