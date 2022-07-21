package com.muhibbin.expensenote.ShareNote;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muhibbin.expensenote.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView registrationNumTextView;
    TextView emailTextView;
    TextView phoneTextView;
    TextView progress_text;
    ImageView crossButtonImageView;
    ImageView editButtonImageView;
    ProgressBar simpleProgressBar;

    public CustomViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.nameTextView);
        registrationNumTextView = itemView.findViewById(R.id.registrationNumTextView);
//        emailTextView = itemView.findViewById(R.id.emailTextView);
//        phoneTextView = itemView.findViewById(R.id.phoneTextView);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
        simpleProgressBar =  itemView.findViewById(R.id.progressBar);
        progress_text =  itemView.findViewById(R.id.progress_text);
    }
}
