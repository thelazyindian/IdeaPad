package com.jashasweejena.ideapad.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.jashasweejena.ideapad.R;
import com.jashasweejena.ideapad.model.Idea;
import com.jashasweejena.ideapad.realm.RealmController;
import com.rm.freedrawview.FreeDrawView;
import com.rm.freedrawview.PathDrawnListener;
import com.rm.freedrawview.PathRedoUndoCountChangeListener;
import com.rm.freedrawview.ResizeBehaviour;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class CanvasActivity2 extends AppCompatActivity implements FreeDrawView.DrawCreatorListener {
    private static final String TAG = CanvasActivity2.class.getSimpleName();
    FreeDrawView mSignatureView;
    Realm realm;

    Button undo;

    FloatingActionButton fab;
    Storage storage;
    private android.content.Context context = CanvasActivity2.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSignatureView = findViewById(R.id.free_draw_view);

        fab = findViewById(R.id.fab);

        realm = RealmController.getInstance().getRealm();

        mSignatureView = findViewById(R.id.free_draw_view);
        undo = findViewById(R.id.undo);


//        undo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSignatureView.undoLast();
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignatureView.getDrawScreenshot(CanvasActivity2.this);
            }
        });


        // Setup the View
        mSignatureView.setPaintColor(ContextCompat.getColor(context, R.color.orange));

        mSignatureView.setPaintWidthPx(getResources().getDimensionPixelSize(R.dimen.paint_width));
        //mSignatureView.setPaintWidthPx(12);
        mSignatureView.setPaintWidthDp(getResources().getDimension(R.dimen.paint_width));
        //mSignatureView.setPaintWidthDp(6);
        mSignatureView.setPaintAlpha(255);// from 0 to 255
        mSignatureView.setResizeBehaviour(ResizeBehaviour.CROP);// Must be one of ResizeBehaviour
        // values;

        // This listener will be notified every time the path done and undone count changes
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

        // This will take a screenshot of the current drawn content of the view
        mSignatureView.getDrawScreenshot(new FreeDrawView.DrawCreatorListener() {
            @Override
            public void onDrawCreated(Bitmap draw) {
//                realm.beginTransaction();
//                Idea idea = new Idea();
//                idea.setDrawing(convertBitmapToByteArray(draw));
//                realm.copyToRealm(idea);
//                realm.commitTransaction();
//
//                Log.d(TAG, "onDrawCreated: " + draw.getByteCount());
            }

            @Override
            public void onDrawCreationError() {
                // Something went wrong creating the bitmap, should never
                // happen unless the async task has been canceled
            }
        });
    }

    private byte[] convertBitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onDrawCreated(Bitmap draw) {

    }

    @Override
    public void onDrawCreationError() {

    }
}
