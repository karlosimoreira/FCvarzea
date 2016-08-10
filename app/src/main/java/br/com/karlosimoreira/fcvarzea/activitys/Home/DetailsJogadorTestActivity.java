package br.com.karlosimoreira.fcvarzea.activitys.Home;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;

public class DetailsJogadorTestActivity extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView ivPhoto;
    private Toolbar toolbar;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Transições
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            TransitionInflater inflater = TransitionInflater.from( this );
            Transition transition = inflater.inflateTransition( R.transition.transitions );
            getWindow().setSharedElementExitTransition( transition );
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_jogador_test);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Salvando o estado da activity
        if(savedInstanceState != null){
            user = savedInstanceState.getParcelable("user");
        }
        else {
            user = getIntent().getExtras().getParcelable("user");
        }


        //Botão de ação Favorito
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tem Nada aki!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("user", user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    private void init(){
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(user.getName() );
        toolbar.setTitle( user.getName()) ;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        ivPhoto = (ImageView)findViewById(R.id.ivUser);
        Picasso.with(this)
                .load(user.getPhoto())
                .into(ivPhoto);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
