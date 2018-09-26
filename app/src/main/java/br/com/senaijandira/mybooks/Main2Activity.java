package br.com.senaijandira.mybooks;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        AbasAdapter adapter = new AbasAdapter( getSupportFragmentManager() );
        adapter.adicionar( new PrimeiroFragment() , "Primeira Aba");
        adapter.adicionar( new PrimeiroFragment() , "Primeira Aba");


        ViewPager viewPager = (ViewPager) findViewById(R.id.abas_view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.abas);
        tabLayout.setupWithViewPager(viewPager);
    }
}