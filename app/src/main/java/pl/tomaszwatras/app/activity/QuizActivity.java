package pl.tomaszwatras.app.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pl.tomaszwatras.app.R;
import pl.tomaszwatras.app.model.Question;
import pl.tomaszwatras.app.model.QuizButton;
import pl.tomaszwatras.app.util.QuestionsHelper;

/**
 * Created by watrix on 12.06.2014.
 */
public class QuizActivity extends ActionBarActivity {

    //public keys
    public static final String TOTAL_POINTS_KEY = "pl.tomaszwatras.app.activity.QuizActivity.totalPoints";
    public static final String TOTAL_QUESTIONS_KEY = "pl.tomaszwatras.app.activity.QuizActivity.totalQuestions";

    //data helper
    private QuestionsHelper questionsHelper;

    // variables
    private List<Question> questionList;
    private int currentQuestion = 0;
    private int totalPoints = 0;
    private int totalNumberOfQuestions;
    private boolean buttonsBlocked = false;

    //components
    private TextView questionView;
    private TextView idTextView;
    private List<QuizButton> buttonList;
    private QuizButton answer5Button;
    private Button nextQuestionButton;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_quiz);

        questionsHelper = new QuestionsHelper(this);
        questionsHelper.open();

        castComponentsToObjects();
        init();
    }

    /**
     * Initializes components and first question
     */
    private void init() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int numberOfQuestionsToFetch = settings.getInt(OptionsActivity.OPTION_NUMBER_OF_QUESTIONS, 20);

        fetchQuestions(numberOfQuestionsToFetch);
        nextQuestionButton.setVisibility(View.INVISIBLE);

        setUpQuestionAndAnswers(questionList.get(currentQuestion));
        setButtonOnClickListeners();
    }

    /**
     * Fetching questions for selected category
     */
    private void fetchQuestions(int numberOfQuestionsToFetch) {
        Bundle extras = getIntent().getExtras();
        String category = "";
        if (extras != null) {
            category = extras.getString(SelectCategoryActivity.CATEGORY_KEY);
        }

        questionList = questionsHelper.getQuestions(numberOfQuestionsToFetch, category);
        totalNumberOfQuestions = questionList.size();

    }

    /**
     * Prepares next question
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void nextQuestion() {
        if (Build.VERSION.SDK_INT >= 14) {
            questionView.setScrollY(0);
        }
        currentQuestion++;

        if (currentQuestion < totalNumberOfQuestions) {
            nextQuestionButton.setVisibility(View.INVISIBLE);

            resetButtons();
            setUpQuestionAndAnswers(questionList.get(currentQuestion));

            buttonsBlocked = false;
        } else {
            Intent i = new Intent(getApplicationContext(), ResultActivity.class);
            i.putExtra(TOTAL_POINTS_KEY, Integer.toString(totalPoints));
            i.putExtra(TOTAL_QUESTIONS_KEY, Integer.toString(totalNumberOfQuestions));

            startActivity(i);
        }
    }

    /**
     * Sets default background color and answer to false for all buttons
     */
    private void resetButtons() {
        for (QuizButton button : buttonList) {
            button.setCorrectAnswer(false);
            button.setBackgroundResource(android.R.drawable.btn_default);
        }
        answer5Button.setBackgroundResource(android.R.drawable.btn_default);
    }

    /**
     * Renders question and answers
     *
     * @param question current question to set up
     */
    private void setUpQuestionAndAnswers(Question question) {
        //show question on a label
        questionView.setText(question.getQuestionText());

        //show question id
        idTextView.setText("id: " + String.valueOf(question.getId()));

        //show answer button 5 only if there are 5 answers
        if (!isAnswer5ButtonVisible(question)) {
            answer5Button.setVisibility(View.INVISIBLE);
            buttonList.remove(answer5Button);
        } else {
            if (!buttonList.contains(answer5Button)) {
                answer5Button.setVisibility(View.VISIBLE);
                buttonList.add(answer5Button);
            }
        }

        // shuffle answer buttons
        Collections.shuffle(buttonList, new Random(new Date().getTime()));

        // first button has always correct answer
        buttonList.get(0).setCorrectAnswer(true);

        for (int i = 0; i < 4; i++) {
            buttonList.get(i).setText(question.getAnswers().get(i));
        }

        if (isAnswer5ButtonVisible(question)) {
            buttonList.get(4).setText(question.getAnswer5());
        }
    }

    /**
     * Check if chosen answer is correct and colorize buttons
     *
     * @param button - chosen button to check
     */
    private void checkAnswer(QuizButton button) {
        if (button.isCorrectAnswer()) {
            totalPoints++;
            button.setBackgroundColor(Color.rgb(120, 171, 70));
            toast = Toast.makeText(this, "Jupi!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            button.setBackgroundColor(Color.rgb(220, 20, 60));
            findCorrectAnswer().setBackgroundColor(Color.rgb(120, 171, 70));
            toast = Toast.makeText(this, "Dałeś dupy", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Look up for the correct answer
     *
     * @return correct answer button, or null
     */
    private QuizButton findCorrectAnswer() {

        for (QuizButton button : buttonList) {
            if (button.isCorrectAnswer()) {
                return button;
            }
        }

        return null;
    }

    /**
     * Sets onClickListeners for whole buttons
     */
    private void setButtonOnClickListeners() {
        for (final QuizButton button : buttonList) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //check answers only if buttons are not blocked
                    if (!buttonsBlocked) {
                        checkAnswer(button);
                    }
                    buttonsBlocked = true;
                    nextQuestionButton.setVisibility(View.VISIBLE);
                }
            });
        }

        answer5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check answers only if buttons are not blocked
                if (!buttonsBlocked) {
                    checkAnswer(answer5Button);
                }
                buttonsBlocked = true;
                nextQuestionButton.setVisibility(View.VISIBLE);
            }
        });

        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
                toast.cancel();
            }
        });
    }

    /**
     * Casts buttons from view to objects
     */
    private void castComponentsToObjects() {
        QuizButton answerButton;
        buttonList = new ArrayList<QuizButton>();

        answerButton = (QuizButton) findViewById(R.id.answer1);
        buttonList.add(answerButton);

        answerButton = (QuizButton) findViewById(R.id.answer2);
        buttonList.add(answerButton);

        answerButton = (QuizButton) findViewById(R.id.answer3);
        buttonList.add(answerButton);

        answerButton = (QuizButton) findViewById(R.id.answer4);
        buttonList.add(answerButton);

        //since answer button 5 is optional, I don't want to add it to the list
        answer5Button = (QuizButton) findViewById(R.id.answer5);

        nextQuestionButton = (Button) findViewById(R.id.nextQuestionButton);

        questionView = (TextView) findViewById(R.id.questionTextView);
        questionView.setMovementMethod(new ScrollingMovementMethod());

        idTextView = (TextView) findViewById(R.id.quiz_activity_id_textView);
    }

    private boolean isAnswer5ButtonVisible(Question question) {
        return question.getAnswer5() != null && !question.getAnswer5().isEmpty();
    }

    @Override
    protected void onResume() {
        questionsHelper.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        questionsHelper.close();
        super.onPause();
    }
}
