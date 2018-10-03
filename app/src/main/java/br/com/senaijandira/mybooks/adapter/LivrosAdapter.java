package br.com.senaijandira.mybooks.adapter;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
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

import br.com.senaijandira.mybooks.R;
import br.com.senaijandira.mybooks.Utils;
import br.com.senaijandira.mybooks.cadastroActivity;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class LivrosAdapter extends ArrayAdapter<Livro> {

    MyBooksDatabase myBooksDb;
    Context mcon;

    public LivrosAdapter(Context ctx) {
        super(ctx, 0, new ArrayList<Livro>());
        mcon = ctx;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        myBooksDb = Room.databaseBuilder(getContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.livro_layout, parent, false);
        }

        final Livro livro = getItem(position);

        ImageView imgLivroCapa = v.findViewById(R.id.imgLivroCapa);
        imgLivroCapa.setImageBitmap(Utils.toBitmap(livro.getCapa()));

        TextView txtLivroTitulo = v.findViewById(R.id.txtLivroTitulo);
        txtLivroTitulo.setText(livro.getTitulo());

        TextView txtLivroDescricao = v.findViewById(R.id.txtLivroDescricao);
        txtLivroDescricao.setText(livro.getDescricao());

        final ImageView imgDeleteLivro = v.findViewById(R.id.imgDeleteLivro);
        if(livro.getStatus() > 0){
            imgDeleteLivro.setImageResource(R.drawable.baseline_delete_grey_18dp);
        }
        imgDeleteLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(livro.getStatus() == 0){
                    myBooksDb.daoLivro().deletar(livro);
                    remove(livro);
                    toast("Livro Removido");
                } else {
                    toast("Remova o livro da categoria");
                }
            }
        });

        ImageView imgEditar = v.findViewById(R.id.imgEditaLivro);
        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), cadastroActivity.class);
                intent.putExtra("LIVRO", livro.getId());
                mcon.startActivity(intent);
            }
        });

        ImageView imgLivroLido = v.findViewById(R.id.imgLivroLido);
        imgLivroLido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(livro.getStatus() == 0){
                    toast("Adicionado aos livros lidos");
                    livro.setStatus(1);
                    imgDeleteLivro.setImageResource(R.drawable.baseline_delete_grey_18dp);
                    myBooksDb.daoLivro().atualizar(livro);
                }else{
                    toast("Remova o livro da categoria");
                }
            }
        });

        return v;

    }

    private void toast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
