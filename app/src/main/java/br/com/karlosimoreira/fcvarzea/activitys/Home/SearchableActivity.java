package br.com.karlosimoreira.fcvarzea.activitys.Home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;
import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;

public class SearchableActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {
    public static Context mContext;
    private RecyclerView rvUsers;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private String clausura;
    private String value;
    private int valueTipo;
    private Query query;
    private List<User> mList;
    private List<User> mListAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            //mList = savedInstanceState.getParcelableArrayList("mList");
            //mListAux = savedInstanceState.getParcelableArrayList("mListAux");
        } else {
            mListAux = new ArrayList<>();
        }
        setContentView(R.layout.activity_searchable);
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
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(SearchableActivity.this,"Salvar Lista de convocados", Toast.LENGTH_LONG).show();
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
       /* adapter = new UserRecyclerAdapter(
                User.class,
                R.layout.modelo_convocar_jogadores,
                UserViewHolder.class,
                query);
        rvUsers.setAdapter(adapter);*/

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
            case 0:
                query = databaseReference.child("User").orderByChild("name");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // adapter.cleanup();
    }

    public void hendleSearch( Intent intent ){
        if( Intent.ACTION_SEARCH.equalsIgnoreCase( intent.getAction() ) ){
            String q = intent.getStringExtra( SearchManager.QUERY );
            filterPlayer( q );

           /* SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery( q, null );*/
        }
    }
    public void filterPlayer(String q ){
        mListAux.clear();

        for( int i = 0, tamI = mList.size(); i < tamI; i++ ){
            if( mList.get(i).getPosition().toLowerCase().startsWith( q.toLowerCase() ) ){
                mListAux.add( mList.get(i) );
            }
        }
        for( int i = 0, tamI = mList.size(); i < tamI; i++ ){
            if( !mListAux.contains( mList.get(i) )
                    && mList.get(i).getName().toLowerCase().startsWith( q.toLowerCase() ) ){
                mListAux.add( mList.get(i) );
            }
        }}


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

    @Override
    public void onClickListener(View view, int position) {

    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }
}
