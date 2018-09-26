package br.com.senaijandira.mybooks;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class MainActivity extends AppCompatActivity {

    LinearLayout listaLivros;

    public static Livro[] livros;

    private MyBooksDatabase myBooksDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBooksDb = Room.databaseBuilder(getApplicationContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        listaLivros = findViewById(R.id.listaLivros);

    }

    @Override
    protected void onResume() {
        super.onResume();

        listaLivros.removeAllViews();

        //Faz um select que retorna todos os livros do banco
        livros = myBooksDb.daoLivro().selecionarTodos();

        for(Livro l : livros){
            criarLivro(l, listaLivros);
        }

    }

    private void deletarLivro(Livro livro, View v){

        myBooksDb.daoLivro().deletar(livro);
        listaLivros.removeView(v);

    }

    public void criarLivro(final Livro livro, ViewGroup root){
        final View v = LayoutInflater.from(this).inflate(R.layout.livro_layout, root, false);

        ImageView imgLivroCapa = v.findViewById(R.id.imgLivroCapa);
        TextView txtLivroTitulo = v.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = v.findViewById(R.id.txtLivroDescricao);

        ImageView imgDeleteLivro = v.findViewById(R.id.imgDeleteLivro);

        imgDeleteLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletarLivro(livro, v);
            }
        });

        imgLivroCapa.setImageBitmap(Utils.toBitmap(livro.getCapa()));
        txtLivroTitulo.setText(livro.getTitulo());
        txtLivroDescricao.setText(livro.getDescricao());

        //Exibir na tela
        root.addView(v);
    }

    public void abrirCadastro(View v){
        startActivity(new Intent(this, cadastroActivity.class));

    }

    public void abrirAba(View v){
        startActivity(new Intent(this, Main2Activity.class));
    }
}
