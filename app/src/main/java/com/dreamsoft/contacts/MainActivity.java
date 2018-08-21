package com.dreamsoft.contacts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.dreamsoft.contacts.R;
import com.dreamsoft.contacts.DatabaseHelper;
import com.dreamsoft.contacts.Contact;
import com.dreamsoft.contacts.MyDividerItemDecoration;
import com.dreamsoft.contacts.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    private ContactsAdapter mAdapter;
    private List<Contact> contactsList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noContactsView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noContactsView = findViewById(R.id.empty_contacts_view);

        db = new DatabaseHelper(this);

        contactsList.addAll(db.getAllContacts());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactDialog(false, null, -1);
            }
        });

        mAdapter = new ContactsAdapter(this, contactsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    /**
     * Inserting new note in db
     * and refreshing the list
     */
    private void createContact(String name,String number, String email, String favourite) {

        long id = db.insertContact(name,number,email,favourite);

        // get the newly inserted note from db
        Contact n = db.getContact(id);

        if (n != null) {
            // adding new note to array list at 0 position
            contactsList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateContact(String name,String number, String email, String favourite, int position) {
        Contact n = contactsList.get(position);

        n.setName(name);
        n.setNumber(number);
        n.setEmail(email);
        n.setFavourite(favourite);


        db.updateContact(n);

        // refreshing the list
        contactsList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteContact(int position) {

        db.deleteContact(contactsList.get(position));

        contactsList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showContactDialog(true, contactsList.get(position), position);
                } else {
                    deleteContact(position);
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showContactDialog(final boolean shouldUpdate, final Contact contact, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.contact_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.name);
        final EditText inputNumber = view.findViewById(R.id.number);
        final EditText inputEmail = view.findViewById(R.id.email);
        final CheckBox inputFavourite = view.findViewById(R.id.favourite);


        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_contact_title) : getString(R.string.lbl_edit_contact_title));

        if (shouldUpdate && contact != null) {
            inputName.setText(contact.getName());
            inputNumber.setText(contact.getNumber());
            inputEmail.setText(contact.getEmail());

            inputFavourite.setText(contact.getFavourite());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNumber.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter number!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                String fav = "";
                if(inputFavourite.isChecked())
                    fav = "Favourite";
                // check if user updating note
                if (shouldUpdate && contact != null) {
                    // update contact by it's id
                    updateContact(inputName.getText().toString(),inputNumber.getText().toString(),inputEmail.getText().toString(),fav, position);
                } else {
                    // create new contact
                    createContact(inputName.getText().toString(),inputNumber.getText().toString(),inputEmail.getText().toString(),fav);
                }
            }
        });
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getContactsCount() > 0) {
            noContactsView.setVisibility(View.GONE);
        } else {
            noContactsView.setVisibility(View.VISIBLE);
        }
    }
}