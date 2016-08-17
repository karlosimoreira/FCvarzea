package br.com.karlosimoreira.fcvarzea.domain.util;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.karlosimoreira.fcvarzea.domain.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Carlos on 05/07/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public final int GOLEIRO = 1;
    public final int ZAGUEIRO = 2;
    public final int LATERAL_DIREITO = 3;
    public final int LATERAL_ESQUERDO = 4;
    public final int VOLANTE = 5 ;
    public final int MEIA_ESQUERDA =6;
    public final int MEIA_DIREITA = 7;
    public final int PONTA_ESQUERDA = 8;
    public final int SEGUNDO_ATACANTE = 9;
    public final int MEIA_OFENCIVO = 10;
    public final int CENTRO_AVANTE = 11;
    public final int PONTA_DIREITA = 12;

    public User user;
    public AutoCompleteTextView name;
    public TextView birthDate;
    public AutoCompleteTextView phone;
    public AutoCompleteTextView city;
    public AutoCompleteTextView state;
    public String pathPhotoSave;
    public CircleImageView photoProfiler;
    public ImageView imagePosition;
    public String pathshield;
    public String[] params;
    public String url_cloudinary;
    public User profilerHeader;
    public Boolean ok = false;
    public Boolean isImageSelect = false;
    public Boolean flag = true;
    public int position = 0;
    public Positions positionSelected;
    public int year;
    public int month;
    public int day;
    public int ativo;
    public int mensalista;
    public String melhorPe;
    public Spinner spPe;
    public CheckBox cbAtivo;
    public CheckBox cbMensal;

    public ImageView imageViewGl;
    public ImageView imageViewLe;
    public ImageView imageViewZg;
    public ImageView imageViewVl;
    public ImageView imageViewMo;
    public ImageView imageViewMe;
    public ImageView imageViewMd;
    public ImageView imageViewLd;
    public ImageView imageViewPe;
    public ImageView imageViewSa;
    public ImageView imageViewCa;
    public ImageView imageViewPd;
    public ImageView imageShield;

    public TextView tvBirthDate;
    public TextView tvPresidente;
    public TextView tvTitulo;
    public TextView tvDescricao;


    public Toolbar toolbar;
    public Button btnArena;
    public Button btnConvocarJogadores;
    public CheckBox cbData;
    public CheckBox cbDia;
    public TextView tvData;
    public Spinner spDia;
    public Calendar time =Calendar.getInstance();
    public DateFormat fmtTime =DateFormat.getTimeInstance();
    public Spinner spNunJogadores;
    public Spinner spClassificacao;
    public Spinner spDuracao;
    public Spinner spPosition;
    public Spinner spValues;
    public SeekBar sbIdadeValor;
    public SeekBar sbIdadeValor2;
    public TextView tvIdadeValor;
    public TextView tvIdadeValor2;
    public CheckBox cbCovocarPosicao;
    public CheckBox cbSim;
    public CheckBox cbNao;
    public CheckBox cbCovocarClasificacao;
    public CheckBox cbIdade;
    public int donoArena;
    public String clausuraPrimaria;
    public String valuePrimario;
    public String valueTipo;

    public RadioGroup rgCriterio;
    public RadioGroup radioGroup;
    public RadioGroup rgTempo;
    public DatabaseReference databaseReference;

    public ArrayAdapter spinnerPositionAdapter(){
        Positions[]posicoes = new Positions[]{Positions.GOLEIRO, Positions.ZAGUEIRO, Positions.LATERAL_ESQUERDO, Positions.LATERAL_DIREITO,Positions.VOLANTE,Positions.MEIA_ESQUERDA,
                Positions.MEIA_DIREITA,Positions.MEIA_OFENCIVO,Positions.PONTA_ESQUERDA,Positions.PONTA_DIREITA,Positions.CENTRO_AVANTE,Positions.SEGUNDO_ATACANTE};
        ArrayAdapter<Positions> adapter = new ArrayAdapter<Positions>(this, android.R.layout.simple_spinner_dropdown_item, posicoes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter spinnerClassAdapter(){
        String[]classificacao = new String[]{"3 ESTRELAS","4 ESTRELAS","5 ESTRELAS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classificacao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter spinnerDuracaoAdapter(){
        String[]duracao = new String[]{"5 MINUTOS","7 MINUTOS","10 MINUTOS","15 MINUTOS","20 MINUTOS","30 MINUTOS","45 MINUTOS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, duracao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter spinnerNunJogadoresAdapter(){
        String[]nunJogadore = new String[]{"8","10","12","14","16","18","22"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nunJogadore);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter spinnerDiaSemanaAdapter(){
        String[]duracao = new String[]{"DOMINGO","SEGUNDA","TERÇA","QUARTA","QUINTA","SEXTA","SABADO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, duracao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter spinnerValuesAdapter(ArrayList<String> citys){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, citys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

}
