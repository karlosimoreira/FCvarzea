package br.com.karlosimoreira.fcvarzea.domain;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;

/**
 * Created by Carlos on 09/07/2016.
 */
public class Match {
    public static String PROVIDER = "br.com.karlosimoreira.fcvarzea.domain.Match.PROVIDER";
    private static final String NODE_DEFAULT = "Match";

    private String idPartida;
    private String namePartida;
    private String descPartida;
    private String presidente;
    private String NunTempo;
    private String duracaoTempo;
    private String numJogadores;
    private String criterioSorteio;
    private String arena;
    private String convocado;



    public Match(){}

    public String getIdPartida() {
        return idPartida;
    }
    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }

    public String getNamePartida() {
        return namePartida;
    }
    public void setNamePartida(String namePartida) {
        this.namePartida = namePartida;
    }

    public String getDescPartida() {
        return descPartida;
    }
    public void setDescPartida(String descPartida) {
        this.descPartida = descPartida;
    }

    public String getPresidente() {
        return presidente;
    }
    public void setPresidente(String presidente) {
        this.presidente = presidente;
    }

    public String getNunTempo() {
        return NunTempo;
    }
    public void setNunTempo(String NunTempo) {
        this.NunTempo = NunTempo;
    }

    public String getDuracaoTempo() {
        return duracaoTempo;
    }
    public void setDuracaoTempo(String duracaoTempo) {
        this.duracaoTempo = duracaoTempo;
    }

    public String getNumJogadores() {
        return numJogadores;
    }
    public void setNumJogadores(String numJogadores) {
        this.numJogadores = numJogadores;
    }

    public String getCriterioSorteio() {
        return criterioSorteio;
    }
    public void setCriterioSorteio(String criterioSorteio) {
        this.criterioSorteio = criterioSorteio;
    }

    public String getConvocado() {
        return convocado;
    }
    public void setConvocado(String convocado) {
        this.convocado = convocado;
    }

    public String getArena() {
        return arena;
    }
    public void setArena(String arena) {
        this.arena = arena;
    }


    public void saveDB( DatabaseReference.CompletionListener... completionListener ){
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdPartida() );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }



}
