package scrapperGui;

import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.stage.Stage;

public class MainStageWindow extends Application {

	private Scene scene;

	private TabPane tabPane;

	private Tab tab;

	private MainPane mainPane;

	private DataPane dataPane;

	public static void main(String[] args) {

		launch(args);

	}

	public void initPanes() {

		mainPane = new MainPane();

		dataPane = new DataPane();

	}

	public TabPane addTabPane() {

		tabPane = new TabPane();

		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		initPanes();

		tab = new Tab("Main", mainPane);

		tabPane.getTabs().add(tab);

		tab = new Tab("Data", dataPane);

		tabPane.getTabs().add(tab);

		return tabPane;

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		scene = new Scene(addTabPane(), 650, 550);

		loadXml();

		scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

		primaryStage.setTitle("GMaps Scrapper");

		primaryStage.setScene(scene);

		primaryStage.setResizable(false);

		primaryStage.show();

	}

	@Override
	public void stop() {

		System.exit(0);

	}

	public void loadXml() {

		dataPane.getDataPaneHandler().loadDataPaneSelectionFromXml();

	}

}
