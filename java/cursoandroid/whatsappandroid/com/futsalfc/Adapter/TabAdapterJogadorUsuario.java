package cursoandroid.whatsappandroid.com.futsalfc.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import cursoandroid.whatsappandroid.com.futsalfc.fragment.DadosJogadorUsuario;
import cursoandroid.whatsappandroid.com.futsalfc.fragment.JogosEquipeJogadorUsuario;

public class TabAdapterJogadorUsuario extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"JOGADOR","JOGOS"};

    public TabAdapterJogadorUsuario(@NonNull FragmentManager fm){
        super(fm);
    }

    @Override
    @NonNull
    //retorna a posição do item da aba
    public Fragment getItem(int position){
        Fragment fragment = null;

        switch(position){

            case 0:
                fragment = new DadosJogadorUsuario();
                break;

            case 1:
                fragment = new JogosEquipeJogadorUsuario();
                break;

        }
        return fragment;
    }

    //quantidade de abas
    @Override
    public int getCount(){return tituloAbas.length;}

    //retorna o título da aba baseado no vetor
    @Override
    public CharSequence getPageTitle(int position){ return tituloAbas[position];}
}
