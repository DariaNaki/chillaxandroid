package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import exampfi.daria.chillaxlinuxtryandroid.R;

public class HistoryOfAppointmentsActivity extends AppCompatActivity implements OnClickListener {

    private EditText mStored;

    private EditText mId;
    private ImageButton mAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_appointments);

        mStored=(EditText)findViewById(R.id.result_db);
        String sent=getIntent().getStringExtra("stored");
        mStored.setText(sent);




        mAdd=(ImageButton) findViewById(R.id.add_to_db);
        mAdd.setOnClickListener(this);
    }


    public void onClick(View view){
        switch(view.getId()){
                case R.id.add_to_db:
                Intent intent=new Intent(getApplicationContext(), Appointment.class);
                startActivity(intent);

                break;
        }

    }
}
