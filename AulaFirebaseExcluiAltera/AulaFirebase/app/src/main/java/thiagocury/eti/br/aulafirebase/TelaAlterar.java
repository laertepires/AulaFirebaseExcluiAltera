package thiagocury.eti.br.aulafirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TelaAlterar extends AppCompatActivity {

    //Widgets
    private EditText etNome;
    private EditText etIdade;
    private Button btnAlterar;
    private Button btnCancelar;

    private Pessoa p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_alterar);

        //Referencias
        etNome = (EditText) findViewById(R.id.ta_et_nome);
        etIdade = (EditText) findViewById(R.id.ta_et_idade);
        btnAlterar = (Button) findViewById(R.id.ta_btn_alterar);
        btnCancelar = (Button) findViewById(R.id.ta_btn_cancelar);

        //Firebase
        FirebaseApp.initializeApp(TelaAlterar.this);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference banco = db.getReference("contatos");

        //Pegando o valor da intent para ser alterado
        if(getIntent().getSerializableExtra("p") != null) {
            p = (Pessoa) getIntent().getSerializableExtra("p");
            etNome.setText(p.getNome());
            etIdade.setText(String.valueOf(p.getIdade()));
        }//fecha if

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pessoa p1 = new Pessoa();
                p1.setNome(etNome.getText().toString());
                p1.setIdade(Integer.parseInt(etIdade.getText().toString()));

                //Alterando através da chave(key) no firebase setando o novo valor
                banco.child(p.getKey()).setValue(p1);

                Toast.makeText(getBaseContext(), "Pessoa alterada com sucesso!", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }//fecha onCreate
}//fecha classe
