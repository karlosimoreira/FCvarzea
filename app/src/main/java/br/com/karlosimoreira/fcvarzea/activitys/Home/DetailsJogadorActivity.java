package br.com.karlosimoreira.fcvarzea.activitys.Home;

import android.os.Build;
import android.os.Bundle;
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

public class DetailsJogadorActivity extends AppCompatActivity {
    private static final int MAX_WIDTH = 500;
    private static final int MAX_HEIGHT = 500;
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
        setContentView(R.layout.activity_details_jogador);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            user = savedInstanceState.getParcelable("user");
        }
        else {
            user = getIntent().getExtras().getParcelable("user");
        }


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
        toolbar.setTitle( user.getName()) ;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPhoto = (ImageView)findViewById(R.id.ivUser);
        Picasso.with(this)
                .load(user.getPhoto())
                .into(ivPhoto);
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
