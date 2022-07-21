package com.muhibbin.expensenote.ShareNote;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.l4digital.fastscroll.FastScroller;

import com.muhibbin.expensenote.R;
import com.muhibbin.expensenote.model.Contact;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> implements FastScroller.SectionIndexer {


    private Context context;
    private List<Contact> contactList;
    private OnItemClickListener mListener;

    public ContactRecyclerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
//        Upload uploadCurrent = mUploads.get(position);
//        Glide.with(mContext)
//                .load(uploadCurrent.getmImageUrl())
//                .placeholder(R.drawable.default_image_thumbnail)
//                .fitCenter()
//                .centerCrop()
//                .into(holder.imageView);
//        holder.textViewName.setText(String.valueOf(uploadCurrent.getLikesCount()));
        final int itemPosition = position;
        final Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public CharSequence getSectionText(int position) {
        String section = String.valueOf(contactList.get(position).getName().charAt(0)).toUpperCase();
        return section;
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView phoneTextView;

        ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
//            mListener.onItemClick(position);
            if (mListener != null) {
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(contactList.get(position).getPhoneNumber());
                }
            }

        }

    }

    public interface OnItemClickListener {
        void onItemClick(String number);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

//    private Context context;
//    private List<Contact> contactList;
//
//    public ContactRecyclerViewAdapter(Context context, List<Contact> contactList) {
//        this.context = context;
//        this.contactList = contactList;
//    }
//
//    @Override
//    public com.muhibbin.expensenote.ShareNote.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
//        return new com.muhibbin.expensenote.ShareNote.ContactViewHolder(view);
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindViewHolder(CustomViewHolder holder, int position) {
//        final int itemPosition = position;
//        final Contact contact = contactList.get(position);
//        holder.nameTextView.setText(contact.getName());
//        holder.phoneTextView.setText(contact.getPhoneNumber());
//
//        holder.itemView.setOnClickListener(this);
//
////
////        holder.itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(context, ""+itemPosition, Toast.LENGTH_SHORT).show();
////
////            }
////        });
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return contactList.size();
//    }
//
//    @Override
//    public CharSequence getSectionText(int position) {
//        String section = String.valueOf(contactList.get(position).getName().charAt(0)).toUpperCase();
//        return section;
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        int position = getAdapterPosition();
//        if (mListener != null) {
//            if (position != RecyclerView.NO_POSITION) {
//                mListener.onItemClick(position);
//            }
//        }
//    }
}
