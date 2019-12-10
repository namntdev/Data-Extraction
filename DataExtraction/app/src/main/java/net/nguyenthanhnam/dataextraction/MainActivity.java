package net.nguyenthanhnam.dataextraction;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private Dialog list_dialog;
    private ProgressBar progressBar;
    //private static final String BASE_URL = "http://10.0.1.132:8081";
    private final String BASE_URL = "http://10.0.2.2:8080";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Locale locale = Locale.getDefault();
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);

        // hide the action bar
        getSupportActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                promptSpeechInput();
                //test();
            }
        });

    }

    private void test (){

        GetDataService service = RetrofitClientInstance.getInstance().create(GetDataService.class);
        Call<List<Item>> call = service.parse("Tôi cần mua bảo hiểm một xe Kia Morning và hai xe Mazda CX5.");
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                progressBar.setVisibility(View.GONE);
                showOrder(response.body());
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    /*Create handle for the RetrofitInstance interface*/
                    GetDataService service = RetrofitClientInstance.getInstance().create(GetDataService.class);
                    progressBar.setVisibility(View.VISIBLE);
                    Call<List<Item>> call = service.parse(result.get(0));
                    call.enqueue(new Callback<List<Item>>() {
                        @Override
                        public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                            progressBar.setVisibility(View.GONE);
                            showOrder(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<Item>> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            }
            default:
                txtSpeechInput.setText("123");
                break;

        }
    }

    public void showOrder(List<Item> items) {
        // TODO Auto-generated method stub
//        Component c1,c2,c3;
//
//        ComponentAdapter array_adapter;
//        c1 = new Component("Component 1","subtitle 1");
//        c2 = new Component("Component 2","subtitle 2");
//        c3 = new Component("Component 3","subtitle 3");
        List<Component> my_list = new ArrayList<Component>();
        for (Item item:items) {
            Component c = new Component(item.getName(),"Số lượng " + item.getAmount());
            my_list.add(c);
        }
//        my_list.add(c1);
//        my_list.add(c2);
//        my_list.add(c3);

        //adapter
        ComponentAdapter array_adapter = new ComponentAdapter(MainActivity.this, R.layout.component,my_list);

        list_dialog = new Dialog(MainActivity.this);
        list_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        list_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        list_dialog.setContentView(R.layout.list_dialog);

        ListView list = (ListView)list_dialog.findViewById(R.id.component_list);
        list.setAdapter(array_adapter);

        Button positiveButton = (Button) list_dialog.findViewById(R.id.positive_button);

        positiveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                list_dialog.dismiss();
            }
        });

        list_dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public static int convertQuantity(String quantity) {
        int res = 0;
        String quantityList[] = {"không","một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười"};
        for (int i=0; i<quantityList.length; i++ ) {
            if (quantityList[i].equalsIgnoreCase(quantity)) {
                res = i;
                break;
            }
        }

        return res;
    }

}
