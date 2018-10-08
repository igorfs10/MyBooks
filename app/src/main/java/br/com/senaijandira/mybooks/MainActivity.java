package br.com.senaijandira.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.senaijandira.mybooks.adapter.AbasAdapter;
import br.com.senaijandira.mybooks.fragment.LerFragment;
import br.com.senaijandira.mybooks.fragment.LidoFragment;
import br.com.senaijandira.mybooks.fragment.LivroFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AbasAdapter adapter = new AbasAdapter( getSupportFragmentManager() );
        adapter.adicionar( new LivroFragment() , "Livros");
        adapter.adicionar( new LerFragment(), "Lerei");
        adapter.adicionar( new LidoFragment() , "Lerido");


        ViewPager viewPager = (ViewPager) findViewById(R.id.abas_view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.abas);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void abrirCadastro(View v){
        startActivity(new Intent(this, CadastroActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}