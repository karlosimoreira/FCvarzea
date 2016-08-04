package br.com.karlosimoreira.fcvarzea.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.activitys.Home.JogadoresActivity;
import br.com.karlosimoreira.fcvarzea.domain.App;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.DownloadImageTask;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends FirebaseRecyclerAdapter<User, UserViewHolder> {

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


    public UserRecyclerAdapter(Class<User> modelClass,int modelLayout, Class<UserViewHolder> viewHolderClass, Query ref ){
        super( modelClass, modelLayout, viewHolderClass, ref );
    }

    @Override
    protected void populateViewHolder( UserViewHolder userViewHolder, User user, int position) {

        ImagemProcess ip = new ImagemProcess();
        //Photo
        if(user.getPhoto() != null){
            new DownloadImageTask(userViewHolder.ivPhoto).execute(user.getPhoto());
        }else {
            userViewHolder.ivPhoto.setImageResource(R.drawable.ic_account);
        }

        //Nome
        userViewHolder.tvName.setText(user.getName());

        //Classificação
        LayerDrawable stars = (LayerDrawable)userViewHolder.rbClassificacao.getProgressDrawable();
        Drawable progress = stars.getDrawable(2);
        DrawableCompat.setTint(progress, Color.YELLOW);
        progress = stars.getDrawable(0);
        DrawableCompat.setTint(progress, Color.GRAY);
        if(user.getClassificacao() != null){
            userViewHolder.rbClassificacao.setRating((Float.parseFloat(user.getClassificacao())));
        }else {
            userViewHolder.rbClassificacao.setRating((float)0.0);
        }

        //Posição
        if (user.getPosition() == null) {
            userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
        } else {
            switch (Integer.parseInt(user.getPosition())) {

                case GOLEIRO:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
                    break;
                case ZAGUEIRO:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_zg));
                    break;
                case LATERAL_ESQUERDO:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_le));
                    break;
                case LATERAL_DIREITO:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_ld));
                    break;
                case MEIA_ESQUERDA:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_me));
                    break;
                case VOLANTE:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_vl));
                    break;
                case MEIA_DIREITA:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_md));
                    break;
                case PONTA_ESQUERDA:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_pe));
                    break;
                case SEGUNDO_ATACANTE:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_sa));
                    break;
                case MEIA_OFENCIVO:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_mo));
                    break;
                case CENTRO_AVANTE:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_ca));
                    break;
                case PONTA_DIREITA:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_pd));
                    break;
                default:
                    userViewHolder.ivPosition.setImageBitmap(ip.convetBitmap(R.drawable.ic_position_select_gl));
            }
        }


    }
}