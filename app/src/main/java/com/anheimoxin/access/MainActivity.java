package com.anheimoxin.access;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anheimoxin.access.service.BaseService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseService instance = BaseService.getInstance();
        instance.init(this);
        if (!instance.checkAccessibilityEnabled(getResources().getString(R.string.access_name))) {
            instance.goAccess();
        }
    }
}
