package br.com.karlosimoreira.fcvarzea.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.activitys.Home.MyMatchActivity;
import br.com.karlosimoreira.fcvarzea.activitys.Home.NewMatchActivity;
import br.com.karlosimoreira.fcvarzea.activitys.Home.SearchMatchActivity;
import br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys.ManageAccountsActivity;
import br.com.karlosimoreira.fcvarzea.adapter.UserRecyclerAdapter;
import br.com.karlosimoreira.fcvarzea.adapter.UserViewHolder;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.DownloadImageTask;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;
import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;
import br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys.LinkAccountsActivity;
import br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys.LoginActivity;
import br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys.RemoveUserActivity;
import br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys.UpdateActivity;
import br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys.UpdateLoginActivity;
import br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys.UpdatePasswordActivity;
import br.com.karlosimoreira.fcvarzea.domain.util.SlidingTabLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Carlos on 28/05/2016.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener, View.OnClickListener{
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private UserRecyclerAdapter adapter;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView txtName;
    private TextView txtInfo;
    private CircleImageView photoHeader;
    private User user;
    private View header;
    private User profilerHeader;
    private ImageView imageViewNew;
    private ImageView imageViewMy;
    private ImageView imageViewSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiler);

        //Inicializar a ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if( firebaseAuth.getCurrentUser() == null  ){
                    Intent intent = new Intent( MainActivity.this, LoginActivity.class );
                    startActivity( intent );
                    finish();
                }
            }
        };
        //Inicia uma instacia do firebase para o Node expecificado
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener( authStateListener );
        databaseReference = LibraryClass.getFirebase();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu( selectMenuNavigationDrawer());
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
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
    protected void onResume() {
        super.onResume();
       init();
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        profilerHeader = dataSnapshot.getValue(User.class);
        txtName.setText(profilerHeader.getName());
        txtInfo.setText(profilerHeader.getEmail());

        if(profilerHeader.getPhoto() == "" || profilerHeader.getPhoto() == null){
            photoHeader.setImageBitmap(ImagemProcess.getResizedBitmap(setupPlaceholder(R.drawable.ic_fc_varzea_medio), 175, 175));
        }else {
            setupImage(profilerHeader.getPhoto());
        }

    }
    @Override
    public void onCancelled(DatabaseError databaseError) {}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //adapter.cleanup();

        if( authStateListener != null ){
            mAuth.removeAuthStateListener( authStateListener );
        }
    }


    private void init() {
        txtName =(TextView)header.findViewById(R.id.txtName);
        txtInfo=(TextView)header.findViewById(R.id.txtInfo);
        photoHeader= (CircleImageView)header.findViewById(R.id.imgProfiler);
        imageViewMy = (ImageView)findViewById(R.id.imageViewMy);
        imageViewMy.setOnClickListener(this);
        imageViewNew = (ImageView)findViewById(R.id.imageViewNew);
        imageViewNew.setOnClickListener(this);
        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearch);
        imageViewSearch.setOnClickListener(this);

        user = new User();
        user.setId( FirebaseAuth.getInstance().getCurrentUser().getUid() );
        user.contextDataDB(this);
    }


    private void setupImage(String Url) {

        Log.i("setupImage","Url: " + Url);
        new DownloadImageTask((CircleImageView) header.findViewById(R.id.imgProfiler)).execute(Url);
    }

    private Bitmap setupPlaceholder(int Url) {

        Drawable drawable = this.getResources().getDrawable(Url);
        Bitmap bitmap =((BitmapDrawable)drawable).getBitmap();

        return bitmap;
    }


    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        User user = new User();
        if(user.checkSocialNetwork(this))
        {
            getMenuInflater().inflate(R.menu.menu_social_network_logged, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_update){
            startActivity(new Intent(this, UpdateActivity.class));
        }
        else if(id == R.id.action_update_login){
            startActivity(new Intent(this, UpdateLoginActivity.class));
        }
        else if(id == R.id.action_update_password){
            startActivity(new Intent(this, UpdatePasswordActivity.class));
        }
        else if(id == R.id.action_link_accounts){
            startActivity(new Intent(this, LinkAccountsActivity.class));
        }
        else if(id == R.id.action_remove_user){
            startActivity(new Intent(this, RemoveUserActivity.class));
        }
        else if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            finish();
        }
            return super.onOptionsItemSelected(item);
        }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_update_profiler) {
            startActivity(new Intent(this, UpdateActivity.class));
        }
        else if (id == R.id.nav_link_accounts) {
            startActivity(new Intent(this, LinkAccountsActivity.class));
        }
        else if (id == R.id.nav_update_conta) {
            startActivity(new Intent(this, ManageAccountsActivity.class));
        }
        else if (id == R.id.nav_chat) {

        }
        else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private int selectMenuNavigationDrawer(){
        User user = new User();
        int id;
        if(user.checkSocialNetwork(this))
        {
            id = R.menu.activity_profiler_drawer;
        }
        else
        {
            id = R.menu.activity_profiler_drawer_email;
        }
        return id;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewNew:
                startActivity(new Intent(this, NewMatchActivity.class));
                break;
            case R.id.imageViewMy:
                startActivity(new Intent(this, MyMatchActivity.class));
                break;
            case R.id.imageViewSearch:
                startActivity(new Intent(this, SearchMatchActivity.class));
                break;
        }
    }
}
