package br.com.senaijandira.mybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;

public class cadastroActivity extends AppCompatActivity {

    Bitmap livroCapa;
    ImageView imgLivroCapa;
    EditText txtTitulo, txtDescricao;
    boolean erro = true;
    int idLivro;

    private final int COD_REQ_GALERIA = 101;

    private MyBooksDatabase myBooksDb;

    private final int ERRO = 0;
    private final int SUCESSO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        myBooksDb = Room.databaseBuilder(getApplicationContext(), MyBooksDatabase.class, Utils.DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();

        imgLivroCapa = findViewById(R.id.imgLivroCapa);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescricao = findViewById(R.id.txtDescricao);

        idLivro = getIntent().getIntExtra("LIVRO", 0);
        if(idLivro > 0){
            Livro livro = myBooksDb.daoLivro().pegarLivro(idLivro);
            imgLivroCapa.setImageBitmap(Utils.toBitmap(livro.getCapa()));
            livroCapa = BitmapFactory.decodeByteArray(livro.getCapa(), 0, livro.getCapa().length);
            txtTitulo.setText(livro.getTitulo());
            txtDescricao.setText(livro.getDescricao());
            erro = false;
        }

    }

    public void abrirGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult( Intent.createChooser(intent, "Selecione uma imagem"), COD_REQ_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == COD_REQ_GALERIA && resultCode == Activity.RESULT_OK){

            try {
                InputStream input = getContentResolver().openInputStream(data.getData());
                livroCapa = BitmapFactory.decodeStream(input);
                imgLivroCapa.setImageBitmap(livroCapa);
                erro = false;
            }catch (Exception ex){
                ex.printStackTrace();
                erro = true;
            }

        }

    }

    public void salvarLivro(View view) {
        String titulo = txtTitulo.getText().toString();
        String descricao = txtDescricao.getText().toString();
        if(!erro && !titulo.equals("") && !descricao.equals("")){
            byte[] capa = Utils.toByteArray(livroCapa);
            Livro livro = new Livro(idLivro, capa, titulo, descricao);
            if(idLivro > 0){
                myBooksDb.daoLivro().atualizar(livro);
            } else {
                myBooksDb.daoLivro().inserir(livro);
            }
            alert(SUCESSO);
        } else {
            alert(ERRO);
        }

    }

    private void alert(int CONDICAO){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setCancelable(false);

        if(CONDICAO == ERRO){
            alert.setTitle("ERRO");
            alert.setMessage("Preencha todos os campos e selecion a imagem.");
            alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }else if(CONDICAO == SUCESSO){
            alert.setTitle("Sucesso");
            alert.setMessage("Livro salvo com sucesso.");
            alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            });
        }

        alert.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }
}
