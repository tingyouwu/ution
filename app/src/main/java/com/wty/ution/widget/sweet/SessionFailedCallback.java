package com.wty.ution.widget.sweet;

import SweetAlert.SweetAlertDialog;

public class SessionFailedCallback extends SweetCallback {

    @Override
    public void onClick(SweetAlertDialog dialog) {
        dialog.dismiss();
    }

}