package br.edu.ufcg.kickzoeira.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.fragments.ProfileFragment;
import br.edu.ufcg.kickzoeira.fragments.SeguidoresFragment;
import br.edu.ufcg.kickzoeira.fragments.SeguindoFragment;
import br.edu.ufcg.kickzoeira.fragments.SimularFragment;
import br.edu.ufcg.kickzoeira.fragments.SobreFragment;

public class KickZoeiraMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener, SeguindoFragment.OnFragmentInteractionListener, SeguidoresFragment.OnFragmentInteractionListener, SimularFragment.OnFragmentInteractionListener, SobreFragment.OnFragmentInteractionListener {

    public CollapsingToolbarLayout collapsingToolbar;
    public AppBarLayout appBarLayout;
    public ImageView imageViewLogoTop;
    public Toolbar toolbar;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private View headerView;

    public FloatingActionButton fabFacebookShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kick_zoeira_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        imageViewLogoTop = (ImageView) findViewById(R.id.backdrop);
        imageViewLogoTop.setImageResource(R.mipmap.estadio);

        collapsingToolbar.setStatusBarScrimColor(Color.parseColor("#00000000"));
        collapsingToolbar.setContentScrimColor(Color.parseColor("#66000000"));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fabFacebookShare = (FloatingActionButton) findViewById(R.id.fab_fb_share);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.nav_header_profile);
        setupHeader();

        Fragment fragment = ProfileFragment.newInstance();
        setTitle("Perfil Zoeira");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentView, fragment).commit();
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++)
        {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++)
            {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.greed
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    private void setupHeader(){
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.imageView);
        TextView tvUserName = (TextView) headerView.findViewById(R.id.tvUserName);
        TextView tvUserEmail = (TextView) headerView.findViewById(R.id.tvUserEmail);

        profileImage.setImageURI(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        tvUserName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() == null ? "Default Name" : FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        tvUserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //FIXME: this is the way to add name and photoURI
//        UserProfileChangeRequest builder = new UserProfileChangeRequest.Builder().setDisplayName("J @ M").build();
//        FirebaseAuth.getInstance().getCurrentUser().updateProfile(builder);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class fragmentClass = ProfileFragment.class;

        if (id == R.id.nav_perfil) {
            fragmentClass = ProfileFragment.class;
            //collapsingToolbar.setTitle(item.getTitle());
        } else if (id == R.id.nav_seguindo) {
            fragmentClass = SeguindoFragment.class;
        } else if (id == R.id.nav_seguidores) {
            fragmentClass = SeguidoresFragment.class;
        } else if (id == R.id.nav_simular) {
            fragmentClass = SimularFragment.class;
        } else if (id == R.id.nav_sobre) {
            fragmentClass = SobreFragment.class;
        } else if (id == R.id.nav_sugestoes) {

            SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "kickaboutapp@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Kick Zoeira - Feedback / Sugestão - "+ dt1.format(new Date(System.currentTimeMillis())));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new Intent(Intent.ACTION_SENDTO));
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));

        } else if (id == R.id.nav_sair) {
            finish();
            return true;
        }


        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.fragmentView, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void openNavigation(View view){
        String URL = view.getTag().toString();
        Log.d("URL", URL);
        Uri uri = Uri.parse(URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
