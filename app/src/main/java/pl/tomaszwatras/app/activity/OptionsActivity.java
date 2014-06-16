package pl.tomaszwatras.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.tomaszwatras.app.R;

/**
 * Created by watrix on 15.06.2014.
 */
public class OptionsActivity extends ActionBarActivity {

    public static final String OPTION_NUMBER_OF_QUESTIONS = "pl.tomaszwatras.OptionsActivity.numberOfQuestions";

    private int numberOfQuestions;
    private EditText numberPicker;
    private Button backButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_options);

        castComponentsToObjects();
        setButtonsOnClickListeners();
        loadUserOptions();
    }

    private void loadUserOptions() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        numberOfQuestions = settings.getInt(OPTION_NUMBER_OF_QUESTIONS, 20);

        numberPicker.setText(String.valueOf(numberOfQuestions));
    }

    /**
     * Validates if stored options are correct
     *
     * @return is options valid?
     */
    private boolean isOptionsValid() {
        boolean isValid = true;
        String number = numberPicker.getText().toString();

        if (number == null || number.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Liczba pytań nie może być pusta!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            numberOfQuestions = Integer.parseInt(number);

            if (numberOfQuestions == 0) {
                Toast.makeText(getApplicationContext(), "Liczba pytań musi być większa od 0!", Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }

        return isValid;
    }

    private void setButtonsOnClickListeners() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOptionsValid()) {
                    saveUserOptions();
                    Toast.makeText(getApplicationContext(), "Opcje zapisane", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void saveUserOptions() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(OPTION_NUMBER_OF_QUESTIONS, numberOfQuestions);
        editor.commit();
    }

    private void castComponentsToObjects() {
        numberPicker = (EditText) findViewById(R.id.options_number_of_questions_input);
        backButton = (Button) findViewById(R.id.options_back_button);
        saveButton = (Button) findViewById(R.id.options_save_button);
    }
}
