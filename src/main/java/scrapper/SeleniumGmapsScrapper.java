package scrapper;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.Getter;
import lombok.Setter;
import scrapperIO.CsvReaderWriter;
import scrapperModel.GmapsSERPModel;

@Getter
@Setter
public class SeleniumGmapsScrapper extends SeleniumScrapper {

	private ChromeOptions options;

	private WebDriverWait webDriverWait;

	private Actions actions;

	private void prepareWebDriver() {

		options = new ChromeOptions();

		options.addArguments("--remote-allow-origins=*");

		driver = new ChromeDriver(options);

		webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));

		actions = new Actions(driver);

	}

	public void openBrowserWithGMaps() {

		String query = super.getQuery().getQuery().replaceAll(" ", ",+");

		driver.get("https://www.google.com/maps/search/" + query +","+ "?hl=en");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		driver.manage().window().maximize();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		driver.findElement(By.xpath(
				"//*[@id=\"yDmH0d\"]/c-wiz/div/div/div/div[2]/div[1]/div[3]/div[1]/div[1]/form[1]/div/div/button/div[1]"));
		driver.findElement(By.xpath(
				"//*[@id=\"yDmH0d\"]/c-wiz/div/div/div/div[2]/div[1]/div[3]/div[1]/div[1]/form[1]/div/div/button"))
				.click();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String secondQuery1 = query + ",+" + super.getQuery().getSecondQuery();

//		String secondQuery = super.getQuery().getSecondQuery().replaceAll(" ", "+");

		String zoomLevel = super.getQuery().getZoomLevel();

		driver.navigate().to(prepareSearchQuery(secondQuery1, setZoomLevel(driver.getCurrentUrl(), zoomLevel)));

		System.out.println(driver.getCurrentUrl());

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String prepareSearchQuery(String query, String coordinatesWithZoomLevel) {

		return "https://www.google.com/maps/search/" + query + "/" + coordinatesWithZoomLevel;

	}

	public String setZoomLevel(String link, String zoomLevel) {

		String StringToReplace = link.split("/@")[1].split("/")[0].split(",")[2];

		link = link.replace(StringToReplace, zoomLevel + "z");

		link = link.split("/")[6] + "?hl=en";

		return link;

	}

	public void loadSerp() {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		String endElementXpath = "//span[@class='HlvSq']";

		WebElement element = null;

		while (true) {
			
			while (super.isPause()) {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			if(super.isRunning() == false) {
				
				break;
				
			}

			element = driver.findElement(
					By.xpath("//*[@id=\"QA0Szd\"]/div/div/div[1]/div[2]/div/div[1]/div/div/div[2]/div[1]"));

			if (element.isDisplayed()) {

				js.executeScript("arguments[0].scrollBy(0, 5000);", element);

				if (driver.findElements(By.xpath(endElementXpath)).size() > 0) {

					if (driver.findElement(By.xpath(endElementXpath)).getText()
							.contains("You've reached the end of the list.")) {
						break;

					}

				}

			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void iterateThroughElementsAndWrite() {

//		List<WebElement> elementsToClick = driver
//				.findElements(By.xpath("//*[@class=\"TFQHme \"]/following-sibling::div"));
		List<WebElement> elementsToClick = driver
				.findElements(By.xpath("//*[@class=\"hfpxzc\"]"));

//		List<WebElement> elementsToClick = driver.findElements(By.xpath("//*[@role=\"article\"]"));

//		List<WebElement> elementsToClick = driver.findElements(By.xpath("//*[@id=\"QA0Szd\"]"));
		
		System.out.println(elementsToClick.size() + " elements to click size");

		Iterator<WebElement> webElementsToClick = elementsToClick.iterator();

		WebElement elementToClick = null;

		List<WebElement> elementsToExtractData = null;

		List<WebElement> title = null;

		List<WebElement> websiteElement = null;

		GmapsSERPModel gmapsSERPModel = null;

		while (webElementsToClick.hasNext()) {

			while (super.isPause()) {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			if(super.isRunning() == false) {
				
				break;
				
			}

			elementToClick = webElementsToClick.next();

			if (isClickable(elementToClick, driver)) {

				actions.moveToElement(elementToClick).click().perform();

			}

			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			elementsToExtractData = driver.findElements(By.xpath("//*[@class=\"Io6YTe fontBodyMedium kR99db \"]"));

			title = driver.findElements(By.xpath("//*[@class=\"DUwDvf fontHeadlineLarge\"]"));

			websiteElement = driver.findElements(By.xpath("//*[@data-item-id=\"authority\"]"));

			gmapsSERPModel = new GmapsSERPModel();

			if (title.size() > 0) {

				gmapsSERPModel.setTitle(title.get(0).getText());

			}

			if (elementsToExtractData.size() > 3) {

				gmapsSERPModel.setAddress(elementsToExtractData.get(0).getText());

			}

			if (websiteElement.size() > 0) {

				gmapsSERPModel.setWebsite(websiteElement.get(0).getAttribute("href"));

			}

			for (WebElement numberElement : elementsToExtractData) {

				if (numberElement.getText().matches("([0-9+-]*\\)*\\(*\\s*)+")) {

					gmapsSERPModel.setPhoneNumber(numberElement.getText());

				}

			}

			if (getWriteToPath() != null) {

				writeSerpToCsvFile(gmapsSERPModel);

			}

			if (super.getSqliteDatabaseConnection() != null) {

				super.getSqliteDatabaseConnection().insertIntoDatabase(gmapsSERPModel);

			}
			
			super.count++;

		}

	}

	private boolean isClickable(WebElement el, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.elementToBeClickable(el));
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	private void writeSerpToCsvFile(GmapsSERPModel gmapsSERPModel) {

		csvReaderWriter.writeArrayLineToCsv(gmapsSERPModel.fieldsValuesToArray(), getWriteToPath());

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

		csvReaderWriter = new CsvReaderWriter();

		prepareWebDriver();

		openBrowserWithGMaps();

		loadSerp();

		iterateThroughElementsAndWrite();

	}

}
