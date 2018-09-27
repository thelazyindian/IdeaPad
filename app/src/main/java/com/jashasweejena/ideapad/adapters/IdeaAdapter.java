package com.jashasweejena.ideapad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanks.htextview.base.HTextView;
import com.jashasweejena.ideapad.R;
import com.jashasweejena.ideapad.model.Idea;
import com.jashasweejena.ideapad.realm.RealmController;

import in.codeshuffle.typewriterview.TypeWriterView;
import io.realm.Realm;
import io.realm.RealmResults;

public class IdeaAdapter extends RealmRecyclerViewAdapter<Idea> {


    final Context context;
    private final String TAG = IdeaAdapter.class.getSimpleName();
//    public TypeWriterView typeWriterView = null;
    public AlertDialog descriptionDialog = null;
    private Realm realm;
    private LayoutInflater inflater;

    public static final String TYPE_TEXT = "text";

    public static final String TYPE_DRAW = "draw";

    public IdeaAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public IdeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        realm = RealmController.getInstance().getRealm();

        return new IdeaViewHolder(inflater.from(context).inflate(R.layout.single_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        //Get an object of Idea at this very position
        final Idea idea = getItem(position);

        //cast the generic ViewHolder to a specific one
        final IdeaViewHolder ideaViewHolder = (IdeaViewHolder) holder;

//        ideaViewHolder.tag.setText(idea.getTag());
        ideaViewHolder.name.setText(idea.getName());

        //If long pressed, launch the edit dialog
        ideaViewHolder.viewForeground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                fabFunction(position);
                return false;

            }
        });

        //If single clicked, show the description
        ideaViewHolder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View showDesc = layoutInflater.inflate(R.layout.show_desc, null, false);

                HTextView description = showDesc.findViewById(R.id.description);
//                TypeWriterView description = showDesc.findViewById(R.id.description);
//                typeWriterView = description;

                ImageView imageView = showDesc.findViewById(R.id.drawingImageView);

//                description.setDelay(100);

                Idea idea = RealmController.with().getAllBooks().get(position);

                String descriptionString = idea.getDesc();
                description.setText(descriptionString);

                byte[] drawingBytes = idea.getDrawing();

                if (drawingBytes != null) {

                    Bitmap drawing = BitmapFactory.decodeByteArray(drawingBytes, 0, drawingBytes.length);
//                    description.animateText(descriptionString);

                    if(drawing != null){
                        imageView.setImageBitmap(drawing);
                    }




                }

                builder.setView(showDesc)
                        .setTitle("Description");

                AlertDialog alertDialog = builder.create();
                descriptionDialog = alertDialog;
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return RealmController.getInstance().getAllBooks().size();
    }

    public void removeItem(int position) {

        Realm r = RealmController.getInstance().getRealm();

        r.beginTransaction();

        RealmResults<Idea> results = realm.where(Idea.class).findAll();
        results.remove(position);

        r.commitTransaction();

        notifyItemRemoved(position);
    }

    public void restoreItem(Idea idea) {

        realm.beginTransaction();

        realm.copyToRealm(idea);

        realm.commitTransaction();

        notifyDataSetChanged();

    }

    public class IdeaViewHolder extends RecyclerView.ViewHolder {

        public CardView viewForeground;
//        public RelativeLayout viewBackground;
        private ImageView tag;
        private TextView name;


        public IdeaViewHolder(View itemView) {
            super(itemView);

            viewForeground = itemView.findViewById(R.id.card_idea);

            tag = itemView.findViewById(R.id.tag);

            name = itemView.findViewById(R.id.name);

//            viewBackground = itemView.findViewById(R.id.view_background);
        }
    }

    private void fabFunction(final int position){
        RealmController realmController = RealmController.with();
        RealmResults<Idea> listOfIdeas = realmController.getAllBooks();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = layoutInflater.inflate(R.layout.edit_idea, null, false);

        final EditText editName = content.findViewById(R.id.editName);
        final EditText editTag = content.findViewById(R.id.editTag);
        final EditText editDesc = content.findViewById(R.id.editDesc);

        editName.setText(listOfIdeas.get(position).getName());
        editTag.setText(listOfIdeas.get(position).getTag());
        editDesc.setText(listOfIdeas.get(position).getDesc());

        builder.setView(content)
                .setTitle("Edit the idea")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<Idea> listOfIdeas = realm.where(Idea.class).findAll();

                        Idea idea = listOfIdeas.get(position);

                        realm.beginTransaction();

                        String name;
                        String tag;
                        String desc;


                        name = editName.getText().toString();
                        tag = editTag.getText().toString();
                        desc = editDesc.getText().toString();

                        if (editName.getText() == null || editName.getText().toString().equals("") || editName.getText().toString().equals(" ")) {
                            Toast.makeText(context.getApplicationContext(), "Name field cannot be left blank!", Toast.LENGTH_SHORT).show();
                            realm.commitTransaction();
                        } else {

                            idea.setName(name);
                            idea.setTag(tag);
                            idea.setDesc(desc);

                            realm.copyToRealm(idea);

                            realm.commitTransaction();

                            notifyDataSetChanged();

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
}
