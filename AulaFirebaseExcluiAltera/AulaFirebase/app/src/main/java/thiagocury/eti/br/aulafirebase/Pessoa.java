package thiagocury.eti.br.aulafirebase;

import java.io.Serializable;

/**
 * Created by Alunos on 21/12/2016.
 */

public class Pessoa implements Serializable{

    private String key;
    private String nome;
    private int idade;

    public Pessoa() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                ", nome='" + nome + '\'' +
                ", idade=" + idade +
                '}';
    }
}//fecha classe
