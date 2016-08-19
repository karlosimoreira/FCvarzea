package br.com.karlosimoreira.fcvarzea.activitys.Home.Arena;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.adapter.ArenaAdapter;

public class ArenasActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arenas);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    private void init(){
        toolbar.setTitle(getResources().getString(R.string.title_activity_summon_arenas));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(ArenasActivity.this, "Salvar arena", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_view:
                         Toast.makeText(ArenasActivity.this, "Trocar view", Toast.LENGTH_SHORT).show();
                         SetRecycleView();
                        break;
                }

                return true;
            }
        });
        SetRecycleView();
    }
    private void SetRecycleView(){
        RecyclerView rvArenas = (RecyclerView) findViewById(R.id.rvListArenas);

        if (rvArenas != null) {
            rvArenas.setHasFixedSize( true );
            rvArenas.setLayoutManager( new LinearLayoutManager(this));
            ArenaAdapter adapter = new ArenaAdapter(setListArenas(),this);
            rvArenas.setAdapter(adapter);
        }
    }

    private ArrayList<String>setListArenas(){
        ArrayList<String> l = new ArrayList<>();
        l.add("Arena das arenas");
        l.add("Poliesportivo dantas");
        l.add("Quadra do manuel");
        l.add("Cantinho da galera");
        l.add("To no trabalho");
        l.add("Bola na rede");
        l.add("Rei pelé");
        l.add("Cruzeiro do sul");
        l.add("Arena das garças");
        l.add("Parque poliesportivo islamico");
        l.add("Quadra do jé");
        l.add("Arena ney city");
        l.add("Quadra brasil");
        l.add("Arema CPMF");

        return l;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_search_actions, menu);
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
