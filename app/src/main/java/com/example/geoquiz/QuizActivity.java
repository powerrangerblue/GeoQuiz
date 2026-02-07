package com.example.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * QuizActivity - Main activity that displays quiz questions and handles user interactions.
 * Implements Philippine geography true/false questions with scoring and a cheat feature.
 */
public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_SCORE = "score";
    private static final String KEY_CHEAT_USED = "cheatUsed";
    private static final String KEY_ANSWERED = "answered";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mScoreTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_luzon, true),
            new Question(R.string.question_visayas, true),
            new Question(R.string.question_mindanao, false),
            new Question(R.string.question_manila, true),
            new Question(R.string.question_davao, true),
            new Question(R.string.question_palawan, true),
            new Question(R.string.question_boracay, false),
            new Question(R.string.question_cebu, true),
    };

    private int mCurrentIndex = 0;
    private int mScore = 0;
    private boolean[] mCheatedQuestions = new boolean[mQuestionBank.length];
    private boolean[] mAnsweredQuestions = new boolean[mQuestionBank.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // Restore saved state if available
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mScore = savedInstanceState.getInt(KEY_SCORE, 0);
            boolean[] cheated = savedInstanceState.getBooleanArray(KEY_CHEAT_USED);
            if (cheated != null) {
                mCheatedQuestions = cheated;
            }
            boolean[] answered = savedInstanceState.getBooleanArray(KEY_ANSWERED);
            if (answered != null) {
                mAnsweredQuestions = answered;
            }
            Log.d(TAG, "State restored: index=" + mCurrentIndex + ", score=" + mScore);
        }

        // Initialize UI views
        mQuestionTextView = findViewById(R.id.question_text_view);
        mScoreTextView = findViewById(R.id.score_text_view);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);
        mCheatButton = findViewById(R.id.cheat_button);

        // Set up button listeners
        mTrueButton.setOnClickListener(v -> checkAnswer(true));
        mFalseButton.setOnClickListener(v -> checkAnswer(false));
        mNextButton.setOnClickListener(v -> nextQuestion());
        mPrevButton.setOnClickListener(v -> prevQuestion());
        mCheatButton.setOnClickListener(v -> cheat());

        updateQuestion();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_SCORE, mScore);
        outState.putBooleanArray(KEY_CHEAT_USED, mCheatedQuestions);
        outState.putBooleanArray(KEY_ANSWERED, mAnsweredQuestions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with requestCode=" + requestCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mCheatedQuestions[mCurrentIndex] =
                    data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
        }
    }

    /**
     * Updates the UI to display the current question.
     */
    private void updateQuestion() {
        if (mCurrentIndex >= mQuestionBank.length) {
            showFinalScore();
            return;
        }

        Question question = mQuestionBank[mCurrentIndex];
        mQuestionTextView.setText(question.getTextResId());
        updateScore();

        // Disable answer buttons if question was already answered
        boolean isAnswered = mAnsweredQuestions[mCurrentIndex];
        mTrueButton.setEnabled(!isAnswered);
        mFalseButton.setEnabled(!isAnswered);

        // Disable cheat button if already used on this question
        mCheatButton.setEnabled(!mCheatedQuestions[mCurrentIndex]);
    }

    /**
     * Updates the score display.
     */
    private void updateScore() {
        mScoreTextView.setText(getString(R.string.score_format, mScore, mQuestionBank.length));
    }

    /**
     * Checks if the user's answer is correct.
     * Prevents duplicate scoring by tracking answered questions.
     *
     * @param userAnswer the user's answer (true or false)
     */
    private void checkAnswer(boolean userAnswer) {
        if (mCurrentIndex >= mQuestionBank.length) {
            return;
        }

        // Prevent duplicate scoring - ignore if question was already answered
        if (mAnsweredQuestions[mCurrentIndex]) {
            Toast.makeText(this, R.string.already_answered_toast, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Answer ignored: question already answered at index " + mCurrentIndex);
            return;
        }

        Question question = mQuestionBank[mCurrentIndex];
        boolean correctAnswer = question.isAnswer();

        // Mark this question as answered
        mAnsweredQuestions[mCurrentIndex] = true;

        int messageResId;
        if (mCheatedQuestions[mCurrentIndex]) {
            messageResId = R.string.judgment_toast_cheater;
        } else if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast;
            mScore++;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Answer checked: user=" + userAnswer + ", correct=" + correctAnswer +
                ", cheated=" + mCheatedQuestions[mCurrentIndex] + ", score=" + mScore);

        // Disable answer buttons after answering
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    /**
     * Advances to the next question.
     */
    private void nextQuestion() {
        mCurrentIndex++;
        updateQuestion();
    }

    /**
     * Goes back to the previous question.
     */
    private void prevQuestion() {
        if (mCurrentIndex > 0) {
            mCurrentIndex--;
        }
        updateQuestion();
    }

    /**
     * Launches the CheatActivity to reveal the correct answer.
     */
    private void cheat() {
        Question question = mQuestionBank[mCurrentIndex];
        Intent intent = CheatActivity.newIntent(QuizActivity.this, question.isAnswer());
        startActivityForResult(intent, REQUEST_CODE_CHEAT);
    }

    /**
     * Displays the final score when the quiz is complete.
     */
    private void showFinalScore() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        mCheatButton.setEnabled(false);
        mNextButton.setEnabled(false);

        String finalMessage = getString(R.string.final_score_message, mScore, mQuestionBank.length);
        Toast.makeText(this, finalMessage, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Quiz completed. Final score: " + mScore + "/" + mQuestionBank.length);
    }
}
