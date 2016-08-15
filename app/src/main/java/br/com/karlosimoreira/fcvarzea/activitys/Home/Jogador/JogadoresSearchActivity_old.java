package br.com.karlosimoreira.fcvarzea.activitys.Home.Jogador;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.adapter.JogadorAdapter;
import br.com.karlosimoreira.fcvarzea.domain.util.BaseJogadorActivity;
import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;
import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;

public class JogadoresSearchActivity_old extends BaseJogadorActivity implements RecyclerViewOnClickListenerHack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       //Transições
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = inflater.inflateTransition( R.transition.transitions );
            getWindow().setSharedElementExitTransition( transition );
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogadores);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference = LibraryClass.getFirebase();
        getDataForActivity();
        mContext = this;

        if(savedInstanceState != null){
            auxUserList = savedInstanceState.getParcelableArrayList("auxUserList");
        }else {

            queryJogadores(valueTipo);
            auxUserList = getnListUserFirebase(query);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("auxUserList", auxUserList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        Log.i("onResume","init");
        toolbar.setTitle( getResources().getString(R.string.title_activity_summon_players) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(JogadoresSearchActivity_old.this,"Salvar Lista de convocados", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

         rvUsers = (RecyclerView) findViewById(R.id.rvList);

        if (rvUsers != null) {
            rvUsers.setHasFixedSize( true );
            rvUsers.addOnItemTouchListener(new RecyclerViewTouchListener(this, rvUsers,this));
            rvUsers.setLayoutManager( new LinearLayoutManager(this));
            JogadorAdapter adapter = new JogadorAdapter(auxUserList,this);
            rvUsers.setAdapter(adapter);

            mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.srlSwipe);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    RefreshData();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void RefreshData(){
        rvUsers.setHasFixedSize( true );
        rvUsers.addOnItemTouchListener(new RecyclerViewTouchListener(this, rvUsers,this));
        rvUsers.setLayoutManager( new LinearLayoutManager(this));
        JogadorAdapter adapter = new JogadorAdapter(auxUserList,this);
        rvUsers.setAdapter(adapter);
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
        //Toast.makeText(this, "Position: "+ position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailsJogadorTestActivity.class);
        intent.putExtra(DetailsJogadorTestActivity.EXTRA_NAME, auxUserList.get(position));

      // TRANSITIONS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){

            View ivPhoto = view.findViewById(R.id.ivPhoto);
           // View tvName = view.findViewById(R.id.tvName);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    Pair.create( ivPhoto, "elementPhoto"));

            this.startActivity( intent, options.toBundle() );
        }
        else {
            this.startActivity(intent);
        }
        //this.startActivity(intent);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(this, "LongPressPosition: " + position, Toast.LENGTH_SHORT).show();
    }

    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener{

        private Context myContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context mContext, final RecyclerView mRecyclerView,  RecyclerViewOnClickListenerHack rvoclh) {
            this.myContext = mContext;
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
