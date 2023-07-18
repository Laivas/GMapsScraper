package scrapper;

import java.nio.file.Path;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;

import lombok.Getter;
import lombok.Setter;
import scrapperDatabase.SqliteDatabaseConnection;
import scrapperIO.CsvReaderWriter;
import scrapperProxy.ProxyUtil;

@Getter
@Setter
public abstract class SeleniumScrapper {
	
	private boolean pause;

	private boolean running;
	
	private int delayInMs;
	
	private int numberOfThreads;
	
	private ProxyUtil proxyUtil;

	private Path writeToPath;
	
	private SqliteDatabaseConnection sqliteDatabaseConnection;
	
	private List<String[]> proxies;

	protected CsvReaderWriter csvReaderWriter;
	
	public int count;

	private Query query;
	
	protected ChromeDriver driver;
	
	public SeleniumScrapper() {

	}
	
	public abstract void start();

	public void stop() {

		setPause(false);

		setRunning(false);

	};
	
	public void pause() {

		setPause(true);

	}

	public void resume() {

		setPause(false);

	}
	
	protected void closeDatabaseConnection() {

		if (sqliteDatabaseConnection != null) {

			sqliteDatabaseConnection.sessionClose();

		}

	}

}
