package com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.DetailsCRUD.ShowDetailNoteList.DetailNoteListActivity;
import com.muhibbin.expensenote.R;

import java.util.ArrayList;

public class DetailNoteCreateSpeechDialogFragment extends DialogFragment implements RecognitionListener {
    private static long noteIdNumber;
    private static DetailNoteCreateListener detailNoteCreateListener;

//    private TextView subjectNameEditText;
    //    private EditText subjectCodeEditText;
//    private TextView subjectCreditEditText;

    private Button tryButton;
    private Button lnButton;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    int focused = 0;
    private String LOG_TAG = "VoiceRecognitionActivity";

    private LottieAnimationView animationView;
    private TextView focused_et;

    public DetailNoteCreateSpeechDialogFragment() {
        // Required empty public constructor
    }

    public static DetailNoteCreateSpeechDialogFragment newInstance(long noteRegNo, DetailNoteCreateListener listener){
        noteIdNumber = noteRegNo;
        detailNoteCreateListener = listener;

        DetailNoteCreateSpeechDialogFragment detailNoteCreateSpeechDialogFragment = new DetailNoteCreateSpeechDialogFragment();

        detailNoteCreateSpeechDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return detailNoteCreateSpeechDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speech_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);
        getDialog().getWindow().setBackgroundDrawable(inset);
        getDialog().setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

//        subjectNameEditText = view.findViewById(R.id.subjectNameEditText);
//        subjectCodeEditText = view.findViewById(R.id.subjectCodeEditText);
//        subjectCreditEditText = view.findViewById(R.id.subjectCreditEditText);
        tryButton = view.findViewById(R.id.tryButton);
        lnButton = view.findViewById(R.id.lnButton);
        animationView = view.findViewById(R.id.animationView);
        focused_et = view.findViewById(R.id.focused_et);

        speech = SpeechRecognizer.createSpeechRecognizer(getContext());
        speech.setRecognitionListener(this);
        setBangla();
        animationView.playAnimation();
        speech.startListening(recognizerIntent);

        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focused_et.setText("Listenting");
                tryButton.setVisibility(View.INVISIBLE);
                animationView.playAnimation();
                speech.startListening(recognizerIntent);

            }
        });
        lnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speech.stopListening();
                final CharSequence[] items = {"English", "Bangla"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Select a language");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                setEnglish();
                                focused_et.setText("Listenting");
                                tryButton.setVisibility(View.INVISIBLE);
                                animationView.playAnimation();
                                speech.startListening(recognizerIntent);
                                break;
                            case 1:
                                setBangla();
                                focused_et.setText("Listenting");
                                tryButton.setVisibility(View.INVISIBLE);
                                animationView.playAnimation();
                                speech.startListening(recognizerIntent);
                                break;

                        }
                    }
                });
                builder.show();
            }
        });

//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    String subjectName = subjectNameEditText.getText().toString();
////                int activated = Integer.parseInt(subjectCodeEditText.getText().toString());
//                    double subjectCredit = Double.parseDouble(subjectCreditEditText.getText().toString());
//                    Log.d("TAGa", "onClick: "+subjectCredit);
//                    DetailNote detailNote = new DetailNote(-1, subjectName, subjectCredit, 1);
//
//                    DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());
//
//                    long id = databaseQueryClass.insertDetail(detailNote, noteIdNumber);
//
//                    if(id>0){
//                        detailNote.setId(id);
//                        detailNoteCreateListener.onSubjectCreated(detailNote);
//                        getDialog().dismiss();
//                        ((DetailNoteListActivity)getActivity()).setTotalCostText();
//
//                    }
//
//                }catch (Exception e){
//                    Toast.makeText(getContext(), "Enter a valid price", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getDialog().dismiss();
//            }
//        });

        return view;
    }

    private void setEnglish() {
        lnButton.setText("EN");
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getContext().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Voice recognition!");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, "en");
    }
    private void setBangla() {
        lnButton.setText("BN");
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"bn-BD");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getContext().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Voice recognition!");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "bn-BD");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "bn-BD");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, "bn-BD");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,"bn-BD");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "bn-BD");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, "bn-BD");
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
//        progressBar.setIndeterminate(false);
//        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        animationView.pauseAnimation();
//        focused_et.setText(getString(R.string.speak_text));
//        tryButton.setVisibility(View.VISIBLE);
//        progressBar.setIndeterminate(true);
//        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        tryButton.setVisibility(View.VISIBLE);
        animationView.pauseAnimation();
        focused_et.setText(errorMessage);
//        Toast.makeText(getContext(), ""+errorMessage, Toast.LENGTH_SHORT).show();

//        returnedText.setText(errorMessage);
//        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults"+arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result;

        focused_et.setText(text);

        DetailNote detailNote = new DetailNote(-1, text, 0, 0);

        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

        long id = databaseQueryClass.insertDetail(detailNote, noteIdNumber);

        if(id>0){
            detailNote.setId(id);
            detailNoteCreateListener.onSubjectCreated(detailNote);
//            getDialog().dismiss();
//            ((DetailNoteListActivity)getActivity()).setTotalCostText();

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getDialog().dismiss();
            }
        }, 500);


//        if(focused==0){
//            subjectNameEditText.setText(text);
//        }
//        else {
//            subjectCreditEditText.setText(text);
//        }

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
//        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

}
