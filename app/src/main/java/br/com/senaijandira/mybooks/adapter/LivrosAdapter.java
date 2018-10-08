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

import br.com.senaijandira.mybooks.Consts;
import br.com.senaijandira.mybooks.R;
import br.com.senaijandira.mybooks.Utils;
import br.com.senaijandira.mybooks.CadastroActivity;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class LivrosAdapter extends ArrayAdapter<Livro> {

    MyBooksDatabase myBooksDb;
    Context mcon;

    public LivrosAdapter(Context ctx) {
        super(ctx, 0, new ArrayList<Livro>());
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
        final ImageView imgEditar = v.findViewById(R.id.imgEditaLivro);
        final ImageView imgLivroLido = v.findViewById(R.id.imgLivroLido);
        final ImageView imgLivroLer = v.findViewById(R.id.imgLivroLer);


        if(livro.getStatus() > 0){
            imgDeleteLivro.setImageResource(R.drawable.baseline_delete_gray_18dp);
            imgLivroLido.setImageResource(R.drawable.baseline_book_gray_18dp);
            imgLivroLer.setImageResource(R.drawable.baseline_chrome_reader_mode_gray_18dp);
        }else{
            imgDeleteLivro.setImageResource(R.drawable.baseline_delete_black_18dp);
            imgLivroLido.setImageResource(R.drawable.baseline_book_black_18dp);
            imgLivroLer.setImageResource(R.drawable.baseline_chrome_reader_mode_black_18dp);
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

        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CadastroActivity.class);
                intent.putExtra("LIVRO", livro.getId());
                getContext().startActivity(intent);
            }
        });

        imgLivroLido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(livro.getStatus() == 0){
                    toast("Adicionado aos livros lidos");
                    livro.setStatus(Consts.LIDO);
                    myBooksDb.daoLivro().atualizar(livro);
                    imgDeleteLivro.setImageResource(R.drawable.baseline_delete_gray_18dp);
                    imgLivroLido.setImageResource(R.drawable.baseline_book_gray_18dp);
                    imgLivroLer.setImageResource(R.drawable.baseline_chrome_reader_mode_gray_18dp);
                }else{
                    toast("Remova o livro da categoria");
                }
            }
        });

        imgLivroLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(livro.getStatus() == 0){
                    toast("Adicionado aos livros à serem lidos");
                    livro.setStatus(Consts.LER);
                    myBooksDb.daoLivro().atualizar(livro);
                    imgDeleteLivro.setImageResource(R.drawable.baseline_delete_gray_18dp);
                    imgLivroLido.setImageResource(R.drawable.baseline_book_gray_18dp);
                    imgLivroLer.setImageResource(R.drawable.baseline_chrome_reader_mode_gray_18dp);
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
