package com.i2i.myfirebasedemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String,String> unamePassMap;
    String[] userIdsArr;
    ArrayAdapter<CharSequence> adapter;

    EditText i_uname,u_uname,i_pass,u_pass;
    TextView d_uname;
    Spinner sp_update,sp_delete;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        i_uname = findViewById(R.id.et_i_uname);
        i_pass = findViewById(R.id.et_i_pass);
        u_uname = findViewById(R.id.et_u_uname);
        u_pass = findViewById(R.id.et_u_pass);
        d_uname = findViewById(R.id.tv_d_uname);
        sp_update = findViewById(R.id.sp_u);
        sp_delete = findViewById(R.id.sp_d);

        unamePassMap = new HashMap<String, String>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("img2imp/users/");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             System.out.println("datadatadata : "+dataSnapshot.getChildrenCount());
             int i=0;
             userIdsArr = new String[(int)(dataSnapshot.getChildrenCount())];
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String uname = postSnapshot.getKey();
                    String pass = postSnapshot.getValue().toString();
                    unamePassMap.put(deEmail(uname),pass);
                    userIdsArr[i++] = deEmail(uname);
                    System.out.println("uname = "+uname+" pass = "+pass);
                }
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,userIdsArr);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_update.setAdapter(adapter);
                sp_delete.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firefirefire", "Failed to read value.", error.toException());
            }
        });



        sp_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String uname = sp_update.getSelectedItem().toString();
                u_uname.setText(uname);
                u_pass.setText(unamePassMap.get(uname));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_delete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String uname = sp_delete.getSelectedItem().toString();
                d_uname.setText(unamePassMap.get(uname));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    public void insertData(View view) {
        if(addDataToDatabase(i_uname.getText().toString(),i_pass.getText().toString()))
        {
            Toast.makeText(this, "Inserted Successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    boolean addDataToDatabase(String uname,String pass)
    {
        try {
            uname = enEmail(uname);
            System.out.println("unamepassunam : "+uname+" paspass :"+pass);
            myRef.child(uname).setValue(pass);
            return true;
        }
        catch (Exception e)
        {
            Log.w("ererererer", "Failed to read value.", e.getCause());
            return false;
        }
    }
    String enEmail(String email)
    {
        return email.replace(".", ",");
    }
    String deEmail(String email)
    {
        return email.replace(",", ".");
    }

    public void updateData(View view) {
        String uname = sp_update.getSelectedItem().toString();
        if(!u_uname.getText().toString().equals(uname))
        {
            myRef.child(enEmail(uname)).removeValue();
        }
        if(addDataToDatabase(u_uname.getText().toString(),u_pass.getText().toString()))
        {
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(View view) {
        String uname = sp_delete.getSelectedItem().toString();
        myRef.child(enEmail(uname)).removeValue();
        Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
    }
}
