package pl.tomaszwatras.app.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by watrix on 14.06.2014.
 */
public class QuizButton extends Button {

    private boolean correctAnswer = false;

    public QuizButton(Context context) {
        super(context);
    }

    public QuizButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuizButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
