package br.com.karlosimoreira.fcvarzea.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;

public class UserNoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView ivPhoto;
    public ImageView ivPosition;
    public TextView tvName;
    //public Button btnDetails;
    public RatingBar rbClassificacao;
    public RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


    public UserNoViewHolder(View itemView) {
        super(itemView);
        this.tvName =(TextView)itemView.findViewById(R.id.tvName);
        this.ivPhoto = (ImageView)itemView.findViewById(R.id.ivPhoto);
        this.ivPosition = (ImageView)itemView.findViewById(R.id.ivPosition);
        //this.btnDetails = (Button)itemView.findViewById(R.id.btnDetails);
        this.rbClassificacao = (RatingBar)itemView.findViewById(R.id.rbClassificacao);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mRecyclerViewOnClickListenerHack = UserNoRecyclerAdapter.mRecyclerViewOnClickListenerHack;
        if (mRecyclerViewOnClickListenerHack != null){
            mRecyclerViewOnClickListenerHack.onClickListener(v,getPosition());
        }
    }
}
