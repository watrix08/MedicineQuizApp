package pl.tomaszwatras.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import pl.tomaszwatras.app.R;

/**
 * Created by watrix on 14.06.2014.
 */
public class ResultActivity extends ActionBarActivity {
    private Double totalPoints;
    private Double totalQuestions;
    private TextView resultTextView;
    private Button mainMenuButton;
    private Button oneMoreTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        castComponentsToObjects();
        setButtonOnClickListeners();
        getPointsAndNumberOfQuestions();

        double percentResult = (totalPoints / totalQuestions) * 100;

        resultTextView.setText(new DecimalFormat("#.##").format(totalPoints)
                + " / " + new DecimalFormat("#.##").format(totalQuestions)
                + "\n" + new DecimalFormat("#.##").format(percentResult) + "%");
    }

    /**
     * Gets stored in context values
     */
    private void getPointsAndNumberOfQuestions() {
        Bundle extras = getIntent().getExtras();
        String points = null;
        String questions = null;
        if (extras != null) {
            points = extras.getString(QuizActivity.TOTAL_POINTS_KEY);
            questions = extras.getString(QuizActivity.TOTAL_QUESTIONS_KEY);
        }

        if (points != null) {
            totalPoints = Double.valueOf(points);
        }
        if (questions != null) {
            totalQuestions = Double.valueOf(questions);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SelectYearActivity.class));
    }

    private void setButtonOnClickListeners() {
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        oneMoreTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QuizActivity.class));
            }
        });
    }

    private void castComponentsToObjects() {
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        mainMenuButton = (Button) findViewById(R.id.result_exit_button);
        oneMoreTimeButton = (Button) findViewById(R.id.result_one_more_time_button);
    }
}
