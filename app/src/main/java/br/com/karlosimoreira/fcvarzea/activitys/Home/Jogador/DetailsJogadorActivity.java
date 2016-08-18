package br.com.karlosimoreira.fcvarzea.activitys.Home.Jogador;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.Positions;
import br.com.karlosimoreira.fcvarzea.domain.util.Uteis;

public class DetailsJogadorActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "user";
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView ivPhoto;
    private Toolbar toolbar;
    private User user;
    private TextView tvIdade;
    private TextView tvReside;
    private TextView tvEmail;
    private TextView tvTelefone;
    private TextView tvPosicao;
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
        //changeCollapsedTitleTextColor(mCollapsingToolbarLayout);
        mCollapsingToolbarLayout.setTitle(user.getName() );

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        tvPosicao= (TextView)findViewById(R.id.tvPosicao);
        tvEmail= (TextView)findViewById(R.id.tvEmail);
        tvTelefone= (TextView)findViewById(R.id.tvTelefone);
        tvIdade= (TextView)findViewById(R.id.tvIdade);
        tvReside = (TextView) findViewById(R.id.tvReside);
        ivPhoto = (ImageView)findViewById(R.id.ivPhoto);
        Picasso.with(this)
                .load(user.getPhoto())
                .into(ivPhoto);

        String infoIdade = "Idade: "+String.valueOf(Uteis.calculaIdade(user.getBirthDate(), "dd/MM/yyyy"));
        tvIdade.setText(infoIdade);
        String infoReside = "Reside em: "+ user.getCity()+"/"+user.getState();
        tvReside.setText(infoReside);
        String infoEmail = "Email: "+user.getEmail();
        tvEmail.setText(infoEmail);
        String infoTelefone = "Telefone: "+ user.getPhone();
        tvTelefone.setText(infoTelefone);
        //tvPosicao.setText(getPosition(Integer.parseInt(user.getPosition())));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void changeCollapsedTitleTextColor(CollapsingToolbarLayout collapsingToolbarLayout) {
        try {
            final Field field = collapsingToolbarLayout.getClass().getDeclaredField("mCollapsingTextHelper");
            field.setAccessible(true);

            final Object object = field.get(collapsingToolbarLayout);
            final Field tpf = object.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            ((TextPaint) tpf.get(object)).setColor(getResources().getColor(R.color.colorPrimary));
        } catch (Exception ignored) {
        }
    }
    public String getPosition(Positions p){

        String result;
        switch (p) {

            case GOLEIRO:

                result = "GOLEIRO";
                break;
            case ZAGUEIRO:

                result = "ZAGUEIRO";
                break;
            case LATERAL_ESQUERDO:

                result = "LATERAL ESQUERDO";
                break;
            case LATERAL_DIREITO:
                result = "LATERAL DIREITO";
                break;
            case MEIA_ESQUERDA:
                result = "MEIA ESQUERDA";
                break;
            case VOLANTE:
                result = "VOLANTE";
                break;
            case MEIA_DIREITA:
                result = "MEIA DIREITA";
                break;
            case PONTA_ESQUERDA:
                result = "PONTA ESQUERDA";
                break;
            case SEGUNDO_ATACANTE:
                result = "SEGUNDO ATACANTE";
                break;
            case MEIA_OFENCIVO:
                result = "MEIA OFENCIVO";
                break;
            case CENTRO_AVANTE:
                result = "CENTRO AVANTE";
                break;
            case PONTA_DIREITA:
                result = "PONTA DIREITA";
                break;
            default:
                result = "GOLEIRO";

        }

        return result;
    }
}
