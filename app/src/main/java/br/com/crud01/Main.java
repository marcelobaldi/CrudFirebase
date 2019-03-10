package br.com.crud01;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.crud01.telas.a1ListaClientes;

public class Main extends AppCompatActivity {

    //Método - Inicial
    @Override protected void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);setContentView(R.layout.main);
        //Ir Para Lista de Clientes
        Intent intent = new Intent( Main.this, a1ListaClientes.class );
        startActivity( intent );
    }
}

//Projeto
//Firebase:     Conta Solutions; Projeto Crud;
//Próxima V.:   02 (CEP); 03 (Foto - Storage);

//Comandos
//Ctrl+/        Comentar e Descomentar em Bloco (como se fosse linha a linha);
//Tab           Alinhar Direita;
//Shitf + Tab   Alinhar Esquerda;
