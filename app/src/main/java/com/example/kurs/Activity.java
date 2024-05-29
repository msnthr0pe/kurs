package com.example.kurs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            String access = getIntent().getStringExtra("access");
            if (!Objects.equals(access, "admin")) {
                hideItem();
            }
            MainFragment mainFragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("access", access);
            mainFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
            navigationView.setCheckedItem(R.id.nav_main);

        }
    }

    private void hideItem()
    {
        navigationView = findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();
        navMenu.findItem(R.id.nav_edit).setVisible(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String access = getIntent().getStringExtra("access");
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("access", access);
        mainFragment.setArguments(bundle);

        if (id == R.id.nav_account) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountFragment()).commit();
            toolbar.setTitle("Account");
        }
        if (id == R.id.nav_main) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
            toolbar.setTitle("Employee manager");
        }
        if (id == R.id.nav_employees) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EmployeesFragment()).commit();
            toolbar.setTitle("Employees");
        }
        if (id == R.id.nav_edit) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditCredentialsFragment()).commit();
            toolbar.setTitle("Login information editor");
        }
        if (id == R.id.nav_chart) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChartFragment()).commit();
            toolbar.setTitle("Employee chart");
        }
        if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
            clearFile();
            Intent intent = new Intent(Activity.this, MainActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearFile()
    {
        String filename = "cred";
        String fileContents = "" + '\n' + "";
        try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}