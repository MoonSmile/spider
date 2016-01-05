package ss;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections.map.StaticBucketMap;

import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;

public class SQLiteHelper {

	public static SQLiteHelper mySqLiteHelper = new SQLiteHelper();
	Queue<Article> articles;
	Connection conn;
	PreparedStatement prep;
	int count = 0;

	private SQLiteHelper() {
		try {
			articles = new LinkedBlockingQueue<Article>();
			Init();
			OnStarted();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void OnStarted() throws InterruptedException {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					synchronized (articles) {
						while (articles.size() != 0) {
							Article cur_article = articles.remove();
							try {
								Insert(cur_article);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

	public void Init() throws ClassNotFoundException, SQLException {
		Class class1 = Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:article.db");
		Statement stat = conn.createStatement();
		ResultSet rsTables = conn.getMetaData().getTables(null, null, "paper", null);
		if (rsTables.next()) {
			System.out.println("表存在");
		} else {
			// stat.executeUpdate("drop table if exists paper;");
			stat.executeUpdate("create table paper (username, article);");
		}

		prep = conn.prepareStatement("insert into paper values (?, ?);");
	}

	public void addArticle(Article article) {
		synchronized (articles) {
			articles.add(article);
		}

	}

	public void Insert(Article article) throws SQLException {
		prep.setString(1, article.user_name);
		prep.setString(2, article.article_name);
		prep.addBatch();
		conn.setAutoCommit(false);
		prep.executeBatch();
		conn.setAutoCommit(true);
	}
}
