package br.com.karlosimoreira.fcvarzea.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;

import java.util.ArrayList;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.util.Animation;
import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Carlos on 19/08/2016.
 */
public class ArenaAdapter extends RecyclerView.Adapter<ArenaAdapter.ArenaViewHolder>{
    public static RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private ArrayList<String> mArenas = new ArrayList<>();
    private Context mContext;

    public ArenaAdapter(ArrayList<String> mArenas, Context mContext) {
        this.mArenas = mArenas;
        this.mContext = mContext;
    }

    @Override
    public ArenaAdapter.ArenaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_arenas, parent, false);
        return new ArenaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArenaAdapter.ArenaViewHolder holder, int position) {
        Animation.setAnimation(holder.itemView,700, Techniques.Tada);
        holder.mTvArena.setText(mArenas.get(position));
        holder.setRecyclerViewOnClickListenerHack(new RecyclerViewOnClickListenerHack() {
            @Override
            public void onClickListener(View view, int position) {
                Toast.makeText(mContext,mArenas.get(position),Toast.LENGTH_SHORT).show();
                view.setSelected(true);
            }
            @Override
            public void onLongPressClickListener(View view, int position) {
                Toast.makeText(mContext,mArenas.get(position)+ " foi Selecionada",Toast.LENGTH_SHORT).show();
                view.setSelected(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArenas.size();
    }
    public class ArenaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.tvArena)
        TextView mTvArena;

        private Context mContext;

        public ArenaViewHolder(View itemView) {
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

