package com.muhibbin.expensenote.Features.NotesCRUD.CreateNote;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.Util.Config;


public class NoteCreateDialogFragment extends DialogFragment {

    private static NoteCreateListener noteCreateListener;

    private EditText nameEditText;
//    private EditText registrationEditText;
//    private EditText phoneEditText;
//    private EditText emailEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
//    private long registrationNumber = -1;
//    private String phoneString = "";
//    private String emailString = "";

    public NoteCreateDialogFragment() {
        // Required empty public constructor
    }

    public static NoteCreateDialogFragment newInstance(String title, NoteCreateListener listener){
        noteCreateListener = listener;
        NoteCreateDialogFragment noteCreateDialogFragment = new NoteCreateDialogFragment();


        noteCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return noteCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note_create_dialog, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);
        getDialog().getWindow().setBackgroundDrawable(inset);
        getDialog().setCancelable(false);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        nameEditText = view.findViewById(R.id.noteNameEditText);
//        registrationEditText = view.findViewById(R.id.registrationEditText);
//        phoneEditText = view.findViewById(R.id.phoneEditText);
//        emailEditText = view.findViewById(R.id.emailEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

//        String title = getArguments().getString(Config.TITLE);
//        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = nameEditText.getText().toString();
//                registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
//                phoneString = phoneEditText.getText().toString();
//                emailString = emailEditText.getText().toString();

                Note note = new Note(-1, nameString);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertNote(note);

                if(id>0){
                    note.setId(id);
                    noteCreateListener.onNoteCreated(note);
                    getDialog().dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
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

}
