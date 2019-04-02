package fr.alanlg.themovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import fr.alanlg.themovieapp.fragment.CreditFragment;
import fr.alanlg.themovieapp.fragment.FavoriteFragment;
import fr.alanlg.themovieapp.fragment.HomeFragment;
import fr.alanlg.themovieapp.fragment.PopularFragment;
import fr.alanlg.themovieapp.fragment.SearchNameFragment;
import fr.alanlg.themovieapp.fragment.SearchTypeFragment;
import fr.alanlg.themovieapp.fragment.SettingsFragment;
import fr.alanlg.themovieapp.fragment.TopRatedFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());
    }

    private void refreshUserDisplay(FirebaseUser user, boolean connected) {
        TextView userDisplayName = findViewById(R.id.userDisplayName);
        TextView userEmail = findViewById(R.id.userEmail);

        if (connected && userDisplayName != null) {
            userDisplayName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());

            //changement de l'affichage du menu
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.nav_connection).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_parameters).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_disconnection).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_favorite).setVisible(true);

            Snackbar.make(findViewById(R.id.drawer_layout), "Vous êtes connecté en tant que " + user.getDisplayName() + " !", Snackbar.LENGTH_LONG).show();
        } else {
            userDisplayName.setText(R.string.nav_header_title);
            userEmail.setText(R.string.nav_header_subtitle);

            //changement de l'affichage du menu
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.nav_connection).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_parameters).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_disconnection).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_favorite).setVisible(false);

            Snackbar.make(findViewById(R.id.drawer_layout), "Vous êtes bien déconnecté !", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                this.refreshUserDisplay(user, true);

            } else {
                if (response == null) {
                    Toast.makeText(this.getApplicationContext(), "Connection annulée", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.getApplicationContext(), "Problème de connection", Toast.LENGTH_SHORT).show();
                    Log.d("Erreur connection : ", Objects.requireNonNull(response.getError()).getMessage());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            refreshUserDisplay(user, true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            toolbar.setTitle(R.string.app_name);
        } else if (id == R.id.nav_search_name) {
            fragment = new SearchNameFragment();
            toolbar.setTitle("Recherche de films par nom");
        } else if (id == R.id.nav_search_type) {
            fragment = new SearchTypeFragment();
            toolbar.setTitle("Recherche de films par type");
        } else if (id == R.id.nav_top_rated) {
            fragment = new TopRatedFragment();
            toolbar.setTitle("Films les mieux notés");
        } else if (id == R.id.nav_popular) {
            fragment = new PopularFragment();
            toolbar.setTitle("Films populaires");
        } else if (id == R.id.nav_favorite) {
            fragment = new FavoriteFragment();
            toolbar.setTitle("Vos favoris");
        } else if (id == R.id.nav_credit) {
            fragment = new CreditFragment();
            toolbar.setTitle("Crédits de l'application");
        } else if (id == R.id.nav_parameters) {
            fragment = new SettingsFragment();
            toolbar.setTitle("Paramètres");
        } else if (id == R.id.nav_connection) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        } else if (id == R.id.nav_disconnection) {
            this.signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        refreshUserDisplay(null, false);
                    }
                });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }
        return false;
    }

}
