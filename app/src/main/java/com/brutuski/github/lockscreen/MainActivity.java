package com.brutuski.github.lockscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnEnable, btnDisable, btnLock = null;
    private ImageView ivAdminStatus = null;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName compName;
    public static final int enable = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, Admin.class);

        btnEnable = findViewById(R.id.btnEnable);
        btnDisable = findViewById(R.id.btnDisable);
        btnLock = findViewById(R.id.btnLock);
        ivAdminStatus = findViewById(R.id.iv_admin_status);


        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Permission is needed to control the lock functionality.");
                startActivityForResult(intent, enable);
            }
        });

        btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager.removeActiveAdmin((compName));
                btnDisable.setVisibility((View.GONE));
                btnEnable.setVisibility(View.VISIBLE);
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean active = devicePolicyManager.isAdminActive(compName);

                if (active) {
                    devicePolicyManager.lockNow();
                }
                else
                    Toast.makeText(MainActivity.this, "Please enable Device Admin Feature", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        btnDisable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        btnEnable.setVisibility(isActive ? View.GONE : View.VISIBLE);

        if(isActive)
            ivAdminStatus.setImageResource(R.drawable.png_admin_enabled);

        else if(!isActive)
            ivAdminStatus.setImageResource(R.drawable.png_admin_disabled);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case enable :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Device Admin → enabled", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Failed to enable Device Admin Features", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
