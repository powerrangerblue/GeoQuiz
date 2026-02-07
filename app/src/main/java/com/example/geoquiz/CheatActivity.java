package com.example.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * CheatActivity - Displays the correct answer when the user decides to cheat.
 * Uses Intent extras to pass data back to QuizActivity.
 */
public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";
    private static final String EXTRA_ANSWER = "com.example.geoquiz.answer";
    public static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    /**
     * Creates a new Intent for launching CheatActivity.
     *
     * @param packageContext the context to launch the activity from
     * @param answerIsTrue   the correct answer for the question
     * @return an Intent configured to start CheatActivity
     */
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER, answerIsTrue);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_cheat);

        // Get the correct answer from the Intent
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER, false);

        // Initialize UI views
        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);

        // Set up button listener
        mShowAnswerButton.setOnClickListener(v -> {
            int answerResId = mAnswerIsTrue ? R.string.true_button : R.string.false_button;
            mAnswerTextView.setText(answerResId);
            setAnswerShownResult(true);
            mShowAnswerButton.setEnabled(false);
            Log.d(TAG, "Answer revealed: " + (mAnswerIsTrue ? "TRUE" : "FALSE"));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    /**
     * Sets the result to indicate that the answer has been shown.
     *
     * @param isAnswerShown true if the answer was shown to the user
     */
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
