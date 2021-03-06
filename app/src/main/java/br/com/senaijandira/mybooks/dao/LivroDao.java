package br.com.senaijandira.mybooks.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import br.com.senaijandira.mybooks.model.Livro;
import br.com.senaijandira.mybooks.Consts;

@Dao
public interface LivroDao {

    @Insert
    void inserir(Livro l);

    @Update
    void atualizar(Livro l);

    @Delete
    void deletar(Livro l);

    @Query("SELECT * FROM livro")
    Livro[] selecionarTodos();

    @Query("SELECT * FROM livro WHERE status = " + Consts.LIDO)
    Livro[] selecionarTodosLidos();

    @Query("SELECT * FROM livro WHERE status = " + Consts.LER)
    Livro[] selecionarTodosNaoLidos();

    @Query("SELECT * FROM livro WHERE id = :idLivro")
    Livro pegarLivro(int idLivro);
}
