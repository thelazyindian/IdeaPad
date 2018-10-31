package com.jashasweejena.ideapad.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
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

public class MainActivity extends AppCompatActivity implements RecyclerTouchItemHelper.RecyclerTouchListener {

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
    FreeDrawView mSignatureView;
    private static int RC_SIGN_IN = 123;
    byte[] bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        coordinatorLayout = findViewById(R.id.coordinatorlayout);
        setSupportActionBar(toolbar);

        realm = RealmController.with().getRealm();

        setUpRecycler();

//        layoutInflater = this.getLayoutInflater();
//        final View content = layoutInflater.inflate(R.layout.canvas, null, false);
//
//        mSignatureView = content.findViewById(R.id.drawingxyz);

//        final View content2 = layoutInflater.inflate(R.layout.canvas2, null, false);
//        CanvasView cv = new CanvasView(this);
//        RelativeLayout parentView = content2.findViewById(R.id.parentView);
//        parentView.addView(cv);

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

    @Override
    protected void onPause() {
        super.onPause();
//        //Pause the animation in case the app is closed and animation is still going on.
//        recyclerViewAdapter.typeWriterView.removeAnimation();
//
//        //Also, dismiss the Dialog showing the description
//        recyclerViewAdapter.descriptionDialog.dismiss();
    }


    @Override
    protected void onStop() {
        super.onStop();
//        //Stop the animation in case the app is closed and animation is still going on.
//        recyclerViewAdapter.typeWriterView.removeAnimation();
//
//        //Also, dismiss the Dialog showing the description
//        recyclerViewAdapter.descriptionDialog.dismiss();
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

//        ItemTouchHelper.Callback callback = new DeletionSwipeHelper(0, ItemTouchHelper.START, this, this);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

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
        dummyIdea1.setName("IdeaPad");
        dummyIdea1.setDesc("xyz");
        ideaList.add(dummyIdea1);

        Idea dummyIdea2 = new Idea();
        dummyIdea2.setId(new Random().nextInt());
        dummyIdea2 = new Idea();
        dummyIdea2.setName("NetControl");
        dummyIdea1.setDesc("xyz");
        ideaList.add(dummyIdea2);

        Idea dummyIdea3 = new Idea();
        dummyIdea3.setId(new Random().nextInt());
        dummyIdea3 = new Idea();
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

    private void drawView() {


        mSignatureView.setPaintColor(Color.BLACK);
        mSignatureView.setPaintWidthPx(getResources().getDimensionPixelSize(R.dimen.paint_width));
        //mSignatureView.setPaintWidthPx(12);
        mSignatureView.setPaintWidthDp(getResources().getDimension(R.dimen.paint_dpi));
        //mSignatureView.setPaintWidthDp(6);
        mSignatureView.setPaintAlpha(255);// from 0 to 255
        mSignatureView.setResizeBehaviour(ResizeBehaviour.CROP);// Must be one of ResizeBehaviour
        // values;
        mSignatureView.setPathRedoUndoCountChangeListener(new PathRedoUndoCountChangeListener() {
            @Override
            public void onUndoCountChanged(int undoCount) {
                // The undoCount is the number of the paths that can be undone
            }

            @Override
            public void onRedoCountChanged(int redoCount) {
                // The redoCount is the number of path removed that can be redrawn
            }
        });

        // This listener will be notified every time a new path has been drawn
        mSignatureView.setOnPathDrawnListener(new PathDrawnListener() {
            @Override
            public void onNewPathDrawn() {
                // The user has finished drawing a path
            }

            @Override
            public void onPathStart() {
                // The user has started drawing a path
            }
        });

        mSignatureView.getDrawScreenshot(new FreeDrawView.DrawCreatorListener() {
            @Override
            public void onDrawCreated(Bitmap draw) {
                // The draw Bitmap is the drawn content of the View
            }

            @Override
            public void onDrawCreationError() {
                // Something went wrong creating the bitmap, should never
                // happen unless the async task has been canceled
            }
        });
    }

    private void fabFunction(@Nullable final String desc) {

        layoutInflater = MainActivity.this.getLayoutInflater();
        final View content = layoutInflater.inflate(R.layout.edit_idea, null, false);

        final EditText editName = content.findViewById(R.id.editName);
        final EditText editDesc = content.findViewById(R.id.editDesc);
        final ImageView image = content.findViewById(R.id.drawingImageView);

        if(desc != null){
            editDesc.setText(desc);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Mann mei ladoo phoota?")
                .setView(content)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Create a new Idea instance which will store information
                        //regarding the idea in respective fields and go into
                        //the realm database

                        if (desc == null) {

                            if (editName.getText() == null || editName.getText().toString().equals("") || editName.getText().toString().equals(" ")) {
                                Toast.makeText(MainActivity.this, "Name field cannot be left blank!", Toast.LENGTH_SHORT).show();
                            }
                            String name = editName.getText().toString();
                            String desc = editDesc.getText().toString();

                            addIdeaToRealm(name, desc);
                        }

                        else{

                            if (editName.getText() == null || editName.getText().toString().equals("") || editName.getText().toString().equals(" ")) {
                                Toast.makeText(MainActivity.this, "Name field cannot be left blank!", Toast.LENGTH_SHORT).show();
                            }

                            String name = editName.getText().toString();
                            addIdeaToRealm(name, desc);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .setNeutralButton(R.string.title_draw, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, CanvasActivity.class);
                        startActivity(intent);
                    }
                })
        ;


        AlertDialog dialog = builder.create();
        // get the center for the clipping circle

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

    public void setByteArray(byte[] bmp) {
        if (bmp != null) {
            Log.d(TAG, "setByteArray: " + "not null bmp");
        }
        this.bmp = bmp;
    }

    public void callNotifyDatasetChanged() {
        recyclerViewAdapter.callNotifyDatasetChanged();
    }

    public void handleDrawing() {

        Realm r = RealmController.getInstance().getRealm();

        r.beginTransaction();
        Idea idea = new Idea();
        idea.setId(System.currentTimeMillis() + RealmController.getInstance().getAllBooks().size() + 1);
        idea.setName(" ");

        r.copyToRealm(idea);
        r.commitTransaction();

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

    private void addIdeaToRealm(String name, String desc){
        realm.beginTransaction();

        Idea idea = new Idea();
        idea.setId(System.currentTimeMillis() + RealmController.getInstance().getAllBooks().size() + 1);
        idea.setName(name);
        idea.setDesc(desc);

        realm.copyToRealm(idea);
        realm.commitTransaction();
    }



}