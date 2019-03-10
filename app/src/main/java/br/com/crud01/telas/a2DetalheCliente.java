package br.com.crud01.telas;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.crud01.R;
import br.com.crud01.dados.Cliente;

public class a2DetalheCliente extends AppCompatActivity {
    //Classe - Firebase
    private FirebaseAuth        fireAuth            = FirebaseAuth.getInstance();       //Autenticação Instanciado;
    private FirebaseDatabase    fireDados           = FirebaseDatabase.getInstance();   //Banco Dados Instanciado;
    private DatabaseReference   fireRefAlterar;                                         //Referencia BD Para Alterar;
    private DatabaseReference   fireRefRemover;                                         //Referencia BD Para Remover;
    private ValueEventListener  fireOuvinteValueAlterar;
    private ChildEventListener  fireChilListener;                                       //Ouvinte Child (Listas, Não For, Etc);

    //Classe - Dados e Objetos Lista
    private Cliente cliente;                                                            // Classe do Array do Item passado
    private EditText nome, email, senha, dataNiver, celular;
    private ImageView imageView;

    //Método - Inicial
    @Override protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState); setContentView(R.layout.detalhe_cliente);
        //Identificar Objetos
        nome        =  (EditText)  findViewById( R.id.nomeXml );
        email       =  (EditText)  findViewById( R.id.emailXml );
        senha       =  (EditText)  findViewById( R.id.senhaXml );
        dataNiver   =  (EditText)  findViewById( R.id.dataNiverXml );
        celular     =  (EditText)  findViewById( R.id.celularXml );
        imageView   =  (ImageView) findViewById(R.id.imageView);

        //Receber Dados (Item da Lista Clicado) da Tela Anterior
        cliente = getIntent().getParcelableExtra( "ItemCliente" );

        //Colocar o Registro nos Campos
        nome.setText( cliente.getClienteNome() );
        email.setText( cliente.getClienteEmail() );
        senha.setText( cliente.getClienteSenha() );
        dataNiver.setText( cliente.getClienteNiver() );
        celular.setText( cliente.getClienteCel() );
    }

    //Método - Alterar
    public void btnAlterar (View view){
        //Nó Para Pegar Dados e Tipo de Ouvindo
        fireRefAlterar = fireDados.getReference("Clientes").child( cliente.getClienteId() );
        ValueEventListener fireOuvinteValueAlterar = new ValueEventListener() {

            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Pegar Dados e Converter Para String
                String nomeS        = nome.getText().toString();
                String emailS       = email.getText().toString();
                String senhaS       = senha.getText().toString();
                String dataNiverS   = dataNiver.getText().toString();
                String celularS     = celular.getText().toString();

                //Instanciar Classe e Passar Valores
                cliente = new Cliente();
                cliente.setClienteNome( nomeS );
                cliente.setClienteEmail( emailS );
                cliente.setClienteSenha( senhaS );
                cliente.setClienteNiver( dataNiverS );
                cliente.setClienteCel(celularS );

                //Atualizar Informações do Usuário
                fireRefAlterar.child( "clienteNome" ).setValue( cliente.getClienteNome() );
                fireRefAlterar.child( "clienteEmail" ).setValue( cliente.getClienteEmail() );
                fireRefAlterar.child( "clienteSenha" ).setValue( cliente.getClienteSenha() );
                fireRefAlterar.child( "clienteNiver" ).setValue( cliente.getClienteNiver() );
                fireRefAlterar.child( "clienteCel" ).setValue( cliente.getClienteCel() );

                //Fechar Tela Atual (Voltar Para Tela Anterior = Lista)
                Toast.makeText( getBaseContext(), "Cliente Atualizado", Toast.LENGTH_LONG ).show();
                finish();
            }
            @Override public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        fireRefAlterar.addListenerForSingleValueEvent( fireOuvinteValueAlterar ); //ouvir só uma vez
    }

    //Método - Deletar
    public void btnDeletar (View view){
        fireRefRemover = fireDados.getReference().child("Clientes");
        fireRefRemover.child( cliente.getClienteId() ).removeValue();
        Toast.makeText( getBaseContext(), "Cliente Removido", Toast.LENGTH_SHORT ).show();
        finish();
    }
}

//Observações
//Os Comandos do Firebase Não Podem Ser Juntos (ir para o nó e já fazer ação). Tem que Ir Para o Nó, e Depois Remover;
//As Referencias e os Listeners Não Podem Ser Globais, Devem Ser Específicos Para Cada Finalidade;

