package br.com.senaijandira.mybooks.fragment;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.senaijandira.mybooks.R;
import br.com.senaijandira.mybooks.Utils;
import br.com.senaijandira.mybooks.adapter.LidosLerAdapter;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class LerFragment extends Fragment {

    ListView lstViewLivros;
    LidosLerAdapter adapter;
    MyBooksDatabase appDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_lidosler_fragment, container, false);

        appDB = Room.databaseBuilder(getContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        lstViewLivros = view.findViewById(R.id.lstViewLivros);

        adapter = new LidosLerAdapter(getContext());

        lstViewLivros.setAdapter(adapter);

        Livro[] livros = appDB.daoLivro().selecionarTodosNaoLidos();

        adapter.addAll(livros);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.clear();

        Livro[] livros = appDB.daoLivro().selecionarTodosNaoLidos();

        adapter.addAll(livros);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}