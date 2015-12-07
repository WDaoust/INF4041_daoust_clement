package org.esiea.daoust_clement.pppproject;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private DatePickerDialog dpd = null;
    private AlertDialog.Builder ad = null;
    private AlertDialog alertDialog = null;
    private static final String TAG ="GetBiersServices";
    private JSONArray json;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(),intentFilter);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.BIERS_UPDATE));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv_hw =(TextView)findViewById(R.id.tv_hello_world);
        TextView btn_hw =(TextView)findViewById(R.id.btn_hello_world);
        getString(R.string.hello_world);
        tv_hw.setText(DateUtils.formatDateTime(getApplicationContext(), (new Date()).getTime(), DateFormat.FULL));

        GetBiersServices.startActionGet_All_Biers(this);

        RecyclerView Rv_bieres = (RecyclerView) findViewById(R.id.rev_biere);

        Rv_bieres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        json=getBiersFromFile();
        Rv_bieres.setAdapter(new BiersAdapter(json));


        btn_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), getString(R.string.msg), Toast.LENGTH_LONG).show();
                // dpd.show();
                //alertDialog.show();
                intentFct();

            }
        });

        dpd= new DatePickerDialog(this, new OnDateSetListener(){

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_hw.setText( new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //ALERT
        ad = new AlertDialog.Builder(this).setTitle("Title").setMessage("Message").setPositiveButton("yes", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_LONG).show();}
    }).setNegativeButton("No",new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_LONG).show();}
    }).setIcon(android.R.drawable.ic_dialog_alert);

        final FrameLayout frameView = new FrameLayout(this);
        ad.setView(frameView);

        alertDialog = ad.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_view, frameView);




    }



    void intentFct() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public JSONArray getBiersFromFile(){
        try{
            InputStream is = new FileInputStream(getCacheDir() +"/"+"bieres.json");
            byte[]buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        }catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        }catch (JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static final String BIERS_UPDATE = "com.octip,cours.inf4042_11.BIERS_UPDATE";

    public class BierUpdate extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, getIntent().getAction());

        }
    }


    private class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder> {
        private JSONArray biers ;

        public BiersAdapter(JSONArray jsonarray) {
            this.biers=jsonarray;

        }

        @Override
        public BierHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

          LayoutInflater li=  LayoutInflater.from(getParent());

            View  v =li.inflate(R.layout.rv_bier_element, viewGroup, false);

            return new BierHolder(v);

        }

        @Override
        public void onBindViewHolder(BierHolder bierHolder, int i) {


        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class BierHolder extends RecyclerView.ViewHolder{

            TextView name;


            public BierHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_bier_element_name);


            }
        }

    }
}
