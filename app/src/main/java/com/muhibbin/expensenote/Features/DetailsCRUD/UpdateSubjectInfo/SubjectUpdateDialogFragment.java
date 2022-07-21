package com.muhibbin.expensenote.Features.DetailsCRUD.UpdateSubjectInfo;


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
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.muhibbin.expensenote.Database.DatabaseQueryClass;
import com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails.DetailNote;
import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.Util.Config;

public class SubjectUpdateDialogFragment extends DialogFragment {

    private EditText subjectNameEditText;
//    private EditText subjectCodeEditText;
    private EditText subjectCreditEditText;
    private Button updateButton;
    private Button cancelButton;

    private static SubjectUpdateListener subjectUpdateListener;
    private static long subjectId;
    private static int position;

    private DatabaseQueryClass databaseQueryClass;

    public SubjectUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static SubjectUpdateDialogFragment newInstance(long subId, int pos, SubjectUpdateListener listener){
        subjectId = subId;
        position = pos;
        subjectUpdateListener = listener;

        SubjectUpdateDialogFragment subjectUpdateDialogFragment = new SubjectUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update subject information");
        subjectUpdateDialogFragment.setArguments(args);

        subjectUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return subjectUpdateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_update_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);
        getDialog().getWindow().setBackgroundDrawable(inset);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        subjectNameEditText = view.findViewById(R.id.subjectNameEditText);
//        subjectCodeEditText = view.findViewById(R.id.subjectCodeEditText);
        subjectCreditEditText = view.findViewById(R.id.subjectCreditEditText);
        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        DetailNote detailNote = databaseQueryClass.getDetailById(subjectId);

        subjectNameEditText.setText(detailNote.getName());
//        subjectCodeEditText.setText(String.valueOf(subject.getCode()));
        subjectCreditEditText.setText(String.valueOf(detailNote.getPrice()));

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String subjectName = subjectNameEditText.getText().toString();
                    int activated = detailNote.getActivated();
                    double subjectCredit = Double.parseDouble(subjectCreditEditText.getText().toString());

                    DetailNote detailNote = new DetailNote(subjectId, subjectName, subjectCredit, activated);

                    long rowCount = databaseQueryClass.updateSubjectInfo(detailNote);

                    if(rowCount>0){
                        subjectUpdateListener.onSubjectInfoUpdate(detailNote, position);
                        getDialog().dismiss();
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
