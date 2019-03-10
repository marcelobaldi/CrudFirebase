package br.com.crud01.telas;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.crud01.R;
import br.com.crud01.dados.Cliente;

public class a1ListaClientes extends AppCompatActivity {
    //Classe - Firebase
    private FirebaseAuth           fireAuth  = FirebaseAuth.getInstance();         //Autenticação Instanciado;
    private FirebaseDatabase       fireDados = FirebaseDatabase.getInstance();     //Banco Dados Instanciado;
    private DatabaseReference      fireRef;                                        //Referencia do Banco de Dados;
    private ChildEventListener     fireChilListener;                               //Ouvinte Child (Listas; Não For; Etc);

    //Classe - Dados e Objetos Lista
    private Cliente                cliente;
    private ListView               listaObjeto;
    private ArrayList<Cliente>     listaValores = new ArrayList<>(  );
    private ArrayAdapter<Cliente>  listaAdapter;
    private List<String>           listakeys    = new ArrayList<String>( );             //Lista SubNós (Id's Clientes) - Para Alteração

    //Método - Inicial
    @Override protected void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);
        //Layout
        setContentView(R.layout.lista_clientes);
        super.setTitle("Página Main");

        //Lista - Objetos
        listaObjeto     = (ListView) findViewById( R.id.listaClientesXml );
        listaAdapter    = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, listaValores );
        listaObjeto.setAdapter( listaAdapter );

        //Lista - Valores
        buscarValoresLista();

        //Lista - Item da Lista
        listaObjeto.setOnItemClickListener( new AdapterView.OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Exibir Nome do Cliente
            Toast.makeText( a1ListaClientes.this, listaValores.get( position ).getClienteNome(), Toast.LENGTH_LONG ).show();

            //Passar Dados do Cliente Clicado
            Intent intent = new Intent(getBaseContext(), a2DetalheCliente.class);
            intent.putExtra( "ItemCliente", listaValores.get( position ));
            startActivity( intent);
        }});
    }

    //Método - Buscar Valores da Lista
    public void buscarValoresLista(){

        //Nó Para Pegar Dados e Tipo de Ouvindo
        fireRef = fireDados.getReference().child( "Clientes" );
        fireChilListener = new ChildEventListener() {

            @Override public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();                      //Pegar SubNós (id's dos clientes);
                listakeys.add( id );                                    //Adicionar Na Lista de Nós;
                cliente = dataSnapshot.getValue( Cliente.class );       //Firebase p/ Classe Dados (igual no firebase, ou if exists e getvalue);
                listaValores.add( cliente );                            //Adicionar os Dados Pegos Na Lista de Valores;
                listaAdapter.notifyDataSetChanged();                    //Atualizar a Exibição da Lista
            }
            @Override public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();                      //Pegar SubNós (id's dos clientes);
                int index = listakeys.indexOf( id );                    //Pegar a Posição do Array do SubNó Alterado;
                Cliente cliente = dataSnapshot.getValue(Cliente.class); //Firebase p/ Classe Dados (igual no firebase, ou if exists e getvalue);
                listaValores.set( index, cliente );                     //Alterar o Registro Pego Na Lista de Valores;
                listaAdapter.notifyDataSetChanged();                    //Atualizar a Exibição da Lista
            }
            @Override public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String id = dataSnapshot.getKey();                      //Pegar SubNós (id's dos clientes);
                int index = listakeys.indexOf( id );                    //Pegar a Posição do Array do SubNó Removido;
                listaValores.remove( index );                           //Remover o Registro Pego Na Lista de Valores;
                listakeys.remove( index );                              //Atualizar a Exibição da Lista
                listaAdapter.notifyDataSetChanged();
            }
            @Override public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        //Atualizar o Ouvinte
        fireRef.addChildEventListener( fireChilListener );
    }

    //Método - Cadastrar Novo Cliente
    public void btnNovoCliente (View view){
        Intent intent = new Intent( a1ListaClientes.this, b1CadastroClientes.class );
        startActivity( intent );
    }
}

//Dúvidas
//Sei Fazer, Mas Não Entendo Muito a Busca (Child Event Listener, Value Event Listener, Etc);
//Os Notify Acima São do Adapter Simples. Se Usar o RecyclerView os Notify serão Diferentes (Remove...);
//Falta o If Macro do EventListener = Null;
//Falta o Destroy Activity e Listener (parar de ouvir);
//Quando Excluir, Como Excluir Também da Autenticação (email e senha);

//Observações
//Os Campos Buscados Estão Sendo Passados de Uma Vez, Mas Poderia Ser Passados "Um a Um" com o If Exists;

