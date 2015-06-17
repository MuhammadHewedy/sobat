package com.googlecode.sobat.examples;

public class QuestionDetail {
	private long id;
	private String question;
	private String correctAnswer;
	private String AnswerA;
	private String AnswerB;
	private String AnswerC;
	private String AnswerD;
	private byte[] image;
	
	private void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public String getAnswerA() {
		return AnswerA;
	}
	public void setAnswerA(String answerA) {
		AnswerA = answerA;
	}
	public String getAnswerB() {
		return AnswerB;
	}
	public void setAnswerB(String answerB) {
		AnswerB = answerB;
	}
	public String getAnswerC() {
		return AnswerC;
	}
	public void setAnswerC(String answerC) {
		AnswerC = answerC;
	}
	public String getAnswerD() {
		return AnswerD;
	}
	public void setAnswerD(String answerD) {
		AnswerD = answerD;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
}
