package com.jashasweejena.ideapad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jashasweejena.ideapad.R;
import com.jashasweejena.ideapad.activity.MainActivity;
import com.jashasweejena.ideapad.app.Prefs;
import com.jashasweejena.ideapad.model.Idea;
import com.jashasweejena.ideapad.realm.RealmController;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmResults;

public class IdeaAdapter extends RealmRecyclerViewAdapter<Idea> {


    private final String TAG = IdeaAdapter.class.getSimpleName();
    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

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

        ideaViewHolder.tag.setText(idea.getTag());
        ideaViewHolder.name.setText(idea.getName());

        //If clicked once, remove that card
        ideaViewHolder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RealmResults<Idea> listOfIdeas = realm.where(Idea.class).findAll();

                //To get the idea class at this position.
                Idea idea = listOfIdeas.get(position);
                String name = idea.getName(); //Store name before deleting the object so that we can display the
                // name in a toast.

                realm.beginTransaction();

                listOfIdeas.remove(position);

                //Means, now everything in the listOfIdeas is deleted
                if (listOfIdeas.size() == 0) {

                    Prefs.with(context).setPreLoad(false);

                }

                realm.commitTransaction();
                notifyDataSetChanged();

                //Inform the user by a toast that the object was removed
                Toast.makeText(context, name + " Was deleted", Toast.LENGTH_SHORT).show();

            }
        });


        //If long pressed, launch the edit dialog
        ideaViewHolder.viewForeground.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View content = layoutInflater.inflate(R.layout.edit_idea, null, false);


                final EditText editName = content.findViewById(R.id.editName);
                final EditText editTag = content.findViewById(R.id.editTag);

                editName.setText(realm.where(Idea.class).findAll().get(position).getName());
                editTag.setText(realm.where(Idea.class).findAll().get(position).getTag());

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
                                name = editName.getText().toString();
                                tag = editTag.getText().toString();

                                if(editName.getText() == null || editName.getText().toString().equals("") || editName.getText().toString().equals(" ")) {
                                    Toast.makeText(context.getApplicationContext(), "Name field cannot be left blank!", Toast.LENGTH_SHORT).show();
                                    realm.commitTransaction();
                                } else {


                                    idea.setName(name);
                                    idea.setTag(tag);


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

                return false;

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

        private TextView tag;

        private TextView name;

        public RelativeLayout viewBackground;


        public IdeaViewHolder(View itemView) {
            super(itemView);

            viewForeground = itemView.findViewById(R.id.card_idea);

            tag = itemView.findViewById(R.id.tag);

            name = itemView.findViewById(R.id.name);

            viewBackground = itemView.findViewById(R.id.view_background);
        }
    }
}
