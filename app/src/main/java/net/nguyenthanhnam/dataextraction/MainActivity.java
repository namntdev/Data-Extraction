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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private Dialog list_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Locale locale = Locale.getDefault();
        Log.d("Test", "La sao nhi " + locale.getCountry());
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        // hide the action bar
        getSupportActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                promptSpeechInput();
                //showOrder();
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
                }
                break;
            }
            default:
                txtSpeechInput.setText("123");
                break;

        }
    }

    public void showOrder() {
        // TODO Auto-generated method stub
        Component c1,c2,c3;

        ComponentAdapter array_adapter;
        c1 = new Component("Component 1","subtitle 1");
        c2 = new Component("Component 2","subtitle 2");
        c3 = new Component("Component 3","subtitle 3");
        List<Component> my_list = new ArrayList<Component>();
        my_list.add(c1);
        my_list.add(c2);
        my_list.add(c3);

        //adapter
        array_adapter = new ComponentAdapter(MainActivity.this, R.layout.component,my_list);

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
