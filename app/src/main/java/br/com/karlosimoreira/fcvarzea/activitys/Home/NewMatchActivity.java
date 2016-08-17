package br.com.karlosimoreira.fcvarzea.activitys.Home;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.activitys.Home.Arena.ArenasActivity;
import br.com.karlosimoreira.fcvarzea.activitys.Home.Jogador.JogadoresSearchActivity;
import br.com.karlosimoreira.fcvarzea.domain.User;
import br.com.karlosimoreira.fcvarzea.domain.util.BaseActivity;
import br.com.karlosimoreira.fcvarzea.domain.util.Calendario;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;
import br.com.karlosimoreira.fcvarzea.domain.util.LibraryClass;
import br.com.karlosimoreira.fcvarzea.domain.util.StringUtils;

public class NewMatchActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener,  DialogInterface.OnCancelListener, TimePickerDialog.OnTimeSetListener{
    private static final int IMAGEM_INTERNA_SHIELD = 26;
    public  static final int JOGADORES_ACTIVITY = 1;
    public  final String SHIELD = "shield";
    private User dataUser;
    private ArrayList<String> listCity;
    private ArrayList<String > listState;
    private  String userName;
    private  String tempoPartida;
    private  String duracaoPartida;
    private  String numJogadores;
    private  String ordemSorteio;
    private  String nomeArena;
    private  String shield;
    private ArrayList<String > convocados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listCity =new ArrayList<>();
        listState =new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null){
            Bundle UserParams = intent.getExtras();
            if(UserParams != null){
                userName = UserParams.getString("userName");
                Log.i("onCreate","userName: "+userName);
            }
        }
        databaseReference = LibraryClass.getFirebase().child(User.NODE_DEFAULT);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()){
                    dataUser = d.getValue(User.class);
                    if(!StringUtils.isNullOrBlank(dataUser.getCity())){
                        listCity.add(dataUser.getCity());
                        listCity = new ArrayList(new HashSet(listCity));
                    }

                    if (!StringUtils.isNullOrBlank(dataUser.getState())){
                        listState.add(dataUser.getState());
                        listState = new ArrayList(new HashSet(listState));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        databaseReference.addValueEventListener(valueEventListener);

        valueTipo = "0";
    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnArena:
                dialogDisplayBuscarArena();

                break;
            case R.id.btnConvocarJogadores:
                dialogDisplayConvocarJogadores();
                break;

        }
    }
    @Override
    public void onCancel(DialogInterface dialog) {
        Calendario.year = Calendario.month = Calendario.day = 0;
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, monthOfYear, dayOfMonth);


        tDefault.set(year, monthOfYear, dayOfMonth, Calendario.hour, Calendario.minute);

        Calendario.year = year;
        Calendario.month = monthOfYear;
        Calendario.day = dayOfMonth;

        tvData.setText(setTextDate());
    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if( hourOfDay < 7 || hourOfDay > 23 ){
            onDateSet(null, year, month, day);
            Toast.makeText(this, "Somente entre 7h e 23h", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendario.hour = hourOfDay;
        Calendario.minute = minute;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        ImagemProcess ip = new ImagemProcess();
        if(requestCode == IMAGEM_INTERNA_SHIELD ){
            if(resultCode == RESULT_OK){
                pathshield = getImageInternal(intent);
                try {
                    shield = ip.saveBitmap(pathshield);
                    imageShield.setImageBitmap(ImagemProcess.getResizedBitmap(setupImage(pathshield, intent), 175, 175));
                    Log.i("onActivityResult","shield: "+shield);

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
        toolbar.setTitle( getResources().getString(R.string.title_activity_new_match) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(NewMatchActivity.this, "Salvar partida", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        tvTitulo = (TextView)findViewById(R.id.tvTitulo);
        tvDescricao = (TextView)findViewById(R.id.tvDesc);
        spDuracao = (Spinner)findViewById(R.id.spDuracao);
        spNunJogadores = (Spinner)findViewById(R.id.spNunJogadores);
        tvPresidente = (TextView)findViewById(R.id.tvPresidente);
        rgTempo = (RadioGroup) findViewById(R.id.rgTempo);
        rgCriterio = (RadioGroup) findViewById(R.id.rgCriterio);
        imageShield = (ImageView)findViewById(R.id.imageShield);



        rgTempo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbTempo1:
                        tempoPartida = "0"; //Partida de 1 tempo
                        break;
                    case R.id.rbTempo2:
                        tempoPartida = "1"; //Partida de 2 tempo
                        break;
                }
            }
        });


        if (tvPresidente != null) {
            tvPresidente.setText(userName);
        }
        spDuracao.setAdapter(spinnerDuracaoAdapter());
        spNunJogadores.setAdapter(spinnerNunJogadoresAdapter());

        spDuracao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                duracaoPartida = spDuracao.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spNunJogadores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numJogadores = spNunJogadores.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rgCriterio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbCriterio1:
                        ordemSorteio = "0"; //Criterio de sorteio sem sorteio
                        break;
                    case R.id.rbCriterio2:
                        ordemSorteio = "1"; //Criterio de sorteio por chegada
                        break;
                    case R.id.rbCriterio3:
                        ordemSorteio = "2"; //Criterio de sorteio Aleatorio
                        break;
                }
            }
        });



        btnArena = (Button)findViewById(R.id.btnArena);
        assert btnArena != null;
        btnArena.setOnClickListener(this);
        btnConvocarJogadores = (Button)findViewById(R.id.btnConvocarJogadores);
        if (btnConvocarJogadores != null) {
            btnConvocarJogadores.setOnClickListener(this);
        }
        valueTipo ="0";


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
        startActivityForResult(intent, IMAGEM_INTERNA_SHIELD);
    }

    public void dialogDisplayDate(){

        Calendario.initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(Calendario.year, Calendario.month, Calendario.day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setMinDate(Calendar.getInstance());
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show( getFragmentManager(), "DatePickerDialog" );
    }

    public void dialogDisplayBuscarArena(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(getResources().getString(R.string.title_dialog_busca_arena));
        dialog.setContentView(R.layout.select_criterio_arena);

        Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnSearch = (Button)dialog.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewMatchActivity.this, ArenasActivity.class));
                //Toast.makeText(UpdateActivity.this,"Favor selecionar uma posição!", Toast.LENGTH_LONG);
                }

        });

        cbData = (CheckBox)dialog.findViewById(R.id.cbData);
        tvData = (TextView) dialog.findViewById(R.id.tvData);
        cbDia = (CheckBox)dialog.findViewById(R.id.cbDia);
        spDia = (Spinner)dialog.findViewById(R.id.spDia) ;

        cbData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbData.isChecked()){
                    tvData.setVisibility(View.VISIBLE);
                    cbDia.setVisibility(View.GONE);
                }
                else{
                    tvData.setVisibility(View.GONE);
                    cbDia.setVisibility(View.VISIBLE);
                }
            }
        });
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDisplayDate();
            }
        });

        cbDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbDia.isChecked()){
                    spDia.setVisibility(View.VISIBLE);
                    cbData.setVisibility(View.GONE);
                }
                else{
                    spDia.setVisibility(View.GONE);
                    cbData.setVisibility(View.VISIBLE);
                }
            }
        });
        spDia.setAdapter(spinnerDiaSemanaAdapter());


        dialog.show();
    }

    public void dialogDisplayConvocarJogadores(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle(getResources().getString(R.string.title_dialog_convocar_jogador));
        dialog.setContentView(R.layout.select_criterio_convocacao);

        Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
        Button btnSearch = (Button)dialog.findViewById(R.id.btnSearch);
        spPosition = (Spinner)dialog.findViewById(R.id.spPosition);
        spValues = (Spinner)dialog.findViewById(R.id.spValues);
        spPosition.setAdapter(spinnerPositionAdapter());
        spClassificacao =(Spinner)dialog.findViewById(R.id.spClassificacao);
        spClassificacao.setAdapter(spinnerClassAdapter());
        sbIdadeValor = (SeekBar)dialog.findViewById(R.id.sbIdadeValor);
        sbIdadeValor2 = (SeekBar)dialog.findViewById(R.id.sbIdadeValor2);
        tvIdadeValor = (TextView)dialog.findViewById(R.id.tvIdadeValor);
        tvIdadeValor2 = (TextView)dialog.findViewById(R.id.tvIdadeValor2);
        cbCovocarPosicao =(CheckBox)dialog.findViewById(R.id.cbCovocarPosicao);
        cbCovocarClasificacao =(CheckBox)dialog.findViewById(R.id.cbCovocarClasificacao);
        cbIdade =(CheckBox)dialog.findViewById(R.id.cbIdade);
        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);

        valueTipo = "0";
        clausuraPrimaria = "city";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbCidade:
                        spValues.setActivated(true);
                        spValues.setVisibility(View.VISIBLE);
                        clausuraPrimaria = "city";
                        valueTipo = "1";
                        spValues.setAdapter(spinnerValuesAdapter(listCity));
                        valuePrimario = spValues.getSelectedItem().toString();
                        break;
                    case R.id.rbEstado:
                        spValues.setActivated(true);
                        spValues.setVisibility(View.VISIBLE);
                        clausuraPrimaria = "state";
                        valueTipo = "1";
                        spValues.setAdapter(spinnerValuesAdapter(listState));
                        valuePrimario = spValues.getSelectedItem().toString();
                        break;
                    case R.id.rbTudo:
                        valueTipo = "0";
                        spValues.setVisibility(View.GONE);
                        spValues.setActivated(false);
                        break;
                }
            }
        });
        if(listState != null)
            Collections.sort(listState);
        if(listCity != null) {
            Collections.sort(listCity);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.v("onClick ", clausuraPrimaria + " & " +valuePrimario);
                if (StringUtils.isNullOrEmpty(valuePrimario) || valuePrimario.equals("None")){
                    valuePrimario = "None";
                }else {
                    if (!spValues.isActivated()){
                        valuePrimario = "None";
                    }else {
                        valuePrimario = spValues.getSelectedItem().toString();
                    }

                }
                enviarDados(clausuraPrimaria, valuePrimario,valueTipo);
                dialog.dismiss();
            }
        });

        sbIdadeValor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progresValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvIdadeValor.setText(String.valueOf(progresValue));
            }
        });

        sbIdadeValor2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progresValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvIdadeValor2.setText(String.valueOf(progresValue));
            }
        });

        cbCovocarPosicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbCovocarPosicao.isChecked()) {
                    spPosition.setVisibility(View.VISIBLE);
                }else {
                    spPosition.setVisibility(View.GONE);
                    spPosition.setSelection(0);
                }
            }
        });
        cbCovocarClasificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbCovocarClasificacao.isChecked()) {
                    spClassificacao.setVisibility(View.VISIBLE);
                }else {
                    spClassificacao.setVisibility(View.GONE);
                    spClassificacao.setSelection(0);
                }
            }
        });
        cbIdade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbIdade.isChecked()) {
                    sbIdadeValor.setVisibility(View.VISIBLE);
                    sbIdadeValor2.setVisibility(View.VISIBLE);
                    tvIdadeValor.setVisibility(View.VISIBLE);
                    tvIdadeValor2.setVisibility(View.VISIBLE);
                }else {
                    sbIdadeValor.setVisibility(View.GONE);
                    sbIdadeValor2.setVisibility(View.GONE);
                    tvIdadeValor.setVisibility(View.GONE);
                    tvIdadeValor2.setVisibility(View.GONE);
                    tvIdadeValor.setText("0");
                    tvIdadeValor2.setText("0");
                }
            }
        });
        dialog.show();
    }

    public void enviarDados(String clausura, String valor, String tipo){

        Bundle params = new Bundle();
        params.putString("clausuraPrimaria", clausura);
        params.putString("valuePrimario", valor);
        params.putString("tipo", tipo);
        Intent intent = new Intent(this, JogadoresSearchActivity.class);
        intent.putExtras(params);
        startActivityForResult(intent, JOGADORES_ACTIVITY );
    }

    public String setTextDate(){
        String data = ( (Calendario.day < 10 ? "0"+Calendario.day : Calendario.day)+"/"+
                (Calendario.month+1 < 10 ? "0"+(Calendario.month+1) : Calendario.month+1)+"/"+ Calendario.year );
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_actions, menu);
        return super.onCreateOptionsMenu(menu);
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
