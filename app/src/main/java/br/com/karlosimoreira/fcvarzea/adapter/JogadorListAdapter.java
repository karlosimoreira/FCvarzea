package br.com.karlosimoreira.fcvarzea.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.activitys.Home.DetailsJogadorTestActivity;
import br.com.karlosimoreira.fcvarzea.activitys.Home.Jogador.JogadoresListActivity;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.Animation;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;
import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos on 03/08/2016.
 */
public class JogadorListAdapter extends RecyclerView.Adapter<JogadorListAdapter.jogadorViewHolder> {

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    public final int GOLEIRO = 1;
    public final int ZAGUEIRO = 2;
    public final int LATERAL_DIREITO = 3;
    public final int LATERAL_ESQUERDO = 4;
    public final int VOLANTE = 5 ;
    public final int MEIA_ESQUERDA =6;
    public final int MEIA_DIREITA = 7;
    public final int PONTA_ESQUERDA = 8;
    public final int SEGUNDO_ATACANTE = 9;
    public final int MEIA_OFENCIVO = 10;
    public final int CENTRO_AVANTE = 11;
    public final int PONTA_DIREITA = 12;
    public static RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private JogadoresListActivity mJogadoresListActivity;

    private ImagemProcess ip;

    private ArrayList<User> mJogadores = new ArrayList<>();
    private Context mContext;

    public JogadorListAdapter(ArrayList<User> mJogadores, Context mContext) {
        this.mJogadores = mJogadores;
        this.mContext = mContext;
        ip = new ImagemProcess();
    }

    @Override
    public jogadorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_convocar_jogadores, parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.convocar_jogadores, parent, false);
        return new jogadorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(jogadorViewHolder holder, int position) {
        Animation.setAnimation(holder.itemView,700, Techniques.Tada);
        User user = mJogadores.get(position);

        Picasso.with(mContext)
                .load(user.getPhoto())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(holder.mIvPhoto);

        holder.mTvName.setText(user.getName());

        if (user.getPosition() == null ) {
            holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
        } else {
            switch (Integer.parseInt(user.getPosition())) {

                case GOLEIRO:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
                    break;
                case ZAGUEIRO:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_zg));
                    break;
                case LATERAL_ESQUERDO:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_le));
                    break;
                case LATERAL_DIREITO:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_ld));
                    break;
                case MEIA_ESQUERDA:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_me));
                    break;
                case VOLANTE:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_vl));
                    break;
                case MEIA_DIREITA:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_md));
                    break;
                case PONTA_ESQUERDA:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_pe));
                    break;
                case SEGUNDO_ATACANTE:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_sa));
                    break;
                case MEIA_OFENCIVO:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_mo));
                    break;
                case CENTRO_AVANTE:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_ca));
                    break;
                case PONTA_DIREITA:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_pd));
                    break;
                default:
                    holder.mIvPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
            }
        }
        LayerDrawable stars = (LayerDrawable)holder.mRbClassificacao.getProgressDrawable();
        Drawable progress = stars.getDrawable(2);
        DrawableCompat.setTint(progress, Color.YELLOW);
        progress = stars.getDrawable(0);
        DrawableCompat.setTint(progress, Color.GRAY);
        if(user.getClassificacao() == null) {
            holder.mRbClassificacao.setRating((float) 0.0);
        }else {
            holder.mRbClassificacao.setRating((Float.parseFloat(user.getClassificacao())));
        }
        mJogadoresListActivity = new JogadoresListActivity();
        holder.setRecyclerViewOnClickListenerHack(new RecyclerViewOnClickListenerHack() {
            @Override
            public void onClickListener(View view, int position) {
                Toast.makeText(mContext,mJogadores.get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailsJogadorTestActivity.class);
                intent.putExtra(DetailsJogadorTestActivity.EXTRA_NAME, mJogadores.get(position));
                mContext.startActivity(intent);
            }

            @Override
            public void onLongPressClickListener(View view, int position) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mJogadores.size();
    }

    public class jogadorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.ivPhoto) ImageView mIvPhoto;
        @Bind(R.id.ivPosition) ImageView mIvPosition;
        @Bind(R.id.tvName) TextView mTvName;
        @Bind(R.id.rbClassificacao) RatingBar mRbClassificacao;

        private Context mContext;

        public jogadorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            if (mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v,itemPosition);
            }
        }
        public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
            mRecyclerViewOnClickListenerHack = r;
        }
    }



}
