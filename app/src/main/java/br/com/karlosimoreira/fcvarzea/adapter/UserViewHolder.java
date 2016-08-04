package br.com.karlosimoreira.fcvarzea.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.karlosimoreira.fcvarzea.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivPhoto;
    public ImageView ivPosition;
    public TextView tvName;
    //public Button btnDetails;
    public RatingBar rbClassificacao;


    public UserViewHolder(View itemView) {
        super(itemView);
        this.tvName =(TextView)itemView.findViewById(R.id.tvName);
        this.ivPhoto = (ImageView)itemView.findViewById(R.id.ivPhoto);
        this.ivPosition = (ImageView)itemView.findViewById(R.id.ivPosition);
        //this.btnDetails = (Button)itemView.findViewById(R.id.btnDetails);
        this.rbClassificacao = (RatingBar)itemView.findViewById(R.id.rbClassificacao);
    }
}
