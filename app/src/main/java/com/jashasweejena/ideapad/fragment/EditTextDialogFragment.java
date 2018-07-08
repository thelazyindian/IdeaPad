package com.jashasweejena.ideapad.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jashasweejena.ideapad.R;

public class EditTextDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    EditText editName;
    EditText editTag;
    EditText editDesc;

    public EditTextDialogFragment() {
    }

    public static EditTextDialogFragment newInstance(String title) {

        EditTextDialogFragment fragment = new EditTextDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_idea, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editName = view.findViewById(R.id.editName);
        editTag = view.findViewById(R.id.editTag);
        editDesc = view.findViewById(R.id.editDesc);

        getDialog().setTitle(getArguments().getString("title"));

        editName.setOnEditorActionListener(this);
        editTag.setOnEditorActionListener(this);
        editDesc.setOnEditorActionListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        String name;
        String tag;
        String desc;

        if (EditorInfo.IME_ACTION_DONE == actionId) {

            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(editName.getText().toString(),
                    editTag.getText().toString(),
                    editDesc.getText().toString());
            return true;

        }
        return false;
    }


    public interface EditNameDialogListener {
        void onFinishEditDialog(String editName, String editTag, String editDesc);
    }
}
