package br.com.senaijandira.mybooks.adapter;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.senaijandira.mybooks.Consts;
import br.com.senaijandira.mybooks.R;
import br.com.senaijandira.mybooks.Utils;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class LidosLerAdapter extends ArrayAdapter<Livro> {

    MyBooksDatabase myBooksDb;

    public LidosLerAdapter(Context ctx) {
        super(ctx, 0, new ArrayList<Livro>());
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        myBooksDb = Room.databaseBuilder(getContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.lidosler_layout, parent, false);
        }

        final Livro livro = getItem(position);

        ImageView imgLivroCapa = v.findViewById(R.id.imgLivroCapa);
        imgLivroCapa.setImageBitmap(Utils.toBitmap(livro.getCapa()));

        TextView txtLivroTitulo = v.findViewById(R.id.txtLivroTitulo);
        txtLivroTitulo.setText(livro.getTitulo());

        TextView txtLivroDescricao = v.findViewById(R.id.txtLivroDescricao);
        txtLivroDescricao.setText(livro.getDescricao());

        final ImageView imgRemoverCategoria = v.findViewById(R.id.imgRemoverCategoria);

        imgRemoverCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("Removido da categoria");
                livro.setStatus(Consts.SEM_STATUS);
                myBooksDb.daoLivro().atualizar(livro);
                remove(livro);
            }
        });

        return v;

    }

    private void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
