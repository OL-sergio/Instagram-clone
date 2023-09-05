package udemy.java.instagram_clone.helper;


import android.app.AlertDialog;
import android.content.Context;

import udemy.java.instagram_clone.R;

public abstract class CustomAlertDialog {

    static AlertDialog alertDialog ;

    public static void setAlertDialog(Context context, String title) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setCancelable(false);
        alert.setView(R.layout.alert_dialog_loading);

        alertDialog = alert.create();
        alertDialog.show();
    }

    public static void dismissAlertDialog() {
        alertDialog.dismiss();
    }

}
