package com.dreamsoft.contacts;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dreamsoft.contacts.R;

/**
 * Created by Mizan on 5/21/2018.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private Context context;
    private List<Contact> contactsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView number;
        public TextView email;
        public TextView favourite;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            number =(TextView) view.findViewById(R.id.number);
            email =(TextView) view.findViewById(R.id.email);
            favourite = (TextView) view.findViewById(R.id.favourite);
        }
    }


    public ContactsAdapter(Context context, List<Contact> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact contact = contactsList.get(position);

        holder.name.setText(contact.getName());
        holder.number.setText(contact.getNumber());
        holder.email.setText(contact.getEmail());
        holder.favourite.setText(contact.getFavourite());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }


}