package sv.edu.udb.pizzeria_forkify.OrderPizza.Activities;

import static sv.edu.udb.pizzeria_forkify.OrderPizza.LandingMenuActivity.refMixto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import sv.edu.udb.pizzeria_forkify.OrderPizza.model.ModelArray;
import sv.edu.udb.pizzeria_forkify.OrderPizza.model.ModelRecetas;
import sv.edu.udb.pizzeria_forkify.OrderPizza.model.ModelRecetasInsert;
import sv.edu.udb.pizzeria_forkify.R;

public class RecipeModification extends AppCompatActivity {

    EditText et_recipe_title,et_nopersonas,et_tiempo,et_descripion,et_imgref;
    TextView tv_ingredintes_mod,tv_pasos_mod,save_update;

    public  static ArrayList<String> ingredientesList =new ArrayList<>();
    public  static ArrayList<String> pasosList  =new ArrayList<>();

    ArrayList<ModelArray> modelArrayIngre = new ArrayList<ModelArray>();
    ArrayList<ModelArray> modelArrayPasos = new ArrayList<ModelArray>();


    //declaracion de una actividad para un resultado
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("Display Recipe", "on result");
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_modification);

        Hooks();
        ingredientesList.clear();
        pasosList.clear();

        if (getIntent().getStringExtra("mod").contains("m")){Setters();}

        tv_ingredintes_mod.setOnClickListener(view -> {
            ModelRecetas modelRecetas =new ModelRecetas();
            Intent intent= new Intent(RecipeModification.this,PasosIngredientesMod.class);
            intent.putExtra("LastKey",getIntent().getStringExtra("LastKey"));
            intent.putExtra("type","ingredientes");
            intent.putExtra("clase",modelRecetas= (ModelRecetas) getIntent().getSerializableExtra("clase"));
            activityResultLauncher.launch(intent);

        });

        tv_pasos_mod.setOnClickListener(new View.OnClickListener() {
            ModelRecetas modelRecetas =new ModelRecetas();
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RecipeModification.this,PasosIngredientesMod.class);
                intent.putExtra("type","pasos");
                intent.putExtra("LastKey",getIntent().getStringExtra("LastKey"));
                intent.putExtra("clase",modelRecetas= (ModelRecetas) getIntent().getSerializableExtra("clase"));
                activityResultLauncher.launch(intent);
            }
        });

        save_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelArrayIngre.clear();
                modelArrayPasos.clear();
                String ListKey = "A";

                for( String item : ingredientesList){
                    ModelArray modelArray= new ModelArray(item,ListKey);
                    int charValue = ListKey.charAt(0);
                    String next  = String.valueOf((char)(charValue + 1));
                    ListKey=next;
                    modelArrayIngre.add(modelArray);
                }
                ListKey = "A";

                for( String item2 : pasosList){
                    ModelArray modelArray= new ModelArray(item2,ListKey);
                    int charValue = ListKey.charAt(0);
                    String next  = String.valueOf((char)(charValue + 1));
                    ListKey=next;
                    modelArrayPasos.add(modelArray);
                }
                if (getIntent().getStringExtra("mod").contains("m")){
                    //modificacion
                    updateDB();

                }else {
                    //creacion
                    insertDB();
                }
                RecipeModification.super.onBackPressed();
                finish();

            }
        });

    }

    private void insertDB() {

        ModelRecetasInsert modelRecetasInsert = new ModelRecetasInsert(
                et_recipe_title.getText().toString().trim(),
                et_imgref.getText().toString().trim(),
                et_descripion.getText().toString().trim(),
                et_tiempo.getText().toString().trim(),
                Integer.parseInt(et_nopersonas.getText().toString().trim())
        );

        String key= getIntent().getStringExtra("LastKey");
        refMixto.child(key).setValue(modelRecetasInsert);

        in_pasos_ingre(key);

    }

    private void updateDB() {
        ModelRecetas modelRecetas =new ModelRecetas();
        modelRecetas= (ModelRecetas) getIntent().getSerializableExtra("clase");

        ModelRecetasInsert modelRecetasInsert = new ModelRecetasInsert(
                modelRecetas.getTitulo(),
                modelRecetas.getRefImg(),
                modelRecetas.getDescripcion(),
                modelRecetas.getTiempo(),
                modelRecetas.getNoPersonas()
        );

        refMixto.child(modelRecetas.getKey()).setValue(modelRecetasInsert);
        in_pasos_ingre(modelRecetas.getKey());
    }

    private void in_pasos_ingre(String key) {
        for( ModelArray item : modelArrayIngre){
            refMixto.child(key).child("Ingredientes").child(item.getKey()).setValue(item.getValor());
        }


        for( ModelArray item : modelArrayPasos){
            refMixto.child(key).child("Pasos").child(item.getKey()).setValue(item.getValor());
        }


    }

    private void Hooks() {
    et_recipe_title=findViewById(R.id.et_recipe_title);
    et_descripion=findViewById(R.id.et_descripion);
    et_nopersonas=findViewById(R.id.et_nopersonas);
    et_tiempo=findViewById(R.id.et_tiempo);
    et_imgref=findViewById(R.id.et_imgref);
    tv_ingredintes_mod=findViewById(R.id.tv_ingredintes_mod);
    tv_pasos_mod=findViewById(R.id.tv_pasos_mod);
    save_update= findViewById(R.id.save_update);
    }

    private void Setters() {
        ModelRecetas modelRecetas;
        modelRecetas= (ModelRecetas) getIntent().getSerializableExtra("clase");

        pasosList.addAll(modelRecetas.getPasos());
        ingredientesList.addAll(modelRecetas.getIngredientes());



        et_imgref.setText(modelRecetas.getRefImg());
        et_recipe_title.setText(modelRecetas.getTitulo());
        et_nopersonas.setText(modelRecetas.getNoPersonas().toString());
        et_tiempo.setText(modelRecetas.getTiempo());
        et_descripion.setText(modelRecetas.getDescripcion());
    }
}