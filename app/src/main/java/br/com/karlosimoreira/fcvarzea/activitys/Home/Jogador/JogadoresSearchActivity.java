package br.com.karlosimoreira.fcvarzea.activitys.Home.Jogador;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.adapter.JogadorAdapter;
import br.com.karlosimoreira.fcvarzea.domain.util.BaseJogadorActivity;
import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;

public class JogadoresSearchActivity extends BaseJogadorActivity {

    public static int selectLayout;
    private boolean layout = true;
    private MenuItem viewLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Transições
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = inflater.inflateTransition( R.transition.transitions );
            getWindow().setSharedElementExitTransition( transition );
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogador_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selectLayout = R.layout.modelo_convocar_jogadores;
        databaseReference = LibraryClass.getFirebase();
        mContext = this;
        getDataForActivity();


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
        for (int i =0; i<auxUserList.size(); i++) {
            Log.i("Tag", "Name: " + auxUserList.get(i).getName() + "\n BirthDate: " + auxUserList.get(i).getBirthDate() + "\n PeBom: " + auxUserList.get(i).getPeBom());
        }
        outState.putParcelableArrayList("auxUserList", auxUserList);

    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        Log.i("onResume", "init");
        toolbar.setTitle(getResources().getString(R.string.title_activity_summon_players));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(JogadoresSearchActivity.this, "Salvar Lista de convocados", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_view:
                        Toast.makeText(JogadoresSearchActivity.this, "Trocar view", Toast.LENGTH_SHORT).show();
                        if (layout){
                            selectLayout = R.layout.convocar_jogadores;
                            viewLayout.setIcon(R.mipmap.ic_view_module);
                            viewLayout.setTitle(R.string.action_view_cards);
                            SetRecycleView();
                            layout = false;
                        }else {
                            selectLayout = R.layout.modelo_convocar_jogadores;
                            viewLayout.setIcon(R.mipmap.ic_view_list);
                            viewLayout.setTitle(R.string.action_view_list);
                            SetRecycleView();
                            layout = true;
                        }
                        break;
                }
                return true;
            }
        });
        SetRecycleView();
    }
    private void SetRecycleView(){
        rvUsers = (RecyclerView) findViewById(R.id.rvList);

        if (rvUsers != null) {
            rvUsers.setHasFixedSize( true );
            rvUsers.setLayoutManager( new LinearLayoutManager(this));
            JogadorAdapter adapter = new JogadorAdapter(auxUserList,this);
            rvUsers.setAdapter(adapter);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_search_actions, menu);
        viewLayout =menu.findItem(R.id.action_view);
        return super.onCreateOptionsMenu(menu);
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
