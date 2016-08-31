package br.com.karlosimoreira.fcvarzea.domain;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;

/**
 * Created by Carlos on 31/08/2016.
 */
public class Arena {

    public static String PROVIDER = "br.com.karlosimoreira.fcvarzea.domain.Arena.PROVIDER";
    private static final String NODE_DEFAULT = "Arena";

    private String idArena;
    private String nomeArena;
    private String proprietario;
    private String idProprietario;
    private String phoneArena;
    private String endArena;
    private String city;
    private String state;
    private String Bairro;
    private String email;
    private String site;
    private String valorHora;
    private String horarioFuncionamento;


    public Arena() {}

    public String getIdArena() {
        return idArena;
    }
    public void setIdArena(String idArena) {
        this.idArena = idArena;
    }

    public String getNomeArena() {
        return nomeArena;
    }
    public void setNomeArena(String nomeArena) {
        this.nomeArena = nomeArena;
    }
    private void setNameArenaInMap( Map<String, Object> map ) {
        if( getNomeArena() != null ){
            map.put( "getNomeArena", getNomeArena() );
        }
    }

    public String getProprietario() {
        return proprietario;
    }
    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }
    private void setProprietarioInMap( Map<String, Object> map ) {
        if( getProprietario() != null ){
            map.put( "getProprietario", getProprietario() );
        }
    }

    public String getIdProprietario() {
        return idProprietario;
    }
    public void setIdProprietario(String idProprietario) {
        this.idProprietario = idProprietario;
    }
    private void setIdProprietarioInMap( Map<String, Object> map ) {
        if( getIdProprietario() != null ){
            map.put( "getIdProprietario", getIdProprietario() );
        }
    }

    public String getPhoneArena() {
        return phoneArena;
    }
    public void setPhoneArena(String phoneArena) {
        this.phoneArena = phoneArena;
    }
    private void setPhoneArenaInMap( Map<String, Object> map ) {
        if( getPhoneArena() != null ){
            map.put( "getPhoneArena", getPhoneArena() );
        }
    }

    public String getEndArena() {
        return endArena;
    }
    public void setEndArena(String endArena) {
        this.endArena = endArena;
    }
    private void setEndArenaInMap( Map<String, Object> map ) {
        if( getEndArena() != null ){
            map.put( "getEndArena", getEndArena() );
        }
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    private void setCityInMap( Map<String, Object> map ) {
        if( getCity() != null ){
            map.put( "getCity", getCity() );
        }
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    private void setStateInMap( Map<String, Object> map ) {
        if( getState() != null ){
            map.put( "getState", getState() );
        }
    }

    public String getBairro() {
        return Bairro;
    }
    public void setBairro(String bairro) {
        Bairro = bairro;
    }
    private void setBairroInMap( Map<String, Object> map ) {
        if( getBairro() != null ){
            map.put( "getBairro", getBairro() );
        }
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    private void setEmailInMap( Map<String, Object> map ) {
        if( getEmail() != null ){
            map.put( "getEmail", getEmail() );
        }
    }

    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    private void setSiteInMap( Map<String, Object> map ) {
        if( getSite() != null ){
            map.put( "getSite", getSite() );
        }
    }

    public String getValorHora() {
        return valorHora;
    }
    public void setValorHora(String valorHora) {
        this.valorHora = valorHora;
    }
    private void setValorHoraInMap( Map<String, Object> map ) {
        if( getValorHora() != null ){
            map.put( "getValorHora", getValorHora() );
        }
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }
    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }
    private void setHorarioFuncionamentoInMap( Map<String, Object> map ) {
        if( getHorarioFuncionamento() != null ){
            map.put( "getHorarioFuncionamento", getHorarioFuncionamento() );
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
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdArena() );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void contextDataDB( Context context ){
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdArena() );
        firebase.addListenerForSingleValueEvent((ValueEventListener) context);
    }
    public void valueEventListenerDataDB( ValueEventListener valueEventListener ){
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdArena() );
        firebase.addListenerForSingleValueEvent(valueEventListener);
    }

    public void updateDB( DatabaseReference.CompletionListener... completionListener ){

        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getIdArena() );

        Map<String, Object> map = new HashMap<>();
        setNameArenaInMap(map);
        setProprietarioInMap(map);
        setIdProprietarioInMap(map);
        setPhoneArenaInMap(map);
        setEndArenaInMap(map);
        setCityInMap(map);
        setStateInMap(map);
        setBairroInMap(map);
        setEmailInMap(map);
        setSiteInMap(map);
        setValorHoraInMap(map);
        setHorarioFuncionamentoInMap(map);

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
