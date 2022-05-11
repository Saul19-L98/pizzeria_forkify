package sv.edu.udb.pizzeria_forkify.OrderPizza.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sv.edu.udb.pizzeria_forkify.OrderPizza.model.ModelRecetas;
import sv.edu.udb.pizzeria_forkify.OrderPizza.ui.home_menu.ModelRecetasAdapter;
import sv.edu.udb.pizzeria_forkify.R;

public class RecipeDisplay extends AppCompatActivity {

    LinearLayout linearLayout;
    LinearLayout linearLayout2;

    TextView tv_titulo,NoPersonas,tv_tiempo;
//    RecyclerView lv_Ingredientes,lv_Pasos;
    private ArrayList<String> ingredientesList;
    private ArrayList<String> pasosList;
    private ArrayList<String> ArrayList_Ingredientes;
    private ArrayList<String> ArrayList_Pasos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        linearLayout = findViewById(R.id.ingredientes);
        linearLayout2 = findViewById(R.id.pasos);
        tv_tiempo=findViewById(R.id.tiempo);
        tv_titulo = findViewById(R.id.tv_recipe_title);
        NoPersonas = findViewById(R.id.tv_noPersonas);


        String key = getIntent().getStringExtra("key");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("RecetarioForkify")
                .child("Categoria")
                .child("Mixto")
                .child(key.toString());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                ArrayList_Ingredientes =new ArrayList<>();
                ArrayList_Pasos= new ArrayList<>();
                ingredientesList = new ArrayList<>();
                pasosList = new ArrayList<>();
                pasosList.clear();
                ingredientesList.clear();

                for (DataSnapshot dataSnapshot1:snapshot.child("Ingredientes").getChildren()){
                     //Log.e("Ingredientes", "Adapter pizza list: "+ dataSnapshot1);
                     ingredientesList.add(dataSnapshot1.getValue().toString());
                }

                for (DataSnapshot dataSnapshot2:snapshot.child("Pasos").getChildren()){
                    //Log.e("Pasos", "Adapter pizza list: "+ dataSnapshot2);
                    pasosList.add(dataSnapshot2.getValue().toString());
                }



                ModelRecetas modelRecetasItem =new ModelRecetas(
                    snapshot.child("titulo").getValue().toString(),
                    snapshot.child("refImg").getValue().toString(),
                    snapshot.child("descripcion").getValue().toString(),
                    snapshot.child("tiempo").getValue().toString(),
                    ingredientesList,pasosList,
                    Integer.parseInt(snapshot.child("noPersonas").getValue().toString())
                );
                    modelRecetasItem.setKey(snapshot.getKey());


                    tv_titulo.setText(modelRecetasItem.getTitulo());
                    tv_tiempo.setText(modelRecetasItem.getTiempo().toString());
                    NoPersonas.setText(modelRecetasItem.getNoPersonas().toString());

                    for (String items : ingredientesList){
                        TextView textView = new TextView(getApplication());
                        textView.setText("-"+items+"\n");
                        textView.setTextSize(20f);
                        linearLayout.addView(textView);
                    }

                for (String items : pasosList){
                    TextView textView = new TextView(getApplication());
                    textView.setText("-"+items+"\n");
                    textView.setTextSize(20f);
                    linearLayout2.addView(textView);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}