package br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.activitys.MainActivity;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.BaseActivity;
import br.com.karlosimoreira.fcvarzea.domain.util.DownloadImageTask;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;
import br.com.karlosimoreira.fcvarzea.domain.util.Positions;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Carlos on 28/05/2016.
 */

public class UpdateActivity extends BaseActivity implements  DatabaseReference.CompletionListener , ValueEventListener, DatePickerDialog.OnDateSetListener,  DialogInterface.OnCancelListener {

    private static final int IMAGEM_INTERNA = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        profilerHeader = dataSnapshot.getValue(User.class);

        name.setText(profilerHeader.getName());
        donoArena = Integer.parseInt(profilerHeader.getProprietario());
        if(donoArena == 1){
            cbSim.setChecked(true);
            cbNao.setChecked(false);
        }else {
            cbSim.setChecked(false);
            cbNao.setChecked(true);
        }
        if(profilerHeader.getBirthDate() != "" && profilerHeader.getBirthDate() != null  ){
            birthDate.setText(profilerHeader.getBirthDate());
        }else {
            birthDate.setText("01/01/1930");
        }
        phone.setText(profilerHeader.getPhone());
        if(profilerHeader.getPosition() != "" && profilerHeader.getPosition() != null  ){
            position = Integer.parseInt(profilerHeader.getPosition());
        }else {
            position = 0;
        }
        city.setText(profilerHeader.getCity());
        state.setText(profilerHeader.getState());
        pathPhotoSave = profilerHeader.getPhoto();
        if(profilerHeader.getPhoto() == "" || profilerHeader.getPhoto() == null){
            if(isImageSelect){
                return;
            }else {
                photoProfiler.setImageBitmap(ImagemProcess.getResizedBitmap(convetBitmap(R.drawable.ic_account), 175, 175));
            }
        }else {
            if (ok == false){
                setupImage(pathPhotoSave);
            }
        }

        if(profilerHeader.getStatus() != null ){
            ativo = Integer.parseInt(profilerHeader.getStatus());
            mensalista = Integer.parseInt(profilerHeader.getTipo());

            if(ativo == 1){
                cbAtivo.setChecked(true);
            }
            else {
                cbAtivo.setChecked(false);
            }
            if(mensalista == 1){
               cbMensal.setChecked(true);
            }
            else {
                cbMensal.setChecked(false);
            }


           spPe.setSelection(Integer.parseInt(profilerHeader.getPeBom()));
        }
        setPosition(position);

    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.report( databaseError.toException() );
    }
    @Override
    public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
        if( firebaseError != null ){
            FirebaseCrash.report( firebaseError.toException() );
            Toast.makeText( this, R.string.Falha_update +firebaseError.getMessage(), Toast.LENGTH_LONG ).show();
        }
        else{
            Toast.makeText( this, R.string.Sucesso_update, Toast.LENGTH_SHORT ).show();
            callMainActivity();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("photoProfiler",pathPhotoSave);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        ImagemProcess Resultado = new ImagemProcess();
        if(requestCode == IMAGEM_INTERNA){
            if(resultCode == RESULT_OK){
                pathshield = getImageInternal(intent);
                try {
                    params = new String[]{pathshield,name.getText().toString()};
                    url_cloudinary =  Resultado.uploadCloudinary(params);

                    ok = true;
                    photoProfiler.setImageBitmap(ImagemProcess.getResizedBitmap(setupImage(pathshield, intent), 175, 175));
                    isImageSelect = true;

                }catch (Exception e){
                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                }
            }
        }
    }


    private void init(){

        toolbar.setTitle( getResources().getString(R.string.title_activity_update_accounts) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                 @Override
                 public boolean onMenuItemClick(MenuItem item) {
                     switch(item.getItemId()) {
                         case R.id.action_save:
                             update();
                             break;

                     }
                      return true;
                 }
        });

        ImagemProcess.ConfigCloudinary();
        ImagemProcess.initCloudinary(this);

        name = (AutoCompleteTextView) findViewById(R.id.name);
        birthDate = (TextView) findViewById(R.id.txtBirthDate);
        phone = (AutoCompleteTextView) findViewById(R.id.phone);
        city = (AutoCompleteTextView)findViewById(R.id.city);
        state = (AutoCompleteTextView)findViewById(R.id.state);
        photoProfiler = (CircleImageView)findViewById(R.id.photoProfiler);
        imagePosition = (ImageView) findViewById(R.id.imagePosition);
        tvBirthDate = (TextView)findViewById(R.id.txtBirthDate);
        spPe = (Spinner)findViewById(R.id.spPe);
        spPe.setAdapter( spinnerAdapter());
        cbAtivo = (CheckBox)findViewById(R.id.cbAtivo);
        cbMensal = (CheckBox)findViewById(R.id.cbMensal);
        cbSim = (CheckBox)findViewById(R.id.cbSim);
        cbNao = (CheckBox)findViewById(R.id.cbNao);


        cbSim.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cbNao.setChecked(!cbNao.isChecked());
            }
        });
        cbNao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cbSim.setChecked(!cbSim.isChecked());
            }
        });

        getMelhorPe();

        user = new User();
        user.setId( FirebaseAuth.getInstance().getCurrentUser().getUid() );
        user.contextDataDB(this);
    }

    private void setupImage(String Url) {

        Log.i("setupImage","UrlUp: " + Url);
        new DownloadImageTask((CircleImageView) findViewById(R.id.photoProfiler)).execute(Url);
    }

    private Bitmap setupImage(String Url, Intent intent) {

        Bitmap bitmap = null;
        Log.i("setupImage","Url: " + Url);
        Uri imageUri = intent.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
        }catch (Exception e){
            Log.i("setupImage","Exception: " + e.getMessage());
        }
        return bitmap;
    }

    private Bitmap setupImageSave(String url) {

        Bitmap bitmap = null;
        Log.i("setupImage","Url: " + url);
       Uri img = Uri.parse(url);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),img);
        }catch (Exception e){
            Log.i("setupImage","Exception: " + e.getMessage());
        }
        return bitmap;
    }

    private Bitmap convetBitmap(int Id) {
        Bitmap bitmapCovert = ImagemProcess.decodeSampledBitmapFromResource(getResources(), Id, 30, 30);
        return bitmapCovert;
    }

    private String getImageInternal(Intent intent){

        Uri imagemSelecionada = intent.getData();
        String[] colunas = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(imagemSelecionada, colunas, null, null, null);
        cursor.moveToFirst();

        int indexColuna = cursor.getColumnIndex(colunas[0]);
        String pathImg = cursor.getString(indexColuna);
        cursor.close();

        return (pathImg);
    }

    public void LoadImage(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGEM_INTERNA);
    }


    public void update(){
        user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid() );
        getCbData();
        user.setName( name.getText().toString() );
        user.setBirthDate(birthDate.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setPosition(String.valueOf(position));
        user.setCity(city.getText().toString());
        user.setState(state.getText().toString());
        user.setPhoto(url_cloudinary);
        user.setPeBom( melhorPe );
        user.setStatus(String.valueOf(ativo));
        user.setTipo(String.valueOf(mensalista));
        user.setProprietario(String.valueOf(donoArena));

       user.updateDB( UpdateActivity.this );

    }

    public void getCbData(){
        if(cbAtivo.isChecked()){
            ativo = 1;
        }
        else {
            ativo = 0;
        }
        if(cbMensal.isChecked()){
            mensalista = 1;
        }else {
            mensalista =0;
        }

        if(cbSim.isChecked()){
            donoArena = 1;
        }else {
            donoArena = 0;
        }
    }

    public void getMelhorPe(){
        spPe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 melhorPe = String.valueOf(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private void msgIsSelected(Boolean result){
        Log.i("msgIsSelected","result: " + result );
        if(result == true) {
            Toast.makeText(UpdateActivity.this, "Apenas uma posição pode ser escolhida!", Toast.LENGTH_LONG);
        }
    }


    public void dialogDisplayBirthDate(){

        initBirthDateData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        Calendar  cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();
        cMin.set(1930, 11, 31 );
        cMax.set( cMax.get(Calendar.YEAR), 11, 31 );
        datePickerDialog.setMinDate(cMin);
        datePickerDialog.setMaxDate(cMax);

        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show( getFragmentManager(), "DatePickerDialog" );
    }

    public void getBirthDate(View view){
        dialogDisplayBirthDate();
    }

    public String setTextBirthDate(){
        String data = (day < 10 ? "0"+day : day)+"/"+
                (month+1 < 10 ? "0"+(month+1) : month+1)+"/"+ year;
        return data;
    }

    private void initBirthDateData(){
        if( year == 0 ){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }


    public void dialogDisplayPosition(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Selecione a posição desejada");
        dialog.setContentView(R.layout.position_dialog_layout);

        imageViewGl = (ImageView)dialog.findViewById(R.id.gl);
        imageViewLe = (ImageView)dialog.findViewById(R.id.le);
        imageViewZg = (ImageView)dialog.findViewById(R.id.zg);
        imageViewVl = (ImageView)dialog.findViewById(R.id.vl);
        imageViewMo = (ImageView)dialog.findViewById(R.id.mo);
        imageViewMe = (ImageView)dialog.findViewById(R.id.me);
        imageViewMd = (ImageView)dialog.findViewById(R.id.md);
        imageViewLd = (ImageView)dialog.findViewById(R.id.ld);
        imageViewPe = (ImageView)dialog.findViewById(R.id.pe);
        imageViewSa = (ImageView)dialog.findViewById(R.id.sa);
        imageViewCa = (ImageView)dialog.findViewById(R.id.ca);
        imageViewPd = (ImageView)dialog.findViewById(R.id.pd);

        final TextView txtPosition = (TextView)dialog.findViewById(R.id.txtPosition);


        Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
        Button btnSave = (Button)dialog.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //txtPosition.setText("Save");
                if(positionSelected != null){
                    position =  getPosition(positionSelected);
                    dialog.dismiss();
                }else {
                    Toast.makeText(UpdateActivity.this,"Favor selecionar uma posição!", Toast.LENGTH_LONG);
                }
            }
        });

        imageViewGl.setImageBitmap(convetBitmap(R.drawable.ic_position_gl));
        imageViewLe.setImageBitmap(convetBitmap(R.drawable.ic_position_le));
        imageViewZg.setImageBitmap(convetBitmap(R.drawable.ic_position_zg));
        imageViewVl.setImageBitmap(convetBitmap(R.drawable.ic_position_vl));
        imageViewMo.setImageBitmap(convetBitmap(R.drawable.ic_position_mo));
        imageViewMe.setImageBitmap(convetBitmap(R.drawable.ic_position_me));
        imageViewMd.setImageBitmap(convetBitmap(R.drawable.ic_position_md));
        imageViewLd.setImageBitmap(convetBitmap(R.drawable.ic_position_ld));
        imageViewPe.setImageBitmap(convetBitmap(R.drawable.ic_position_pe));
        imageViewSa.setImageBitmap(convetBitmap(R.drawable.ic_position_sa));
        imageViewCa.setImageBitmap(convetBitmap(R.drawable.ic_position_ca));
        imageViewPd.setImageBitmap(convetBitmap(R.drawable.ic_position_pd));


        imageViewGl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Goleiro");

                if (flag) {
                    imageViewGl.setImageBitmap(convetBitmap(R.drawable.ic_position_select_gl));
                    disableView(String.valueOf(imageViewGl.getTag()));
                }
                else {
                    imageViewGl.setImageBitmap(convetBitmap(R.drawable.ic_position_gl));
                    enableView(String.valueOf(imageViewGl.getTag()));
                }
                positionSelected = Positions.GOLEIRO;

                flag = !flag;
                Log.i("imageViewGl","flag: " + flag );
            }
        });

        imageViewLe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Lateral esquerdo");
                if (flag) {
                    imageViewLe.setImageBitmap(convetBitmap(R.drawable.ic_position_select_le));
                    disableView(String.valueOf(imageViewLe.getTag()));
                }
                else {
                    imageViewLe.setImageBitmap(convetBitmap(R.drawable.ic_position_le));
                    enableView(String.valueOf(imageViewLe.getTag()));
                }
                positionSelected = Positions.LATERAL_ESQUERDO;

                flag = !flag;
                Log.i("imageViewLe","flag: " + flag );

            }
        });

        imageViewLd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Lateral Direito");

                if (flag) {
                    imageViewLd.setImageBitmap(convetBitmap(R.drawable.ic_position_select_ld));
                    disableView(String.valueOf(imageViewLd.getTag()));
                }
                else {
                    imageViewLd.setImageBitmap(convetBitmap(R.drawable.ic_position_ld));
                    enableView(String.valueOf(imageViewLd.getTag()));
                }
                positionSelected = Positions.LATERAL_DIREITO;

                flag = !flag;
                Log.i("imageViewLd","flag: " + flag );
            }
        });

        imageViewZg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Zagueiro");

                if (flag) {
                    imageViewZg.setImageBitmap(convetBitmap(R.drawable.ic_position_select_zg));
                    disableView(String.valueOf(imageViewZg.getTag()));
                }
                else {
                    imageViewZg.setImageBitmap(convetBitmap(R.drawable.ic_position_zg));
                    enableView(String.valueOf(imageViewZg.getTag()));
                }
                positionSelected = Positions.ZAGUEIRO;

                flag = !flag;
                Log.i("imageViewZg","flag: " + flag );
            }
        });

        imageViewVl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Volante");
                if (flag) {
                    imageViewVl.setImageBitmap(convetBitmap(R.drawable.ic_position_select_vl));
                    disableView(String.valueOf(imageViewVl.getTag()));
                }
                else {
                    imageViewVl.setImageBitmap(convetBitmap(R.drawable.ic_position_vl));
                    enableView(String.valueOf(imageViewZg.getTag()));
                }
                positionSelected = Positions.VOLANTE;

                flag = !flag;
                Log.i("imageViewVl","flag: " + flag );
            }
        });

        imageViewMe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Meia esquerda");
                if (flag) {
                    imageViewMe.setImageBitmap(convetBitmap(R.drawable.ic_position_select_me));
                    disableView(String.valueOf(imageViewMe.getTag()));
                }
                else {
                    imageViewMe.setImageBitmap(convetBitmap(R.drawable.ic_position_me));
                    enableView(String.valueOf(imageViewMe.getTag()));
                }
                positionSelected = Positions.MEIA_ESQUERDA;

                flag = !flag;
                Log.i("imageViewMe","flag: " + flag );

            }
        });

        imageViewMd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Meia Direito");
                if (flag) {
                    imageViewMd.setImageBitmap(convetBitmap(R.drawable.ic_position_select_md));
                    disableView(String.valueOf(imageViewMd.getTag()));
                }
                else {
                    imageViewMd.setImageBitmap(convetBitmap(R.drawable.ic_position_md));
                    enableView(String.valueOf(imageViewMd.getTag()));
                }
                positionSelected = Positions.MEIA_DIREITA;

                flag = !flag;
                Log.i("imageViewMd","flag: " + flag );
            }
        });

        imageViewMo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Meio ofencivo");
                if (flag) {
                    imageViewMo.setImageBitmap(convetBitmap(R.drawable.ic_position_select_mo));
                    disableView(String.valueOf(imageViewMo.getTag()));
                }
                else {
                    imageViewMo.setImageBitmap(convetBitmap(R.drawable.ic_position_mo));
                    enableView(String.valueOf(imageViewMo.getTag()));
                }
                positionSelected = Positions.MEIA_OFENCIVO;

                flag = !flag;
                Log.i("imageViewMo","flag: " + flag );
            }
        });

        imageViewPe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Ponta esquerda");
                if (flag) {
                    imageViewPe.setImageBitmap(convetBitmap(R.drawable.ic_position_select_pe));
                    disableView(String.valueOf(imageViewPe.getTag()));
                }
                else {
                    imageViewPe.setImageBitmap(convetBitmap(R.drawable.ic_position_pe));
                    enableView(String.valueOf(imageViewPe.getTag()));
                }
                positionSelected = Positions.PONTA_ESQUERDA;

                flag = !flag;
                Log.i("imageViewPe","flag: " + flag );
            }
        });

        imageViewPd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Ponta Direita");
                if (flag) {
                    imageViewPd.setImageBitmap(convetBitmap(R.drawable.ic_position_select_pd));
                    disableView(String.valueOf(imageViewPd.getTag()));
                }
                else {
                    imageViewPd.setImageBitmap(convetBitmap(R.drawable.ic_position_pd));
                    enableView(String.valueOf(imageViewPd.getTag()));
                }
                positionSelected = Positions.PONTA_DIREITA;

                flag = !flag;
                Log.i("imageViewPd","flag: " + flag );
            }
        });

        imageViewSa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Segundo atacante");
                if (flag) {
                    imageViewSa.setImageBitmap(convetBitmap(R.drawable.ic_position_select_sa));
                    disableView(String.valueOf(imageViewSa.getTag()));
                }
                else {
                    imageViewSa.setImageBitmap(convetBitmap(R.drawable.ic_position_sa));
                    enableView(String.valueOf(imageViewSa.getTag()));
                }
                positionSelected = Positions.SEGUNDO_ATACANTE;

                flag = !flag;
                Log.i("imageViewSa","flag: " + flag );
            }
        });

        imageViewCa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPosition.setText("Centro avante");
                if (flag) {
                    imageViewCa.setImageBitmap(convetBitmap(R.drawable.ic_position_select_ca));
                    disableView(String.valueOf(imageViewCa.getTag()));
                }
                else {
                    imageViewCa.setImageBitmap(convetBitmap(R.drawable.ic_position_ca));
                    enableView(String.valueOf(imageViewCa.getTag()));
                }
                positionSelected = Positions.CENTRO_AVANTE;

                flag = !flag;
                Log.i("imageViewCa","flag: " + flag );
            }
        });


        dialog.show();

    }

    public int getPosition(Positions p){

        int result = 0;
        switch (p) {

            case GOLEIRO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_gl));
                result = GOLEIRO;
                break;
            case ZAGUEIRO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_zg));
                result = ZAGUEIRO;
                break;
            case LATERAL_ESQUERDO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_le));
                result = LATERAL_ESQUERDO;
                break;
            case LATERAL_DIREITO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_ld));
                result = LATERAL_DIREITO;
                break;
            case MEIA_ESQUERDA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_me));
                result = MEIA_ESQUERDA;
                break;
            case VOLANTE:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_vl));
                result = VOLANTE;
                break;
            case MEIA_DIREITA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_md));
                result = MEIA_DIREITA;
                break;
            case PONTA_ESQUERDA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_pe));
                result = PONTA_ESQUERDA;
                break;
            case SEGUNDO_ATACANTE:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_sa));
                result = SEGUNDO_ATACANTE;
                break;
            case MEIA_OFENCIVO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_mo));
                result = MEIA_OFENCIVO;
                break;
            case CENTRO_AVANTE:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_ca));
                result = CENTRO_AVANTE;
                break;
            case PONTA_DIREITA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_pd));
                result = PONTA_DIREITA;
                break;
            default:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_gl));
                result = GOLEIRO;

        }

        return result;
    }

    public void setPosition(int p){

        int result = 0;
        switch (p) {

            case GOLEIRO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_gl));
                break;
            case ZAGUEIRO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_zg));
                break;
            case LATERAL_ESQUERDO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_le));
                break;
            case LATERAL_DIREITO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_ld));
                break;
            case MEIA_ESQUERDA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_me));
                break;
            case VOLANTE:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_vl));
                break;
            case MEIA_DIREITA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_md));
                break;
            case PONTA_ESQUERDA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_pe));
                break;
            case SEGUNDO_ATACANTE:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_sa));
                break;
            case MEIA_OFENCIVO:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_mo));
                break;
            case CENTRO_AVANTE:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_ca));
                break;
            case PONTA_DIREITA:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_select_pd));
                break;
            default:
                imagePosition.setImageBitmap(convetBitmap(R.drawable.ic_position_gl));

        }

    }


    private void disableView(String tag){
        if(tag.equals("gl")){
            imageViewLe.setClickable(false);
            //imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("le")){
            //imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("ld")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            //imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("zg")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            //imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("vl")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            //imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("mo")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            //imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("me")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            //imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("md")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            //imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("pe")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            //imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("sa")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            //imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("ca")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            //imageViewCa.setClickable(false);
            imageViewPd.setClickable(false);
        }
        else if (tag.equals("pd")){
            imageViewLe.setClickable(false);
            imageViewGl.setClickable(false);
            imageViewZg.setClickable(false);
            imageViewVl.setClickable(false);
            imageViewMo.setClickable(false);
            imageViewMe.setClickable(false);
            imageViewMd.setClickable(false);
            imageViewLd.setClickable(false);
            imageViewPe.setClickable(false);
            imageViewSa.setClickable(false);
            imageViewCa.setClickable(false);
            //imageViewPd.setClickable(false);
        }

    }

    private void enableView( String tag){

        if(tag.equals("gl")){
            imageViewLe.setClickable(true);
            //imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("le")){
            //imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("ld")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            //imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("zg")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            //imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("vl")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            //imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("mo")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            //imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("me")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            //imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("md")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            //imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("pe")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            //imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("sa")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            //imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("ca")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            //imageViewCa.setClickable(true);
            imageViewPd.setClickable(true);
        }
        else if (tag.equals("pd")){
            imageViewLe.setClickable(true);
            imageViewGl.setClickable(true);
            imageViewZg.setClickable(true);
            imageViewVl.setClickable(true);
            imageViewMo.setClickable(true);
            imageViewMe.setClickable(true);
            imageViewMd.setClickable(true);
            imageViewLd.setClickable(true);
            imageViewPe.setClickable(true);
            imageViewSa.setClickable(true);
            imageViewCa.setClickable(true);
            //imageViewPd.setClickable(true);
        }
    }


    public ArrayAdapter spinnerAdapter(){
        String[]pe = new String[]{"Esquerdo","Direito" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }


    public void CallFinshActivity(View view){
        callMainActivity();
    }

    private void callMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = day = 0;
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day);

       this.year = year;
       this.month = monthOfYear;
       this.day = dayOfMonth;

        tvBirthDate.setText(setTextBirthDate());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
