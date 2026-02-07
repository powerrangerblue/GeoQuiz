package com.example.geoquiz;

/**
 * Question model class that stores a geography question and its correct answer.
 */
public class Question {
    private int textResId;  // Resource ID for the question text
    private boolean answer; // True or False answer

    /**
     * Constructor to create a Question object.
     *
     * @param textResId the resource ID of the question text
     * @param answer    the correct answer (true or false)
     */
    public Question(int textResId, boolean answer) {
        this.textResId = textResId;
        this.answer = answer;
    }

    /**
     * Gets the resource ID of the question text.
     *
     * @return the text resource ID
     */
    public int getTextResId() {
        return textResId;
    }

    /**
     * Sets the resource ID of the question text.
     *
     * @param textResId the text resource ID to set
     */
    public void setTextResId(int textResId) {
        this.textResId = textResId;
    }

    /**
     * Gets the correct answer for this question.
     *
     * @return the correct answer (true or false)
     */
    public boolean isAnswer() {
        return answer;
    }

    /**
     * Sets the correct answer for this question.
     *
     * @param answer the correct answer to set
     */
    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
