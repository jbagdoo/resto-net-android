package ca.usimage.resto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public  class GetDataDialog extends DialogFragment {


    public static GetDataDialog newInstance(int title) {
    	GetDataDialog frag = new GetDataDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.restonet4)
                .setTitle(R.string.app_name)
                .setMessage(title)
                
                .setNegativeButton(R.string.dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((RestonetActivity)getActivity()).doNegativeClick();
                        }
                    }
                )
                 .setPositiveButton(R.string.dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((RestonetActivity)getActivity()).doPositiveClick();
                        }
                    }
                 )
                .create();
    }
}