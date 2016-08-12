package br.com.karlosimoreira.fcvarzea.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;

public class User implements Parcelable{
    public static String PROVIDER = "br.com.karlosimoreira.fcvarzea.domain.User.PROVIDER";
    public static final String NODE_DEFAULT = "User";
    private  final String PROVIDER_FACEBOOK = "facebook";
    private  final String PROVIDER_GOOGLE = "google";

    private String id;
    private String name;
    private String email;
    private String phone;
    private String position;
    private String city;
    private String state;
    private String photo;
    private String birthDate;
    private String password;
    private String newPassword;
    private String status;
    private String tipo;
    private String peBom;
    private String proprietario;
    private String classificacao;
    public User(){}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private void setNameInMap( Map<String, Object> map ) {
        if( getName() != null ){
            map.put( "name", getName() );
        }
    }
    public void setNameIfNull(String name) {
        if( this.name == null ){
            this.name = name;
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
            map.put( "email", getEmail() );
        }
    }
    public void setEmailIfNull(String email) {
        if( this.email == null ){
            this.email = email;
        }

    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    private void setPhoneInMap( Map<String, Object> map ) {
        if( getPhone() != null ){
            map.put( "phone", getPhone() );
        }
    }

    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    private void setBirthDateInMap( Map<String, Object> map ) {
        if( getBirthDate() != null ){
            map.put( "birthDate", getBirthDate());
        }
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    private void setPositionInMap( Map<String, Object> map ) {
        if( getPosition() != null ){
            map.put( "position", getPosition());
        }
    }

    public String getCity() {
        return city;
    }
    public void setCity(String cidade) {
        this.city = cidade;
    }
    private void setCityInMap( Map<String, Object> map ) {
        if( getCity() != null ){
            map.put( "city", getCity());
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
            map.put( "state", getState());
        }
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    private void setPhotoInMap( Map<String, Object> map ) {
        if( getPhoto() != null ){
            map.put( "photo", getPhoto());
        }
    }

    @Exclude
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    private void setStatusInMap( Map<String, Object> map ) {
        if( getStatus() != null ){
            map.put( "status", getStatus());
        }
    }

    public String getPeBom() {
        return peBom;
    }
    public void setPeBom(String peBom) {
        this.peBom = peBom;
    }
    public void setPeBomInMap(Map<String, Object> map) {
        if(getPeBom() != null){
            map.put("peBom", getPeBom());
        }
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    private void setTipoInMap( Map<String, Object> map ) {
        if( getTipo() != null ){
            map.put( "tipo", getTipo());
        }
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    private void setProprietarioInMap(Map<String, Object> map){
        if(getProprietario() != null){
            map.put("proprietario", getProprietario());
        }
    }
    public String getClassificacao() {
        return classificacao;
    }
    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
    private void setClassificacaoInMap(Map<String, Object> map){
        if(getClassificacao() != null){
            map.put("classificacao", getClassificacao());
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
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getId() );

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void contextDataDB( Context context ){
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getId() );
        firebase.addListenerForSingleValueEvent((ValueEventListener) context);
    }
    public void valueEventListenerDataDB( ValueEventListener valueEventListener ){
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getId() );
        firebase.addListenerForSingleValueEvent(valueEventListener);
    }

    public void updateDB( DatabaseReference.CompletionListener... completionListener ){

        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getId() );

        Map<String, Object> map = new HashMap<>();
        setNameInMap(map);
        setEmailInMap(map);
        setBirthDateInMap(map);
        setPhoneInMap(map);
        setPositionInMap(map);
        setPhotoInMap(map);
        setCityInMap(map);
        setStateInMap(map);
        setStatusInMap(map);
        setPeBomInMap(map);
        setTipoInMap(map);
        setProprietarioInMap(map);
        setClassificacaoInMap(map);

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

    public void removeDB( DatabaseReference.CompletionListener completionListener ){
        Log.i("removeDB","User");
        DatabaseReference firebase = LibraryClass.getFirebase().child(NODE_DEFAULT).child( getId() );
        firebase.setValue(null, completionListener);
    }

    public boolean checkSocialNetwork(Context context)
    {
        String token = getProviderSP( context );
        return (token.contains(PROVIDER_FACEBOOK) || token.contains(PROVIDER_GOOGLE));
    }

    // PARCELABLE
    public User(Parcel parcel){
        setId(parcel.readString());
        setName(parcel.readString());
        setEmail(parcel.readString());
        setPhone(parcel.readString());
        setPhoto(parcel.readString());
        setBirthDate(parcel.readString());
        setPosition(parcel.readString());
        setCity(parcel.readString());
        setState(parcel.readString());
        setStatus(parcel.readString());
        setPeBom(parcel.readString());
        setTipo(parcel.readString());
        setProprietario(parcel.readString());
        setClassificacao(parcel.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( getId() );
        dest.writeString( getName() );
        dest.writeString( getEmail() );
        dest.writeString( getPhone() );
        dest.writeString( getPhoto() );
        dest.writeString( getPosition() );
        dest.writeString( getCity() );
        dest.writeString( getState() );
        dest.writeString( getStatus() );
        dest.writeString( getPeBom() );
        dest.writeString( getTipo() );
        dest.writeString( getProprietario() );
        dest.writeString( getClassificacao() );
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
