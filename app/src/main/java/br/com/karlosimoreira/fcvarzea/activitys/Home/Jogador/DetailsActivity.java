package br.com.karlosimoreira.fcvarzea.activitys.Home.Jogador;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;

public class DetailsActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "user";
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView ivPhoto;
    private Toolbar toolbar;
    private TextView tvIdade;
    private User user;
    private int idade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Salvando o estado da activity
        if(savedInstanceState != null){
            user = savedInstanceState.getParcelable("user");
        }
        else {
            user = getIntent().getExtras().getParcelable("user");
        Log.i("onCreate","user.getBirthDate() "+ user.getBirthDate()+"\n user.getCity() "+user.getCity()+ " \n user.getPeBom() "+ user.getPeBom());

        }
        //Botão de ação Favorito
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tem Nada aki!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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


        ivPhoto = (ImageView)findViewById(R.id.iv_car);
        tvIdade = (TextView)findViewById(R.id.tvIdade);
        Picasso.with(this)
                .load(user.getPhoto())
                .into(ivPhoto);
       // Log.i("Carlos","user.getBirthDate() "+ user.getBirthDate()+"\n user.getPhoto() "+user.getCity()+ " \n user.getName() "+ user.getPeBom());


       // String ms = "Idade: "+ String.valueOf(Uteis.calcularIdade(user.getBirthDate()));
       // tvIdade.setText(ms);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
