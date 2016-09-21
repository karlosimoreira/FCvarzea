package br.com.karlosimoreira.fcvarzea.activitys.Home.Arena.Cad;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.util.BaseActivity;
import br.com.karlosimoreira.fcvarzea.domain.util.ImagemProcess;

public class CadArenaActivity extends BaseActivity {
    private static final int IMAGEM_INTERNA_ARENA = 28;
    private  String imgArena;
    public String pathArena;
    ImageView imageArena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_arena);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ImagemProcess ip = new ImagemProcess();
        if(requestCode == IMAGEM_INTERNA_ARENA ){
            if(resultCode == RESULT_OK){
                pathArena = getImageInternal(intent);
                try {
                    imgArena = ip.saveBitmap(pathArena);
                    imageArena.setImageBitmap(ImagemProcess.getResizedBitmap(setupImage(pathArena, intent), 175, 175));
                    Log.i("onActivityResult","shield: "+imgArena);

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
        toolbar.setTitle( getResources().getString(R.string.title_activity_new_arena) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(CadArenaActivity.this, "Salvar Arena", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        imageArena =(ImageView)findViewById(R.id.imageArena);
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
        startActivityForResult(intent, IMAGEM_INTERNA_ARENA);
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
