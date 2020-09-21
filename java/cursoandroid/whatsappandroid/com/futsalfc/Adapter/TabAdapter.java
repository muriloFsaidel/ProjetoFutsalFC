package cursoandroid.whatsappandroid.com.futsalfc.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import cursoandroid.whatsappandroid.com.futsalfc.fragment.EquipesFragment;
import cursoandroid.whatsappandroid.com.futsalfc.fragment.JogosFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"EQUIPES","JOGOS"};

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    // posição do item da aba
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch(position){

            case 0:
                fragment = new EquipesFragment();
                break;

            case 1:
                fragment = new JogosFragment();
                break;
        }
        return fragment;
    }

    //quantidade de abas
    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    //retorna o título da aba baseado no vetor
    @Override
    public CharSequence getPageTitle(int position){
        return tituloAbas[position];
    }
}
