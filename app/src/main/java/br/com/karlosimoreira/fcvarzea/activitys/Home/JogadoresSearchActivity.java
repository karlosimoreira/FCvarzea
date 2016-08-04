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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.adapter.JogadorAdapter;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;
import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;

public class JogadoresSearchActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    public static Context mContext;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private String clausura;
    private String value;
    private int valueTipo;
    private Query query;
    private ArrayList<User> userList;
    int position;

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
                        Toast.makeText(JogadoresSearchActivity.this,"Salvar Lista de convocados", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        queryJogadores(valueTipo);
        RecyclerView rvUsers = (RecyclerView) findViewById(R.id.rvList);

        if (rvUsers != null) {
            rvUsers.setHasFixedSize( true );
            rvUsers.addOnItemTouchListener(new RecyclerViewTouchListener(this, rvUsers,this));
            rvUsers.setLayoutManager( new LinearLayoutManager(this));
            JogadorAdapter adapter = new JogadorAdapter(getnListUserFirebase(query),this);
            rvUsers.setAdapter(adapter);
        }
    }

    private ArrayList<User> getnListUserFirebase(Query mQuery){
        userList = new ArrayList<>();

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    userList.add(ds.getValue(User.class));
                }
                for (int i =0; i<userList.size(); i++){
                    Log.i("Tag", "Name: "+ userList.get(i).getName() + "\n Position: " + userList.get(i).getPosition()+ "\n Photo: " + userList.get(i).getPhoto()+ "\n Classificação: " + userList.get(i).getClassificacao()) ;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return userList;
    }

    private void getDataForActivity(){
        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                clausura = params.getString("clausuraPrimaria");
                value = params.getString("valuePrimario");
                valueTipo = Integer.parseInt(params.getString("tipo"));
                Log.i("getDataForActivity", clausura + " & " + value + " & " + valueTipo);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_search_actions, menu);
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
        Toast.makeText(this, "Position: "+ position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(this, "LongPressPosition: " + position, Toast.LENGTH_SHORT).show();
    }

    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener{

        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context mContext, final RecyclerView mRecyclerView,  RecyclerViewOnClickListenerHack rvoclh) {
            this.mContext = mContext;
            this.mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(childView != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(childView,
                                mRecyclerView.getChildAdapterPosition(childView) );
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                    if(childView != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(childView,
                                mRecyclerView.getChildAdapterPosition(childView) );
                    }
                    return(true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
    }
}
