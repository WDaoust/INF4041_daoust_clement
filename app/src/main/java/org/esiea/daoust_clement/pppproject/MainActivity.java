package org.esiea.daoust_clement.pppproject;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.BoringLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity  {


    private static final String TAG = "GetBiersServices";
    private JSONArray json;
    private RecyclerView rev_bieres=null;
    private DatePickerDialog dpd = null;
    private AlertDialog.Builder ad = null;
    private AlertDialog alertDialog = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //final TextView tv_hw = (TextView) findViewById(R.id.tv_hello_world);
        TextView btn_hw = (TextView) findViewById(R.id.btn_hello_world);
        getString(R.string.hello_world);
        //tv_hw.setText(DateUtils.formatDateTime(getApplicationContext(), (new Date()).getTime(), DateFormat.FULL));

        GetBiersServices.startActionGet_All_Biers(this);

        rev_bieres = (RecyclerView) findViewById(R.id.rev_biere);
        rev_bieres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rev_bieres.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rev_bieres, new ClickListener() {



            @Override
            public void onClick(View view, int position) {
                if(position==2|| position==3|| position== 5|| position== 7|| position== 11|| position== 13|| position== 17
                        || position== 19|| position== 23|| position== 29|| position== 31|| position==37|| position== 41
                        || position== 43|| position== 47|| position== 53|| position== 59|| position== 61|| position== 67
                        || position== 71|| position== 73|| position== 79|| position== 83|| position== 89|| position==97){
                    Toast.makeText(MainActivity.this, "n°"+position+ " "+getString(R.string.Unavailable), Toast.LENGTH_SHORT).show();
                }
                else {Toast.makeText(MainActivity.this, "n°"+position+ " "+getString(R.string.available), Toast.LENGTH_SHORT).show();}


            }

            @Override
            public void onLongClick(View view, final int  position) {

                if(position==2|| position==3|| position== 5|| position== 7|| position== 11|| position== 13|| position== 17
                        || position== 19|| position== 23|| position== 29|| position== 31|| position==37|| position== 41
                        || position== 43|| position== 47|| position== 53|| position== 59|| position== 61|| position== 67
                        || position== 71|| position== 73|| position== 79|| position== 83|| position== 89|| position==97) {
                    Toast.makeText(MainActivity.this, "n°" + position + " " + getString(R.string.Unavailable), Toast.LENGTH_SHORT).show();
                }
                else{new AlertDialog.Builder(MainActivity.this )
                        .setTitle( "Validation" )
                        .setMessage( getString(R.string.addMessage) )
                        .setPositiveButton( "yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "n°" + position + " " + getString(R.string.addedMessage), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, ""+getString(R.string.msg2), Toast.LENGTH_SHORT).show();
                            }
                        } )
                        .show();

                }
            }
        }));
        json = getBiersFromFile();
        rev_bieres.setAdapter(new BiersAdapter(json));


        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(), intentFilter);

        ad = new AlertDialog.Builder(this);
        btn_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();

            }
        });


        ad = new AlertDialog.Builder(this).setTitle("Validation").setMessage("Souhaitez-vous valider cette action ?").setPositiveButton("yes", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
            Toast.makeText(getApplicationContext(),getString(R.string.msg),Toast.LENGTH_LONG).show();
            intentFct();
        }
            }).setNegativeButton("No",new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int which){
            Toast.makeText(getApplicationContext(), getString(R.string.msg2), Toast.LENGTH_LONG).show();
        }
            }).setIcon(android.R.drawable.ic_dialog_alert);
        final FrameLayout frameView = new FrameLayout(this);
                ad.setView(frameView);
                alertDialog = ad.create();
                LayoutInflater inflater = alertDialog.getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_view, frameView);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();




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
            Toast.makeText(this, ""+item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id ==R.id.navigate){
            startActivity(new Intent(this, SubActivity.class));
        }

        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    public static final String BIERS_UPDATE = "com.octip.inf4042_11.BIERS_UPDATE";

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.esiea.daoust_clement.pppproject/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.esiea.daoust_clement.pppproject/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }




    public class BierUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, getIntent().getAction());
            // Mettre une notification ici
            rev_bieres.setAdapter(new BiersAdapter(getBiersFromFile()));
        }
    }


    public JSONArray getBiersFromFile() {//enregistre un jsonarray avec le contenu du lien
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "bieres.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder> {
        private JSONArray biers;

        public BiersAdapter(JSONArray jsonarray) {
            this.biers = jsonarray;
        }

        @Override
        public BierHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            LayoutInflater li = LayoutInflater.from(viewGroup.getContext());

            View v = li.inflate(R.layout.rv_bier_element, viewGroup, false);

            return new BierHolder(v);

        }

        @Override
        public void onBindViewHolder(BierHolder bierHolder, int i) {

            try{
                JSONObject jObj= biers.getJSONObject(i);
                String jS= jObj.getString("name");
                bierHolder.name.setText(jS);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return biers.length();
        }

        class BierHolder extends RecyclerView.ViewHolder {

            TextView name;


            public BierHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_bier_element_name);



            }
        }


    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{


        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            Log.d("BLA","constructor invoked");
            this.clickListener=clickListener;
            gestureDetector= new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.d("BLA","onSimpleTapUp");
                    return true;

                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child =recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clickListener!=null){

                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                    Log.d("BLA","onLongPress");
                   // super.onLongPress(e);
                }
            });

        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d("BLA","onInterceptTouchEvent"+gestureDetector.onTouchEvent(e)+" " +e);
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if (child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d("BLA","onTouchEvent" +e);

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view, int position);
    }
}
