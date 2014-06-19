package pl.tomaszwatras.app.util;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.tomaszwatras.app.enums.Category;
import pl.tomaszwatras.app.model.Question;

/**
 * Created by watrix on 13.06.2014.
 */
public class QuestionsHelper {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public QuestionsHelper(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }

    /**
     * Gets all questions from db
     *
     * @return list of all questions
     */
    public List<Question> getAllQuestions() {

        return executeQuery("SELECT  * FROM sumQuiz");
    }

    public List<Question> getQuestions(int numberOfQuestions, String category) {

        return executeQuery("SELECT  * FROM sumQuiz WHERE category = '" + category +
                "' ORDER BY RANDOM() LIMIT " + String.valueOf(numberOfQuestions));
    }

    private List<Question> executeQuery(String queryString) {
        List<Question> questionList = new ArrayList<Question>();

        Cursor cursor = database.rawQuery(queryString, null);
        String categoryString;

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(Integer.parseInt(cursor.getString(0)));
                categoryString = cursor.getString(1);
                if (categoryString != null && !categoryString.isEmpty())
                    question.setCategory(Category.valueOf(categoryString));
                question.setQuestionText(cursor.getString(2));
                question.setAnswer1(cursor.getString(3));
                question.setAnswer2(cursor.getString(4));
                question.setAnswer3(cursor.getString(5));
                question.setAnswer4(cursor.getString(6));
                question.setAnswer5(cursor.getString(7));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        Collections.shuffle(questionList);

        return questionList;
    }
}

