package br.com.karlosimoreira.fcvarzea.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.karlosimoreira.fcvarzea.fragments.profilerFragments.LinkAccountsFragment;
import br.com.karlosimoreira.fcvarzea.fragments.profilerFragments.RemoveAccountsFragment;
import br.com.karlosimoreira.fcvarzea.fragments.profilerFragments.UpdatePasswordFragment;

/**
 * Created by Carlos on 06/07/2016.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Context nContext;
    private String[] titles = {"CONECTAR CONTAS", "ATUALIZAR SENHA", "REMOVER CONTA"};

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        nContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        if(position == 0){
            frag = new LinkAccountsFragment();
        }
        else if(position == 1){
            frag = new UpdatePasswordFragment();
        }
        else if(position == 2){
            frag = new RemoveAccountsFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frag.setArguments(bundle);

        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
         super.getPageTitle(position);
        return (titles[position]);
    }
}
