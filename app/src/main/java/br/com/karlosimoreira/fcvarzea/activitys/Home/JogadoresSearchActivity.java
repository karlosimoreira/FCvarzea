package br.com.karlosimoreira.fcvarzea.activitys.Home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.List;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.adapter.JogadorAdapter;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;
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
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private ArrayList<User> userList;
    private List<Bitmap> bitmapsList;
    private ImagemProcess ip;

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
        }
        /*rvUsers.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/
       /* if (rvUsers != null) {
            rvUsers.setLayoutManager( new LinearLayoutManager(this));
            UserNoRecyclerAdapter adapter = new UserNoRecyclerAdapter(getnListUserFirebase(query), bitmapsList,this);
            adapter.setRecyclerViewOnClickListenerHack(this);
            rvUsers.setAdapter(adapter);
        }*/
        if (rvUsers != null) {
            rvUsers.setLayoutManager( new LinearLayoutManager(this));
            JogadorAdapter adapter = new JogadorAdapter(getnListUserFirebase(query),this);
            adapter.setRecyclerViewOnClickListenerHack(this);
            rvUsers.setAdapter(adapter);
        }
    }

    private ArrayList<User> getnListUserFirebase(Query mQuery){
        userList = new ArrayList<>();
        bitmapsList = new ArrayList<>();
        ip = new ImagemProcess();

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    userList.add(ds.getValue(User.class));
                    bitmapsList.add( ip.loadBitmap(ds.getValue(User.class).getPhoto()));
                    //userList = new ArrayList(new HashSet(userList));
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
        Toast.makeText(this, "Position: "+ position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(this, "LongPressPosition: " + position, Toast.LENGTH_SHORT).show();
    }

    public static class RecycleViewTouchEvent implements RecyclerView.OnItemTouchListener{

        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecycleViewTouchEvent(Context mContext, final RecyclerView mRecyclerView, final RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack) {
            this.mContext = mContext;
            this.mRecyclerViewOnClickListenerHack = mRecyclerViewOnClickListenerHack;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                mRecyclerView.getChildPosition(cv) );
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                mRecyclerView.getChildPosition(cv) );
                    }

                    return(true);
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
