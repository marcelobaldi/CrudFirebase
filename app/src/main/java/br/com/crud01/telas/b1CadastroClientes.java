package br.com.crud01.telas;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import br.com.crud01.R;
import br.com.crud01.dados.Cliente;

public class b1CadastroClientes extends AppCompatActivity {
    //Classe - Firebase
    private FirebaseAuth     fireAuth  = FirebaseAuth.getInstance();      //Autenticação Instanciado
    private FirebaseDatabase fireDados = FirebaseDatabase.getInstance();  //Banco Dados Instanciado
    private StorageReference mStorageReference;

    //Classe - Dados e Objetos da Tela
    private Cliente     cliente;
    private EditText    nome, email, senha, dataNiver, celular;
    private ImageView   imagemCliente;

    //Imagem Firebase
    private final static int READ_REQUEST_CODE = 0;
    private String caminhoParaImagem;
    private Uri uri;

    //Método - Inicial
    @Override protected void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState); setContentView(R.layout.cadastro_clientes);
        //Identificar Objetos
        nome        =  (EditText)  findViewById( R.id.nomeXml );
        email       =  (EditText)  findViewById( R.id.emailXml );
        senha       =  (EditText)  findViewById( R.id.senhaXml );
        dataNiver   =  (EditText)  findViewById( R.id.dataNiverXml );
        celular     =  (EditText)  findViewById( R.id.celularXml );
        imagemCliente = (ImageView) findViewById(R.id.iv_cadastro_cliente_imagem);

        mStorageReference = FirebaseStorage.getInstance().getReference("Clientes");
    }

    //Método - Cadastrar Novo Cliente - Parte 1
    public void btnCadastrar (View view){
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

        //Cadastrar no Firebase (Módulo Autenticação) - Senha Mínima de 6 Dígitos
        fireAuth.createUserWithEmailAndPassword( cliente.getClienteEmail(), cliente.getClienteSenha())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {
                Toast.makeText( b1CadastroClientes.this, "Cadastrado", Toast.LENGTH_LONG ).show();
                popularImagemDeCliente();
                cliente.setClienteIMagemUri(caminhoParaImagem);
                cadastrarUsuarioNoBanco();
            } else {
                Toast.makeText( b1CadastroClientes.this, "Erro", Toast.LENGTH_LONG ).show();
            }}});
    }

    private void popularImagemDeCliente() {
        StorageReference pizzasReference = mStorageReference.child(fireAuth.getCurrentUser().getUid() + "/foto.jpg");
        pizzasReference.putFile(uri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // Get a URL to the uploaded content//Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
            caminhoParaImagem = uriTask.toString();
            Log.e("TelaAddProduto", "**\nImagem upada com sucesso" + "\nUri: **" + caminhoParaImagem);
        }})
            .addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
            // Handle unsuccessful uploads// ...
            Log.e("TelaAddProduto", "Nao foi possivel subir a imagem");
        }});
    }

    //Método - Cadastrar Novo Cliente - Parte 2
    public void cadastrarUsuarioNoBanco (){
        //Criar Usuário na Tabela Clientes (Será o Id do Usuário Criado Pelo Firebase)
        DatabaseReference refCliente = fireDados.getReference("Clientes");
        //Salvar no Firebase
        refCliente.child(fireAuth.getCurrentUser().getUid()).setValue(cliente);
        //Fechar Tela Atual (Voltar Para Tela Anterior = Lista)
        finish();
    }

    //Pegar Arquivos do Celular 1/2
    public void btnAddFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    //Pegar Arquivos do Celular 2/2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == READ_REQUEST_CODE){
            if(data != null){
                uri = data.getData();
                Picasso.with(this).load(uri).into(imagemCliente);
            }
        }
    }
}

//Como Saber Se Cadastrou no Firebase? Tem algum  Task isSuccessful?
//Qual o Problema das Regras de Segurança Estar em Modo Teste no Firebase?

