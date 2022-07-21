package com.muhibbin.expensenote.Features.NotesCRUD.UpdateNoteInfo;

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
import com.muhibbin.expensenote.Features.NotesCRUD.CreateNote.Note;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.Util.Config;


public class NoteUpdateDialogFragment extends DialogFragment {

    private static long noteRegNo;
    private static int noteItemPosition;
    private static NoteUpdateListener noteUpdateListener;

    private Note mNote;

    private EditText nameEditText;
//    private EditText registrationEditText;
//    private EditText phoneEditText;
//    private EditText emailEditText;
    private Button updateButton;
    private Button cancelButton;

    private String nameString = "";
//    private long registrationNumber = -1;
//    private String phoneString = "";
//    private String emailString = "";

    private DatabaseQueryClass databaseQueryClass;

    public NoteUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static NoteUpdateDialogFragment newInstance(long registrationNumber, int position, NoteUpdateListener listener){
        noteRegNo = registrationNumber;
        noteItemPosition = position;
        noteUpdateListener = listener;
        NoteUpdateDialogFragment noteUpdateDialogFragment = new NoteUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update note information");
        noteUpdateDialogFragment.setArguments(args);

        noteUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return noteUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note_update_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);
        getDialog().getWindow().setBackgroundDrawable(inset);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);


        databaseQueryClass = new DatabaseQueryClass(getContext());

        nameEditText = view.findViewById(R.id.noteNameEditText);
//        registrationEditText = view.findViewById(R.id.registrationEditText);
//        phoneEditText = view.findViewById(R.id.phoneEditText);
//        emailEditText = view.findViewById(R.id.emailEditText);
        updateButton = view.findViewById(R.id.updateNoteInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mNote = databaseQueryClass.getNoteByRegNum(noteRegNo);

        if(mNote !=null){
            nameEditText.setText(mNote.getName());
//            registrationEditText.setText(String.valueOf(mNote.getRegistrationNumber()));
//            phoneEditText.setText(mNote.getPhoneNumber());
//            emailEditText.setText(mNote.getEmail());

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = nameEditText.getText().toString();
//                    registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
//                    phoneString = phoneEditText.getText().toString();
//                    emailString = emailEditText.getText().toString();

                    mNote.setName(nameString);
//                    mNote.setRegistrationNumber(registrationNumber);
//                    mNote.setPhoneNumber(phoneString);
//                    mNote.setEmail(emailString);

                    long id = databaseQueryClass.updateNoteInfo(mNote);

                    if(id>0){
                        noteUpdateListener.onNoteInfoUpdated(mNote, noteItemPosition);
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

        }

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
