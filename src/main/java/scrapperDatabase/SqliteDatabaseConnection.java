package scrapperDatabase;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import lombok.*;
import scrapperModel.GmapsSERPModel;

@Setter
@Getter
public class SqliteDatabaseConnection {
	
//	private static final String userHome = System.getProperty("user.home").replaceAll("\\\\", "/");
	
	private String databaseLocation = "/xmlSettings/";
	
	private String sqlLite = "jdbc:sqlite:";
	
	private Configuration configuration;
	
	private Session session;
	
	private Transaction transaction;
	
	public SqliteDatabaseConnection(String databaseLocation, String databaseName) {
		
		this.databaseLocation = databaseLocation;
		
		sqliteConfigurationConnection(databaseName);
		
	}
	
	public Configuration sqliteConfigurationConnection(String databaseName) {
		
			configuration = new Configuration()
					
				.addAnnotatedClass(GmapsSERPModel.class)
//				.addAnnotatedClass(ShipDetails.class)
				
			    .setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect")
			    .setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC")
			    .setProperty("hibernate.connection.url", "jdbc:sqlite:" + getDatabaseLocation() + databaseName)
//			    .setProperty("hibernate.connection.username", user)
//			    .setProperty("hibernate.connection.password", password)
			    .setProperty("hibernate.order_updates", "true")
				.setProperty("hibernate.hbm2ddl.auto", "update");
			
			return configuration;
		
	}
	
	public Session session() {
		
		session = configuration.buildSessionFactory().openSession();
		
		return session;
		
	}
	
	public void insertIntoDatabase(GmapsSERPModel gmapsSERPModel) {
		
		transaction = session().beginTransaction();
		
		session.persist(gmapsSERPModel);
		
		transaction.commit();
		
		
	}
	
	public void sessionClose() {
		
		if(this.session != null) {
			
			this.session.close();
			
		}
		
	}
	

}
