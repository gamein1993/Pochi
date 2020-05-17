package com.will.pochi.flagment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class KiDialogFragment extends DialogFragment {
    private final CharSequence title;
    private final CharSequence message;
    private final EditText et;
    private final DialogInterface.OnClickListener clickListener;
    private final DialogInterface.OnDismissListener dismissListener;

    public KiDialogFragment(CharSequence title, CharSequence message, EditText et, DialogInterface.OnClickListener clickListener, DialogInterface.OnDismissListener dismissListener) {
        this.title = title;
        this.message = message;
        this.et = et;
        this.clickListener = clickListener;
        this.dismissListener = dismissListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(et);
        builder.setPositiveButton("OK", clickListener);
        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) dismissListener.onDismiss(dialog);
    }
}
