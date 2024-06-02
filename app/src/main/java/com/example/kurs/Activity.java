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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
        toolbar.setTitle("Редактор сотрудников");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            String access = getIntent().getStringExtra("access");
            if (!Objects.equals(access, "admin")) {
                hideItem();
            }
            //MainFragment mainFragment = new MainFragment();
            EmployeeManagerFragment employeeManagerFragment = new EmployeeManagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("access", access);
            //mainFragment.setArguments(bundle);
            employeeManagerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, employeeManagerFragment).commit();
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

        //MainFragment mainFragment = new MainFragment();
        EmployeeManagerFragment employeeManagerFragment = new EmployeeManagerFragment();
        AccountFragment accountFragment = new AccountFragment();
        EmployeesFragment employeesFragment = new EmployeesFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(getIntent().getExtras());
        //mainFragment.setArguments(bundle);
        employeeManagerFragment.setArguments(bundle);
        accountFragment.setArguments(bundle);
        employeesFragment.setArguments(bundle);

        if (id == R.id.nav_account) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, accountFragment).commit();
            toolbar.setTitle("Аккаунт");
        }
        if (id == R.id.nav_main) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, employeeManagerFragment).commit();
            toolbar.setTitle("Редактор сотрудников");
        }
        if (id == R.id.nav_employees) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, employeesFragment).commit();
            toolbar.setTitle("Сотрудники");
        }
        if (id == R.id.nav_edit) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditCredentialsFragment()).commit();
            toolbar.setTitle("Редактор акканутов");
        }
        if (id == R.id.nav_chart) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChartFragment()).commit();
            toolbar.setTitle("Диаграмма аккаунтов");
        }
        if (id == R.id.nav_logout) {
            //Toast.makeText(this, "Успешный выход", Toast.LENGTH_SHORT).show();
            clearFile("cred");
            clearFile("currentLogin");
            Intent intent = new Intent(Activity.this, MainActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearFile(String filename)
    {

        String fileContents = "";
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