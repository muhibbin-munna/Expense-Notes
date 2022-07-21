package com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.DetailsCRUD.ShowDetailNoteList.DetailNoteListActivity;
import com.muhibbin.expensenote.R;


public class DetailNoteCreateDialogFragment extends DialogFragment {

    private static long noteIdNumber;
    private static DetailNoteCreateListener detailNoteCreateListener;

    private EditText subjectNameEditText;
//    private EditText subjectCodeEditText;
    private EditText subjectCreditEditText;

    private Button createButton;
    private Button cancelButton;

    public DetailNoteCreateDialogFragment() {
        // Required empty public constructor
    }

    public static DetailNoteCreateDialogFragment newInstance(long noteRegNo, DetailNoteCreateListener listener){
        noteIdNumber = noteRegNo;
        detailNoteCreateListener = listener;

        DetailNoteCreateDialogFragment detailNoteCreateDialogFragment = new DetailNoteCreateDialogFragment();

        detailNoteCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return detailNoteCreateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_create_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);
        getDialog().getWindow().setBackgroundDrawable(inset);
        getDialog().setCancelable(false);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        subjectNameEditText = view.findViewById(R.id.subjectNameEditText);
//        subjectCodeEditText = view.findViewById(R.id.subjectCodeEditText);
        subjectCreditEditText = view.findViewById(R.id.subjectCreditEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String subjectName = subjectNameEditText.getText().toString();
//                int activated = Integer.parseInt(subjectCodeEditText.getText().toString());
                    double subjectCredit = 0;
                    if(!subjectCreditEditText.getText().toString().isEmpty()){
                        subjectCredit = Double.parseDouble(subjectCreditEditText.getText().toString());

                    }
                    DetailNote detailNote = new DetailNote(-1, subjectName, subjectCredit, 0);

                    DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                    long id = databaseQueryClass.insertDetail(detailNote, noteIdNumber);

                    if(id>0){
                        detailNote.setId(id);
                        detailNoteCreateListener.onSubjectCreated(detailNote);
                        getDialog().dismiss();
//                        ((DetailNoteListActivity)getActivity()).setTotalCostText();

                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "Enter a valid price", Toast.LENGTH_SHORT).show();
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
