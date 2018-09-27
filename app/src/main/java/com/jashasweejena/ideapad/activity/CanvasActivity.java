package com.jashasweejena.ideapad.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.ajithvgiri.canvaslibrary.CanvasView;
import com.jashasweejena.ideapad.R;
import com.jashasweejena.ideapad.model.Idea;
import com.jashasweejena.ideapad.realm.RealmController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

public class CanvasActivity extends AppCompatActivity {
    RelativeLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentView = findViewById(R.id.parentView);
        CanvasView canvasView = new CanvasView(this);
        parentView.addView(canvasView);

        parentView.setDrawingCacheEnabled(true);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCanvas();
            }
        });
    }

    private void saveCanvas() {

        Bitmap bitmap = parentView.getDrawingCache();

//        final File Path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Canvas");
//        Path.mkdirs();
//        String fileName = "Canvas-" + System.currentTimeMillis() + ".jpg";
//        File saveFile = new File(Path, fileName);
//        FileOutputStream FOS = null;
//        try {
//            FOS = new FileOutputStream(saveFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FOS);
//            FOS.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        byte[] byteArray = convertBitmapToByteArray(bitmap);

        Realm realm = RealmController.getInstance().getRealm();

        realm.beginTransaction();
        Idea idea = new Idea();
        idea.setId(System.currentTimeMillis() + RealmController.getInstance().getAllBooks().size() + 1);
        idea.setDrawing(byteArray);
        realm.copyToRealm(idea);
        realm.commitTransaction();


    }

    private byte[] convertBitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
