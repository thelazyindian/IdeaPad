package com.jashasweejena.ideapad.activity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.appthemeengine.ATEActivity;
//import com.firebase.ui.auth.AuthUI;
//import com.firebase.ui.auth.IdpResponse;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.ajithvgiri.canvaslibrary.CanvasView;
import com.jashasweejena.ideapad.R;
import com.jashasweejena.ideapad.adapters.IdeaAdapter;
import com.jashasweejena.ideapad.adapters.RealmIdeaAdapter;
import com.jashasweejena.ideapad.app.Prefs;
import com.jashasweejena.ideapad.app.RecyclerTouchItemHelper;
import com.jashasweejena.ideapad.model.Idea;
import com.jashasweejena.ideapad.realm.RealmController;
import com.mrgames13.jimdo.splashscreen.App.SplashScreenBuilder;
import com.rm.freedrawview.FreeDrawView;
import com.rm.freedrawview.PathDrawnListener;
import com.rm.freedrawview.PathRedoUndoCountChangeListener;
import com.rm.freedrawview.ResizeBehaviour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends ATEActivity implements RecyclerTouchItemHelper.RecyclerTouchListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private IdeaAdapter recyclerViewAdapter;
    private Realm realm;
    private LayoutInflater layoutInflater;
    private RealmResults<Idea> listOfIdeas;
    private static int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        coordinatorLayout = findViewById(R.id.coordinatorlayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = RealmController.with().getRealm();

        setUpRecycler();

        listOfIdeas = RealmController.getInstance().getAllBooks();

        setRealmAdapter(listOfIdeas);

        handleIntent();

//        firebaseStart();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabFunction(null);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabFunction(null);
            }
        });

    }


    //Helps to initialise RealmIdeaAdapter instance which in turn extends RealmModelAdapter
    // which extends RealmBase Adapter. Also, we are setting the RealmIdeaAdapter instance
    // to recyclerViewAdapter
    public void setRealmAdapter(RealmResults<Idea> listOfIdeas) {

        RealmIdeaAdapter realmAdapter = new RealmIdeaAdapter(this, listOfIdeas, true);
        //Join the RecyclerView Adapter and the realmAdapter
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

        runAnimation(recyclerView, 0);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void runAnimation(RecyclerView recyclerView, int type) {

        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;

        switch (type) {
            case 0: //Fall down animation
                controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        }

        if (controller != null) {

            recyclerView.setLayoutAnimation(controller);
            recyclerView.scheduleLayoutAnimation();

        }

    }

    public void setRealmData() {

        ArrayList<Idea> ideaList = new ArrayList<>();

        Idea dummyIdea1 = new Idea();
        dummyIdea1.setId(new Random().nextInt());
        dummyIdea1.setTag("App");
        dummyIdea1.setName("IdeaPad");
        dummyIdea1.setDesc("xyz");
        ideaList.add(dummyIdea1);

        Idea dummyIdea2 = new Idea();
        dummyIdea2.setId(new Random().nextInt());
        dummyIdea2 = new Idea();
        dummyIdea2.setTag("Web");
        dummyIdea2.setName("NetControl");
        dummyIdea1.setDesc("xyz");
        ideaList.add(dummyIdea2);

        Idea dummyIdea3 = new Idea();
        dummyIdea3.setId(new Random().nextInt());
        dummyIdea3 = new Idea();
        dummyIdea3.setTag("Dummy Idea");
        dummyIdea3.setName("Dumb");
        dummyIdea1.setDesc("xyz");
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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int deletedPosition) {
        if (viewHolder instanceof IdeaAdapter.IdeaViewHolder) {

            //Store the object to be हलाल so that you can resurrect it back, if you want.
            final Idea deletedIdea = RealmController.getInstance().getAllBooks().get(deletedPosition);

            //Store the fields of current idea object which
            //is gonna be deleted and then while restoring it
            //create a new Realm object and set these fields
            //to new object and pass the new object to restoreItem.
            final String deletedName = deletedIdea.getName();
            final String deletedTag = deletedIdea.getTag();
            final String deletedDesc = deletedIdea.getDesc();
            final Long deletedId = deletedIdea.getId();

            Log.d(TAG, "onSwiped: " + "Adapter position before deletion " + deletedPosition);
            Log.d(TAG, "onSwiped: " + "Name of Item before deletion " + deletedName);

            recyclerViewAdapter.removeItem(deletedPosition);

            if (RealmController.getInstance().getAllBooks().size() == 0) {

                Prefs.with(getApplicationContext()).setPreLoad(false);

            }

            //As item is removed, show SnackBar

            Snackbar snackbar = Snackbar.make(coordinatorLayout, deletedName + " Removed from cart", Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Create new Idea object with fields of old object and use it instead.
                    Idea newIdea = new Idea();
                    newIdea.setId(deletedId);
                    newIdea.setTag(deletedTag);
                    newIdea.setName(deletedName);
                    newIdea.setDesc(deletedDesc);

                    //Restore the deleted item.
                    recyclerViewAdapter.restoreItem(newIdea);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }


    }
//    private void firebaseStart(){
//
//
//        // Choose authentication providers
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.GoogleBuilder().build());
//
//// Create and launch sign-in intent
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN);
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//            Log.d(TAG, "onActivityResult: " + response.getEmail());
//
//            if (resultCode == RESULT_OK) {
//                // Successfully signed in
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Log.d(TAG, "onActivityResult: " + user.getEmail());
//                // ...
//            } else {
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                // ...
//                Log.d(TAG, "onActivityResult: " + "Login failed!!!!!!!!!fa");
//
//            }
//        }
//    }


    private void fabFunction(@Nullable final String desc) {

        layoutInflater = MainActivity.this.getLayoutInflater();
        final View content = layoutInflater.inflate(R.layout.edit_idea, null, false);

        final EditText editName = content.findViewById(R.id.editName);
        final EditText editTag = content.findViewById(R.id.editTag);
        final EditText editDesc = content.findViewById(R.id.editDesc);

        final String name = editName.getText().toString();
        final String tag = editTag.getText().toString();


//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Mann mei ladoo phoota?")
//                .setView(content)
//                .setPositiveButton(android.R.string.ok, null)
//                .setNegativeButton(android.R.string.cancel, null)
//                .setNeutralButton(R.string.title_draw, null);



        // get the center for the clipping circle

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Mann mei ladoo phoota?")
                .setView(content)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.title_draw, null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create a new Idea instance which will store information
                //regarding the idea in respective fields and go into
                //the realm database

                if (desc == null) {

                    if (editName.getText() == null || name.equals("") || name.equals(" ")) {
                        editName.requestFocus();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Name field cannot be empty!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {

                        final String desc = editDesc.getText().toString();

                        addIdeaToRealm(name, tag, desc);
                        dialog.dismiss();
                    }
                }

                else {

                    if (editName.getText() == null || editName.getText().toString().equals("") || editName.getText().toString().equals(" ")) {
                        editName.requestFocus();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Name field cannot be empty!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {

                        String name = editName.getText().toString();
                        String tag = editTag.getText().toString();

                        addIdeaToRealm(name, tag, desc);
                        dialog.dismiss();

                    }
                }

                dialog.dismiss();
            }


        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CanvasActivity.class);
                startActivity(intent);
            }
        });

        final View view = dialog.getWindow().getDecorView();

        view.post(new Runnable() {
            @Override
            public void run() {
                final int centerX = view.getWidth() / 2;
                final int centerY = view.getHeight() / 2;
                // TODO Get startRadius from FAB
                // TODO Also translate animate FAB to center of screen?
                float startRadius = 20;
                float endRadius = view.getHeight();
                Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
                animator.setDuration(500);
                animator.start();
            }
        });

        dialog.show();
    }

    private void handleIntent(){

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if(Intent.ACTION_SEND.equals(action) && type.equals("text/plain")) {
            String sentText = intent.getStringExtra(Intent.EXTRA_TEXT);

            if (sentText != null) {
                fabFunction(sentText);
            }
        }
    }

    private void addIdeaToRealm(String name, String tag, String desc){
        realm.beginTransaction();

        Idea idea = new Idea();
        idea.setId(System.currentTimeMillis() + RealmController.getInstance().getAllBooks().size() + 1);
        idea.setName(name);
        idea.setTag(tag);
        idea.setDesc(desc);

        realm.copyToRealm(idea);
        realm.commitTransaction();
    }



}


