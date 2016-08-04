package br.com.karlosimoreira.fcvarzea.activitys.Home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.activitys.MainActivity;
import br.com.karlosimoreira.fcvarzea.adapter.UserRecyclerAdapter;
import br.com.karlosimoreira.fcvarzea.adapter.UserViewHolder;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;
import br.com.karlosimoreira.fcvarzea.listener.CustomValueEventListener;

public class JogadoresActivity extends AppCompatActivity {

    public static Context mContext;
    private RecyclerView rvUsers;
    private UserRecyclerAdapter adapter;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String clausura;
    private String value;
    private int valueTipo;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogadores);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference = LibraryClass.getFirebase();
        mContext = this;
        getDataForActivity();

    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        toolbar.setTitle( getResources().getString(R.string.title_activity_summon_players) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(JogadoresActivity.this,"Salvar Lista de convocados", Toast.LENGTH_LONG).show();
                        break;

                }
                return true;
            }
        });
       progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        queryJogadores(valueTipo);
        rvUsers = (RecyclerView)findViewById(R.id.rvList);
        if (rvUsers != null) {
            rvUsers.setHasFixedSize( true );
        }
        rvUsers.setLayoutManager( new LinearLayoutManager(this));
        adapter = new UserRecyclerAdapter(
                User.class,
                R.layout.modelo_convocar_jogadores,
                UserViewHolder.class,
                query);
        rvUsers.setAdapter(adapter);

    }
    private void getDataForActivity(){
        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                clausura = params.getString("clausuraPrimaria");
                value = params.getString("valuePrimario");
                valueTipo = Integer.parseInt(params.getString("tipo"));
                Log.v("getDataForActivity", params.getString("clausuraPrimaria") + " & " + params.getString("valuePrimario"+ " & " + params.getString("tipo")));
            }
        }
    }

    private void queryJogadores(int tipo){
        switch(tipo)
        {
            case 1:
                 query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
            break;
            case 2:
                query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
                break;
            case 3:
                 query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
                break;
            case 4:
                 query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
                break;
            case 5:
                 query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
                break;
            case 6:
                query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
                break;
            case 7:
                query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
                break;
            default:
                query = databaseReference.child("User").orderByChild("name");

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_actions, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_searchable_activity);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( item );
        }

        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        searchView.setQueryHint( getResources().getString(R.string.search_hint) );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
