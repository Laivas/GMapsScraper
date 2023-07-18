package scrapperGuiHandlers;

import lombok.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.time.DurationFormatUtils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import scrapper.Query;
import scrapper.QueryImpl;
import scrapper.SeleniumGmapsScrapper;
import scrapper.SeleniumScrapper;
import scrapperDatabase.SqliteDatabaseConnection;
import scrapperGui.MainPane;
import scrapperIO.XmlReaderWriter;
import scrapperModel.DataPaneSelection;
import scrapperUtil.FileNameGenerator;

@Getter
@Setter
public class MainPaneHandler implements EventHandler<ActionEvent> {

	private MainPane mainPane;

	private SeleniumScrapper seleniumScrapper;

	public MainPaneHandler(MainPane mainPane) {

		this.mainPane = mainPane;

	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

		if (event.getSource() == mainPane.getPreparedSearchRadioButton()
				|| event.getSource() == mainPane.getManualSearchRadioButton()) {

			uiSelectionSwitcher();

		}

		if (event.getSource() == mainPane.getCsvUrlsListButton()) {

			csvUrlListButtonHandler();

		}

		if (event.getSource() == mainPane.getStartButton()) {

			startHandler();

		}

		if (event.getSource() == mainPane.getStopButton()) {

			stopHandler();

		}

		if (event.getSource() == mainPane.getPauseResumeButton()) {

			pauseResumeHandler();

		}

	}

	public void csvUrlListButtonHandler() {

		FileChooser fileChooser = new FileChooser();

		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		fileChooser.getExtensionFilters().add((new ExtensionFilter("Csv files", "*.csv")));

		File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

		if (file != null) {

			mainPane.getCsvUrlsListField().setText(file.getAbsolutePath());

		}

	}

	public void startHandler() {

		if (seleniumScrapper != null) {

			if (seleniumScrapper.isRunning()) {

				seleniumScrapper.setRunning(false);

			}

		}

		if (seleniumScrapper == null) {

			seleniumScrapper = new SeleniumGmapsScrapper();

		}

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (seleniumScrapper != null) {

					if (seleniumScrapper.isRunning() == false) {

						getSelectedSaveDataPath();

						seleniumScrapper.setRunning(true);

						updateProgress(seleniumScrapper);

						Query query = buildManualSearchQuery(mainPane);

						seleniumScrapper.setQuery(query);

						seleniumScrapper.start();

						seleniumScrapper.stop();

					}

				}

			}

		});

		thread.start();

	}

	public void stopHandler() {

		if (seleniumScrapper != null) {

			if (seleniumScrapper.isRunning()) {

				seleniumScrapper.stop();

				seleniumScrapper.getDriver().quit();

			}

		}

	}

	public void pauseResumeHandler() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (seleniumScrapper != null) {

					if (seleniumScrapper.isRunning()) {

						if (mainPane.getPauseResumeButton().getText().equals("Pause")) {

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									mainPane.getPauseResumeButton().setText("Resume");

								}

							});

							seleniumScrapper.pause();

						}

						if (mainPane.getPauseResumeButton().getText().equals("Resume")) {

							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									mainPane.getPauseResumeButton().setText("Pause");

								}

							});

							seleniumScrapper.resume();

						}

					}

				}

			}

		});

		thread.start();

	}

	public void uiSelectionSwitcher() {

		if (mainPane.getManualSearchRadioButton().isSelected()) {

			mainPane.getCsvUrlsListButton().setDisable(true);

			mainPane.getFirstQuery().setDisable(false);

			mainPane.getSecondQuery().setDisable(false);

		}

		if (mainPane.getPreparedSearchRadioButton().isSelected()) {

			mainPane.getCsvUrlsListButton().setDisable(false);

			mainPane.getFirstQuery().setDisable(true);

			mainPane.getSecondQuery().setDisable(true);

		}

	}

	private Query buildManualSearchQuery(MainPane mainPane) {

		Query query = new QueryImpl();

		if (mainPane.getFirstQuery() != null && mainPane.getSecondQuery() != null) {

			query.setQuery(mainPane.getFirstQuery().getText());

			query.setSecondQuery(mainPane.getSecondQuery().getText());

			query.setZoomLevel(mainPane.getZoomLevelQuery().getText());

		}

		return query;

	}

	public void getSelectedSaveDataPath() {

		XmlReaderWriter xmlReaderWriter = new XmlReaderWriter();

		DataPaneSelection dataPaneSelection = new DataPaneSelection();

		dataPaneSelection = xmlReaderWriter.fromXml(dataPaneSelection, "dataPaneSelection.xml");

		if (dataPaneSelection.isSaveCsv()) {

			if (dataPaneSelection.isGenerateCsvFileName()) {

				FileNameGenerator fileNameGenerator = new FileNameGenerator();

				seleniumScrapper.setWriteToPath(Paths.get(dataPaneSelection.getCsvFolderDir() + File.separator
						+ fileNameGenerator.generateDateFileName() + ".csv"));

			}

			if (dataPaneSelection.isGenerateCsvFileName() == false && dataPaneSelection.getCsvFileName() != null
					&& !dataPaneSelection.getCsvFileName().isEmpty()) {

				seleniumScrapper.setWriteToPath(Paths.get(dataPaneSelection.getCsvFolderDir() + File.separator
						+ dataPaneSelection.getCsvFileName() + ".csv"));

			}
		}

		if (dataPaneSelection.isSaveSqliteDb()) {

			if (dataPaneSelection.isGenerateSqliteDbFileName()) {

				FileNameGenerator fileNameGenerator = new FileNameGenerator();

				SqliteDatabaseConnection sqliteDatabaseConnection = new SqliteDatabaseConnection(
						dataPaneSelection.getSqliteFolderDir() + File.separator,
						fileNameGenerator.generateDateFileName() + ".db");

				seleniumScrapper.setSqliteDatabaseConnection(sqliteDatabaseConnection);

			}

			if (dataPaneSelection.isGenerateSqliteDbFileName() == false
					&& dataPaneSelection.getSqliteDbFileName() != null
					&& !dataPaneSelection.getSqliteDbFileName().isEmpty()) {

				SqliteDatabaseConnection sqliteDatabaseConnection = new SqliteDatabaseConnection(
						dataPaneSelection.getSqliteFolderDir() + File.separator,
						dataPaneSelection.getSqliteDbFileName() + ".db");

				seleniumScrapper.setSqliteDatabaseConnection(sqliteDatabaseConnection);

			}

		}
		if (dataPaneSelection.isSaveSqliteDb() == false && dataPaneSelection.isSaveCsv() == false) {

			FileNameGenerator fileNameGenerator = new FileNameGenerator();

			seleniumScrapper.setWriteToPath(Paths.get(System.getProperty("user.home") + File.separator
					+ fileNameGenerator.generateDateFileName() + ".csv"));

		}

	}

	private String elapsedTimeFrom(long millis) {

		return DurationFormatUtils.formatDuration(millis, "HH:mm:ss").replaceAll("-", "");

	}

	private String getStatus(SeleniumScrapper scrapper) {

		if (scrapper.isRunning() == false) {

			return "terminated";

		}

		if (scrapper.isRunning() && scrapper.isPause()) {

			return "paused";

		}

		if (scrapper.isRunning() || scrapper.isPause() == false) {

			return "running";

		}

		return null;

	}

	public void updateProgress(SeleniumScrapper scrapper) {

		Timer timer = new Timer();

		long start = System.currentTimeMillis();

		timer.schedule(new TimerTask() {

//			int count = 0;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (scrapper.isRunning() == true) {

					mainPane.getStatusField().setText(getStatus(scrapper));

					mainPane.getTimeElapsedField().setText(elapsedTimeFrom((start - System.currentTimeMillis())));

					mainPane.getProgressField().setText("Writen: " + String.valueOf(scrapper.getCount()));

				}

				if (scrapper.isRunning() == false) {

					mainPane.getStatusField().setText(getStatus(scrapper));

					mainPane.getTimeElapsedField().setText(elapsedTimeFrom((start - System.currentTimeMillis())));

					mainPane.getProgressField().setText("Writen: " + String.valueOf(scrapper.getCount()));

					timer.cancel();

				}

			}

		}, 0, 500);

	}

}
