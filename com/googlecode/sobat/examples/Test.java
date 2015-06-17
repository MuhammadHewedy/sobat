package com.googlecode.sobat.examples;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import com.googlecode.sobat.Query;
import com.googlecode.sobat.Session;
import com.googlecode.sobat.SessionManager;
import com.googlecode.sobat.ql.From;
import com.googlecode.sobat.ql.OrderBy;
import com.googlecode.sobat.ql.Projection;
import com.googlecode.sobat.ql.Where;
import com.googlecode.sobat.ql.OrderBy.Order;
import com.googlecode.sobat.ql.condition.Conditions;
import com.googlecode.sobat.ql.condition.CreateCondition.Type;

public class Test {
		
	public static void main(String[] args) throws Exception{
		
		CRUDExample1();
		CRUDExample2();
		CRUDExample3();
		CRUDExample4();
		CRUDExample1();
		CRUDExample5();
		QLExample1();
		QLExample2();
		createSQLQueryExample1();
		createSQLQueryExample2();
	}
	
	public static void CRUDExample1() throws SQLException, IOException{
		SessionManager mgr = SessionManager.getSessionManager();
		Session session =  mgr.newSession();
		session.beginTransaction();
		
		QuestionDetail qd = new QuestionDetail();
		qd.setQuestion("How are you ? ");
		qd.setAnswerA("AA");
		qd.setAnswerB("answerB");
		qd.setAnswerD("DDX");
		qd.setCorrectAnswer("Fine");
		InputStream imageStream = Test.class.getClassLoader().getResourceAsStream("com/googlecode/sobat/examples/image.png");
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int b = 0;
		while ( (b = imageStream.read()) != -1)
			byteStream.write(b);
		byte[] image = byteStream.toByteArray();
		qd.setImage(image);
		session.save(qd);

		session.commitTransaction();
		session.close();
	}
	
	public static void CRUDExample2() throws SQLException{
		SessionManager mgr = SessionManager.getSessionManager();
		Session session =  mgr.newSession();
		session.beginTransaction();
		
		QuestionDetail qd = session.get(QuestionDetail.class, new Long(1)); 
		qd.setAnswerB("Fine");
		session.update(qd);
		
		session.commitTransaction();
		session.close();
	}
	
	/**
	 * Dirty checking
	 * @throws SQLException
	 */
	public static void CRUDExample3() throws SQLException{
		SessionManager mgr = SessionManager.getSessionManager();
		Session session =  mgr.newSession();
		session.beginTransaction();
		
		QuestionDetail qd = session.get(QuestionDetail.class, new Long(1));
		
		session.commitTransaction();
		session.close();
		
		// update the detached object out of any session
		qd.setAnswerC("Answer C");
		
		Session session2 =  SessionManager.getSessionManager().newSession();
		session2.beginTransaction();
		// the second session determined that the detached object has changed (became dirty) and it updated it
		session2.update(qd);
		
		session2.commitTransaction();
		session2.close();
	}
	
	/**
	 * Delete
	 * @throws SQLException
	 */
	public static void CRUDExample4() throws SQLException{
		SessionManager mgr = SessionManager.getSessionManager();
		Session session =  mgr.newSession();
		session.beginTransaction();
		
		QuestionDetail qd = session.get(QuestionDetail.class, new Long(1));
		session.delete(qd);
		
		session.commitTransaction();
		session.close();
	}
	
	/**
	 * Get All
	 * @throws SQLException
	 */
	public static void CRUDExample5() throws SQLException{
		SessionManager mgr = SessionManager.getSessionManager();
		Session session =  mgr.newSession();
		session.beginTransaction();
		
		List<QuestionDetail> qdList = session.get(QuestionDetail.class);
		printObjectList(qdList);
		
		session.commitTransaction();
		session.close();
	}
	
	/**
	 * Query Language Example1 <br />
	 * It illustrates basic usage of Sobat Query Language (actually it is an API rather than a Language)
	 */
	public static void QLExample1(){
		
		From from = new From(QuestionDetail.class);
		Conditions conditions = new Conditions(from);
		conditions.addCondition(Type.Equals, "AnswerA", "AA");
		Where where = new Where(from , conditions);
		OrderBy orderBy = new OrderBy(where, "id", Order.ASC);
		OrderBy orderBy2 = new OrderBy(where, "question", Order.DESC);
		
		List<QuestionDetail> result = orderBy2.execute();
		printObjectList(result);
	}

	/**
	 * Query Language Example2 <br />
	 * It usage Illustrate projection
	 */
	public static void QLExample2() {
		
		From from = new From(QuestionDetail.class);
		Projection projection = new Projection(from, new String[] {"question", "correctAnswer", "AnswerA", "AnswerB", "AnswerA"});
		OrderBy orderBy = new OrderBy(projection, "id", Order.DESC);
		
		List<Object[]> result = orderBy.execute();
		printArrayList(result);
	}
	
	/**
	 * Native SQLQuery Example1 <br />
	 * shows how to use native SQL Queries (MYSQL, M$ SQL Server, Oracle, etc), although, this usage reduces application portability across
	 * Databases 
	 * @throws Exception
	 */
	public static void createSQLQueryExample1() throws Exception{
		SessionManager mgr = SessionManager.getSessionManager();
		Session session =  mgr.newSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery("SELECT quest, correctAns, QA, QB, QA FROM question_deatil WHERE QA =  'AA' ORDER BY id ASC",
				null);
		List<Object[]> list = query.execute();
		printArrayList(list);
		
		session.commitTransaction();
		session.close();		
	}
	
	public static void createSQLQueryExample2() throws Exception{
		
		SessionManager mgr = SessionManager.getSessionManager();
		Session session =  mgr.newSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery("SELECT * FROM question_deatil WHERE QA =  'AA' ORDER BY id ASC",
				QuestionDetail.class);
		List<QuestionDetail> result = query.execute();
		printObjectList(result);
		
		session.commitTransaction();
		session.close();		
	}
	
	public static void printObjectList(List<QuestionDetail> qdList) {
		for (QuestionDetail obj: qdList) 
			System.out.println(obj.getQuestion() + "\t-\t" +  obj.getCorrectAnswer() + "\t-\t" + obj.getAnswerA() + "\t-\t" +  obj.getAnswerB()
					+ "\t-\t" +  obj.getAnswerC() +"\t-\t" +   obj.getAnswerD());
	}
	
	public static void printArrayList(List<Object[]> qdList) {
		for (Object[] arr : qdList) {
			for (Object obj : arr)
				System.out.print(obj + "\t-\t");
			System.out.println();
		}
	}
	
}
