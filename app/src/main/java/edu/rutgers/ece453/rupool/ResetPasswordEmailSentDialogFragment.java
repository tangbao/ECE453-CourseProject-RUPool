package edu.rutgers.ece453.rupool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by zhu_z on 2017/12/17.
 */

public class ResetPasswordEmailSentDialogFragment extends DialogFragment {
    private ResetPasswordEmailSentDialogFragmentListener mDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Password reset email sent.")
                .setTitle("Email sent successfully")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialogListener.success();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mDialogListener = (ResetPasswordEmailSentDialogFragmentListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement ResetPasswordEmailSentDialogFragmentListener");
        }
    }

    public interface ResetPasswordEmailSentDialogFragmentListener {
        void success();
    }
}

