package br.com.crud01.dados;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {
    //Variáveis
    private String clienteId;               //Correlacionar com Outros Nós
    private String clienteNome;             //Identificação do Cliente
    private String clienteEmail;            //Login
    private String clienteSenha;            //Login
    private String clienteNiver;            //Marketing No Aniversário
    private String clienteCel;              //Marketing No Aniversário, Etc (SMS)
    private String clienteIMagemUri;        //salvar imagens;

    //GetterSetter (Alt + Insert)
    public String getClienteId() {return clienteId;}
    public void setClienteId(String clienteId) {this.clienteId = clienteId;}
    public String getClienteNome() {return clienteNome;}
    public void setClienteNome(String clienteNome) {this.clienteNome = clienteNome;}
    public String getClienteEmail() {return clienteEmail;}
    public void setClienteEmail(String clienteEmail) {this.clienteEmail = clienteEmail;}
    public String getClienteSenha() {return clienteSenha;}
    public void setClienteSenha(String clienteSenha) {this.clienteSenha = clienteSenha;}
    public String getClienteNiver() {return clienteNiver;}
    public void setClienteNiver(String clienteNiver) {this.clienteNiver = clienteNiver;}
    public String getClienteCel() {return clienteCel;}
    public void setClienteCel(String clienteCel) {this.clienteCel = clienteCel;}
    public String getClienteIMagemUri() { return clienteIMagemUri; }
    public void setClienteIMagemUri(String clienteIMagemUri) {this.clienteIMagemUri = clienteIMagemUri; }

    //Construtor Vazio (Alt + Insert) - Select None
    public Cliente() {
    }

    //To String (Alt + Insert) - Select None
    @Override public String toString() {
        return "Nome: " + getClienteNome();
    }

    //Parcelable (Alt + Insert) - Não Selecionar "Include Filds"
    @Override public int describeContents() {
        return 0;
    }
    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( this.clienteId );
        dest.writeString( this.clienteNome );
        dest.writeString( this.clienteEmail );
        dest.writeString( this.clienteSenha );
        dest.writeString( this.clienteNiver );
        dest.writeString( this.clienteCel );
        dest.writeString( this.clienteIMagemUri );

    }
    protected Cliente(Parcel in) {
        this.clienteId = in.readString();
        this.clienteNome = in.readString();
        this.clienteEmail = in.readString();
        this.clienteSenha = in.readString();
        this.clienteNiver = in.readString();
        this.clienteCel = in.readString();
        this.clienteIMagemUri = in.readString();
    }
    public static final Creator<Cliente> CREATOR = new Creator<Cliente>() {
        @Override public Cliente createFromParcel(Parcel source) {
            return new Cliente( source );
        }
        @Override public Cliente[] newArray(int size) {
            return new Cliente[size];
        }
    };

}

//0-Classe Dados                (No Singular - Recomendado);
//1-Instalar Plugin             (Android Parcelable Code Generator);
//2-Declarar Variáveis          (ID + Outras; Private; Nome da Classe Tb; Mesmo Nome No Firebase - Para não Usar os "Ifs Exists";
//3-Modificadores de Acesso     (Getter e Setter) - Alt + Insert
//4-Construtor Vazio            (Exigência do Firebase e Parcelable);
//5-To String                   (Como Será a Lista, Se o Adapter for Simples);
//6-Implements Parcelable       (Na Classe);
//7-Parcelable                 	(Nas Variáveis. Não Selecionar Caixa de Seleção) - Alt + Insert;
