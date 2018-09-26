package br.com.senaijandira.mybooks;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class LivroFragment extends Fragment {

    ListView lstViewLivros;
    LivrosAdapter adapter;
    MyBooksDatabase appDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_livro_fragment, container, false);

        appDB = Room.databaseBuilder(getContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        lstViewLivros = view.findViewById(R.id.lstViewLivros);

        adapter = new LivrosAdapter(getContext());

        lstViewLivros.setAdapter(adapter);

        Livro[] livros = appDB.daoLivro().selecionarTodos();

        adapter.addAll(livros);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.clear();

        Livro[] livros = appDB.daoLivro().selecionarTodos();

        adapter.addAll(livros);
    }
}
