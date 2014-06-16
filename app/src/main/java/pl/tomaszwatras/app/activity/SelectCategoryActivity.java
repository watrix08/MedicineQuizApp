package pl.tomaszwatras.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import pl.tomaszwatras.app.R;
import pl.tomaszwatras.app.enums.Category;

/**
 * Created by watrix on 14.06.2014.
 */
public class SelectCategoryActivity extends ActionBarActivity {
    public static final String CATEGORY_KEY = "pl.tomaszwatras.app.activity.SelectCategoryActivity.category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_category);

        Button microbiolButton = (Button) findViewById(R.id.select_category_microbiol_exam_button);
        microbiolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), QuizActivity.class);

                i.putExtra(CATEGORY_KEY, Category.MICROBIOLOGY.toString());
                startActivity(i);
            }
        });
    }
}
