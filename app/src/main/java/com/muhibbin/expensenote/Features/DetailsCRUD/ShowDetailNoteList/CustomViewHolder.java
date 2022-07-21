package com.muhibbin.expensenote.Features.DetailsCRUD.ShowDetailNoteList;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muhibbin.expensenote.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView detailsNoteNameTV;
    TextView priceTv;
    CheckBox subjectNameCheckBox;
//    ImageView editButtonImageView;
    LinearLayout priceLL;

    public CustomViewHolder(View itemView) {
        super(itemView);

        detailsNoteNameTV = itemView.findViewById(R.id.subjectNameTextView);

        priceTv = itemView.findViewById(R.id.subjectCreditTextView);
        subjectNameCheckBox = itemView.findViewById(R.id.subjectNameCheckBox);
        priceLL = itemView.findViewById(R.id.priceLL);
//        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
