package br.com.karlosimoreira.fcvarzea.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.activitys.Home.JogadoresSearchActivity;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;
import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;

public class UserNoRecyclerAdapter extends RecyclerView.Adapter<UserNoViewHolder> {

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
    private List<User> mList;
    private List<Bitmap> bList;
    private LayoutInflater layoutInflater;
    private UserNoViewHolder userNoViewHolder;
    public static RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context contextJogadoresSearchActivity;
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    private ImagemProcess ip;

    public UserNoRecyclerAdapter(List<User> mList,List<Bitmap> bList, Context nContext) {
        this.mList = mList;
        this.bList = bList;
        layoutInflater =(LayoutInflater)nContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ip = new ImagemProcess();
    }

    @Override
    public UserNoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.modelo_convocar_jogadores, parent, false);
        userNoViewHolder = new UserNoViewHolder(view);
        contextJogadoresSearchActivity = JogadoresSearchActivity.mContext;
        return userNoViewHolder;
    }

    @Override
    public void onBindViewHolder(UserNoViewHolder holder, int position) {
        User user = mList.get(position);
        popularViews(holder, user, position);
        //for (int i =0; i< mList.size(); i++){
            Log.i("onBindViewHolder", "Name: "+ user.getName() + "\n Position: " + user.getPosition()+ "\n Photo: " + user.getPhoto()+ "\n Classificação: " + user.getClassificacao()+"\nArrayPosition: "+position);
       // }
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    private void popularViews(UserNoViewHolder holder, User user, int positon){
        //Nome
        holder.tvName.setText(user.getName());
        //Posição
        if (user.getPosition() == null ) {
            holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
        } else {
            switch (Integer.parseInt(user.getPosition())) {

                case GOLEIRO:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
                    break;
                case ZAGUEIRO:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_zg));
                    break;
                case LATERAL_ESQUERDO:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_le));
                    break;
                case LATERAL_DIREITO:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_ld));
                    break;
                case MEIA_ESQUERDA:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_me));
                    break;
                case VOLANTE:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_vl));
                    break;
                case MEIA_DIREITA:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_md));
                    break;
                case PONTA_ESQUERDA:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_pe));
                    break;
                case SEGUNDO_ATACANTE:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_sa));
                    break;
                case MEIA_OFENCIVO:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_mo));
                    break;
                case CENTRO_AVANTE:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_ca));
                    break;
                case PONTA_DIREITA:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_pd));
                    break;
                default:
                    holder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
            }
        }
        //Photo
        if( user.getPhoto() != null) {
            Picasso.with(JogadoresSearchActivity.mContext)
                    .load(user.getPhoto())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(userNoViewHolder.ivPhoto);
            //userNoViewHolder.ivPhoto.setImageBitmap(loadBitmap(user.getPhoto()));
       }
        //Classificação
        LayerDrawable stars = (LayerDrawable)userNoViewHolder.rbClassificacao.getProgressDrawable();
        Drawable progress = stars.getDrawable(2);
        DrawableCompat.setTint(progress, Color.YELLOW);
        progress = stars.getDrawable(0);
        DrawableCompat.setTint(progress, Color.GRAY);
        if(user.getClassificacao() == null) {
            holder.rbClassificacao.setRating((float) 0.0);
        }else {
            holder.rbClassificacao.setRating((Float.parseFloat(user.getClassificacao())));
        }
    }

    private Bitmap loadBitmap(String pathPhoto){
        return ip.loadBitmap(pathPhoto);
    }
}