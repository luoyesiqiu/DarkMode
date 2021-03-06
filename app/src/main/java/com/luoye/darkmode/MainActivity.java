package com.luoye.darkmode;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luoye.darkmode.fragment.SettingsFragment;

import java.util.ArrayList;

import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.SimpleCallback;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        request();
    }

    private  void request(){
        PermissionManager.Builder()
                .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                .askAgain(true)
                .askAgainCallback(new AskAgainCallback() {
                    @Override
                    public void showRequestPermission(AskAgainCallback.UserResponse response) {

                    }
                })
                .callback(new SimpleCallback() {
                    @Override
                    public void result(boolean allPermissionsGranted) {
                        if(allPermissionsGranted){
                            FragmentManager fragmentManager=getFragmentManager();
                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frag_container,new SettingsFragment());
                            fragmentTransaction.commit();
                        }
                    }
                })
                .ask(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }
}
