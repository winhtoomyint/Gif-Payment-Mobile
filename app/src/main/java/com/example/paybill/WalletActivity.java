package com.example.paybill;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.paybill.ui.NotiFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;


import static com.example.paybill.Utils.UserDetailData.address;
import static com.example.paybill.Utils.UserDetailData.city;
import static com.example.paybill.Utils.UserDetailData.email_address;
import static com.example.paybill.Utils.UserDetailData.location;
import static com.example.paybill.Utils.UserDetailData.password;
import static com.example.paybill.Utils.UserDetailData.phone;
import static com.example.paybill.Utils.UserDetailData.state;
import static com.example.paybill.Utils.UserDetailData.userAccountID;
import static com.example.paybill.Utils.UserDetailData.userExternalKey;
import static com.example.paybill.Utils.UserDetailData.username;


public class WalletActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,R.id.nav_cash_in,R.id.nav_about,R.id.nav_bank_cashin,R.id.nav_shopfrag,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send,R.id.nav_bill,R.id.nav_mbtopup,R.id.nav_setting,R.id.nav_termcondit)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wallet, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(WalletActivity.this,ServiceActivity.class));
                Toast.makeText(this, "Clicked Services", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.log_out:
                logOut();
                startActivity(new Intent(WalletActivity.this, LoginActivity.class));
                return true;
            case R.id.mymail:
                startActivity(new Intent(this, NotiFragment.class));
                Toast.makeText(this, "Mail", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void logOut(){
        username = null;
        password = null;
        userAccountID = null;
        userExternalKey = null;
        location = null;
        phone = null;
        email_address="";
        address="";
        city="";
        state="";
        finish();
    }

}
