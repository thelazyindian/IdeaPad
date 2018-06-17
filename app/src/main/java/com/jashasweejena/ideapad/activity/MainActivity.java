package com.jashasweejena.ideapad.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jashasweejena.ideapad.R;
import com.jashasweejena.ideapad.adapters.IdeaAdapter;
import com.jashasweejena.ideapad.adapters.RealmIdeaAdapter;
import com.jashasweejena.ideapad.app.Prefs;
import com.jashasweejena.ideapad.app.RecyclerTouchItemHelper;
import com.jashasweejena.ideapad.model.Idea;
import com.jashasweejena.ideapad.realm.RealmController;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RecyclerTouchItemHelper.RecyclerTouchListener{

    private com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton fab;
    private final static String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private IdeaAdapter recyclerViewAdapter;
    private Realm realm;
    private LayoutInflater layoutInflater;
    private RealmResults<Idea> listOfIdeas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        coordinatorLayout = findViewById(R.id.coordinatorlayout);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = RealmController.with().getRealm();


        recyclerView = findViewById(R.id.recycler);


        setUpRecycler();

//        if(!Prefs.with(this).getPreLoad()) {
//            setRealmData();
//        }

//        setRealmData();

        listOfIdeas = RealmController.getInstance().getAllBooks();

        setRealmAdapter(listOfIdeas);

        fab = (com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton) (findViewById(R.id.fab));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutInflater = MainActivity.this.getLayoutInflater();
                final View content = layoutInflater.inflate(R.layout.edit_idea, null, false);

                final EditText editName = content.findViewById(R.id.editName);
                final EditText editTag = content.findViewById(R.id.editTag);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Mann mei ladoo phoota?")
                        .setView(content)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                //Create a new Idea instance which will store information
                                //regarding the idea in respective fields and go into
                                //the realm database


                                if (editName.getText() == null || editName.getText().toString().equals("") || editName.getText().toString().equals(" ")) {
                                    Toast.makeText(MainActivity.this, "Name field cannot be left blank!", Toast.LENGTH_SHORT).show();
                                } else {

                                    realm.beginTransaction();

                                    Idea idea = new Idea();
                                    idea.setId(System.currentTimeMillis() + RealmController.getInstance().getAllBooks().size() + 1);
                                    idea.setName(editName.getText().toString());
                                    idea.setTag(editTag.getText().toString());

                                    realm.copyToRealm(idea);
                                    realm.commitTransaction();

                                }


                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    //Helps to initialise RealmIdeaAdapter instance which in turn extends RealmModelAdapter
    // which extends RealmBase Adapter. Also, we are setting the RealmIdeaAdapter instance
    // to recyclerViewAdapter
    public void setRealmAdapter(RealmResults<Idea> listOfIdeas) {

        RealmIdeaAdapter realmAdapter = new RealmIdeaAdapter(this, listOfIdeas, true);
        //Join the RecyclerView Adapter and the realmAda                                                      pter
        recyclerViewAdapter.setRealmBaseAdapter(realmAdapter);

        //Redraw the RecyclerView layout
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void setUpRecycler() {


        //Assign ItemTouchHelper to RecyclerView.
        ItemTouchHelper.SimpleCallback itemTouchHelper = new RecyclerTouchItemHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

        //Set up Vertical LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerViewAdapter = new IdeaAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void setRealmData() {

        ArrayList<Idea> ideaList = new ArrayList<>();

        Idea dummyIdea1 = new Idea();
        dummyIdea1.setId(new Random().nextInt());
        dummyIdea1.setTag("App");
        dummyIdea1.setName("IdeaPad");
        ideaList.add(dummyIdea1);

        Idea dummyIdea2 = new Idea();
        dummyIdea2.setId(new Random().nextInt());
        dummyIdea2 = new Idea();
        dummyIdea2.setTag("Web");
        dummyIdea2.setName("NetControl");
        ideaList.add(dummyIdea2);

        Idea dummyIdea3 = new Idea();
        dummyIdea3.setId(new Random().nextInt());
        dummyIdea3 = new Idea();
        dummyIdea3.setTag("Dummy Idea");
        dummyIdea3.setName("Dumb");
        ideaList.add(dummyIdea3);

        for (Idea item : ideaList) {

            //Copy all the dummy ideas to realm

            //Everything happens with a Realm transaction
            realm.beginTransaction();
            realm.copyToRealm(item);
            realm.commitTransaction();

        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof IdeaAdapter.IdeaViewHolder) {

            //Store the object to be हलाल so that you can resurrect it back, if you want.
            final Idea deletedIdea = RealmController.getInstance().getAllBooks().get(viewHolder.getAdapterPosition());
            final int deletedPosition = viewHolder.getAdapterPosition();
            String deletedName = deletedIdea.getName();

            Log.d(TAG, "onSwiped: " + "Adapter position before deletion " + deletedPosition);
            Log.d(TAG, "onSwiped: " + "Name of Item before deletion " + deletedName);

            recyclerViewAdapter.removeItem(deletedPosition);

            //As item is not removed, show SnackBar

//            Snackbar snackbar = Snackbar.make(coordinatorLayout, deletedName + " Removed from cart", Snackbar.LENGTH_SHORT);
//            snackbar.setAction("UNDO", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    //Restore the deleted item.
//                    recyclerViewAdapter.restoreItem(deletedIdea, deletedPosition);
//
//                }
//            });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();

        }
    }
}
