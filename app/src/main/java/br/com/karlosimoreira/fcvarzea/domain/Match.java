package br.com.karlosimoreira.fcvarzea.domain;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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
    private String escudo;



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
    private void setNamePartidaInMap( Map<String, Object> map ) {
        if( getNamePartida() != null ){
            map.put( "namePartida", getNamePartida() );
        }
    }

    public String getDescPartida() {
        return descPartida;
    }
    public void setDescPartida(String descPartida) {
        this.descPartida = descPartida;
    }
    private void setDescPartidaInMap( Map<String, Object> map ) {
        if( getDescPartida() != null ){
            map.put( "descPartida", getDescPartida() );
        }
    }

    public String getPresidente() {
        return presidente;
    }
    public void setPresidente(String presidente) {
        this.presidente = presidente;
    }
    private void setPresidenteInMap( Map<String, Object> map ) {
        if( getPresidente() != null ){
            map.put( "presidente", getPresidente() );
        }
    }

    public String getNunTempo() {
        return NunTempo;
    }
    public void setNunTempo(String NunTempo) {
        this.NunTempo = NunTempo;
    }
    private void setNunTempoInMap( Map<String, Object> map ) {
        if( getNunTempo() != null ){
            map.put( "NunTempo", getNunTempo() );
        }
    }

    public String getDuracaoTempo() {
        return duracaoTempo;
    }
    public void setDuracaoTempo(String duracaoTempo) {
        this.duracaoTempo = duracaoTempo;
    }
    private void setDuracaoTempoInMap( Map<String, Object> map ) {
        if( getDuracaoTempo() != null ){
            map.put( "duracaoTempo", getDuracaoTempo() );
        }
    }

    public String getNumJogadores() {
        return numJogadores;
    }
    public void setNumJogadores(String numJogadores) {
        this.numJogadores = numJogadores;
    }
    private void setNumJogadoresInMap( Map<String, Object> map ) {
        if( getCriterioSorteio() != null ){
            map.put( "numJogadores", getCriterioSorteio() );
        }
    }

    public String getCriterioSorteio() {
        return criterioSorteio;
    }
    public void setCriterioSorteio(String criterioSorteio) {
        this.criterioSorteio = criterioSorteio;
    }
    private void setCriterioSorteioInMap( Map<String, Object> map ) {
        if( getNumJogadores() != null ){
            map.put( "criterioSorteio", getNumJogadores() );
        }
    }

    public String getConvocado() {
        return convocado;
    }
    public void setConvocado(String convocado) {
        this.convocado = convocado;
    }
    private void setConvocadoInMap( Map<String, Object> map ) {
        if( getConvocado() != null ){
            map.put( "convocado", getConvocado() );
        }
    }

    public String getArena() {
        return arena;
    }
    public void setArena(String arena) {
        this.arena = arena;
    }
    private void setArenaInMap( Map<String, Object> map ) {
        if( getArena() != null ){
            map.put( "arena", getArena() );
        }
    }
    public String getEscudo() {
        return escudo;
    }
    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }
    private void setEscudoInMap( Map<String, Object> map ) {
        if( getEscudo() != null ){
            map.put( "escudo", getEscudo() );
        }
    }

    //Metodos

    public void saveProviderSP(Context context, String token ){
        LibraryClass.saveSP( context, PROVIDER, token );
    }
    public String getProviderSP(Context context ){
        return( LibraryClass.getSP( context, PROVIDER) );
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

    public void contextDataDB( Context context ){
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdPartida() );
        firebase.addListenerForSingleValueEvent((ValueEventListener) context);
    }
    public void valueEventListenerDataDB( ValueEventListener valueEventListener ){
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdPartida() );
        firebase.addListenerForSingleValueEvent(valueEventListener);
    }

    public void updateDB( DatabaseReference.CompletionListener... completionListener ){

        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdPartida() );

        Map<String, Object> map = new HashMap<>();
        setNamePartidaInMap(map);
        setDescPartidaInMap(map);
        setPresidenteInMap(map);
        setNunTempoInMap(map);
        setDuracaoTempoInMap(map);
        setNumJogadoresInMap(map);
        setCriterioSorteioInMap(map);
        setConvocadoInMap(map);
        setArenaInMap(map);
        setEscudoInMap(map);

        if( map.isEmpty() ){
            return;
        }
        if( completionListener.length > 0 ){
            firebase.updateChildren(map, completionListener[0]);
        }
        else{
            firebase.updateChildren(map);
        }
    }

}
