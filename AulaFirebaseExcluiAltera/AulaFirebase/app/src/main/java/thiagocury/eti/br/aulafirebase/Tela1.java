package thiagocury.eti.br.aulafirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tela1 extends AppCompatActivity {

    //Widgets
    private EditText etNome;
    private EditText etIdade;
    private Button btnOK;
    private ListView lvPessoas;
    //Objeto
    private Pessoa p;
    //Array + Adapter
    private ArrayAdapter<Pessoa> adapter;
    private ArrayList<Pessoa> pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);

        //Referencias
        etNome = (EditText) findViewById(R.id.t1_et_nome);
        etIdade = (EditText) findViewById(R.id.t1_et_idade);
        btnOK = (Button) findViewById(R.id.t1_btn_ok);
        lvPessoas = (ListView) findViewById(R.id.t1_lv_pessoas);

        //Array + Adapters
        pessoas = new ArrayList<>(); //vazio!
        adapter = new ArrayAdapter<Pessoa>(
                        Tela1.this,
                        android.R.layout.simple_list_item_1,
                        pessoas);
        lvPessoas.setAdapter(adapter);

        //Firebase
        FirebaseApp.initializeApp(Tela1.this);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference banco = db.getReference("contatos");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = new Pessoa();
                p.setNome(etNome.getText().toString());
                p.setIdade(
                        Integer.parseInt(etIdade.getText().toString()));
                //Enviar(empurrar) para o firebase
                banco.push().setValue(p);
                Toast.makeText(
                        getBaseContext(),
                        "Pessoa cadastrado com sucesso!",
                        Toast.LENGTH_LONG).show();
            }//fecha onclick
        });//fecha listener


        banco.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pessoas.clear(); //Joinha do Macgyver
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Pessoa p = data.getValue(Pessoa.class);
                    p.setKey(data.getKey()); //Colocando key manualmente no objeto
                    pessoas.add(p);
                }//fecha for
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        lvPessoas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //objeto que tem a key
                Pessoa p = pessoas.get(i);

                Intent it = new Intent(Tela1.this, TelaAlterar.class);
                it.putExtra("p",p);
                startActivity(it);
            }
        });

        lvPessoas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {

                final int posSelec = i;

                AlertDialog.Builder msg = new AlertDialog.Builder(Tela1.this);
                msg.setTitle("Confirmação!");
                msg.setMessage("Você tem certeza que deseja excluir?");
                msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Pessoa p = pessoas.get(posSelec);

                        //Removendo através da chave(key) no firebase
                        banco.child(p.getKey()).removeValue();

                        pessoas.remove(posSelec);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(),"Pessoa excluída com sucesso!", Toast.LENGTH_LONG).show();
                    }
                });
                msg.setNegativeButton("Não", null);
                msg.show();
                return true;
            }
        });
    }//fecha onCreate
}//fecha classe
