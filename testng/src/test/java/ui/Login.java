package ui;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Login {
	WebDriverWait wait;
	Actions action;
	int flag = 0;
	String b = "";
	WebDriver driver;
	@Test(priority = 0)
	public void startAndRemoveAd() throws InterruptedException, IOException {
	    
		
	    PropertiesJava file = new PropertiesJava();
	    String getBrowser = file.properties("getBrowser");
	    String getWebsiteURL = file.properties("getWebsiteURL");
		String getInputLocation = file.properties("getInputLocation");
		String getInputValue = file.properties("getInputValue");
		String getDriverSetup = file.properties("getDriverSetup");
		
		
		if(getDriverSetup.equalsIgnoreCase("NormalDriverSetup")) {
			DriverSetup getWebDriver = new DriverSetup();
			driver = getWebDriver.getDriver(getBrowser);
		}
		else if(getDriverSetup.equalsIgnoreCase("GridDriverSetup")) {
			GridDriverSetup getGridDriver = new GridDriverSetup();
			driver = getGridDriver.getGridDriver(getBrowser);
		}
		
	    driver.get(getWebsiteURL);
		driver.manage().window().maximize();
		Actions actions = new Actions(driver);
		
		driver.findElement(By.id("city")).sendKeys(getInputLocation);
		driver.findElement(By.id("srchbx")).sendKeys(getInputValue, Keys.ENTER);
		
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.pollingEvery(Duration.ofSeconds(5));
		WebElement cross = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"best_deal_div\"]/section/span")));
		actions.moveToElement(cross).click().perform();
		
		ScreenShotJava screenShotPage = new ScreenShotJava();
		screenShotPage.screenshot(driver);
	
	}
	@Test(priority = 1)
	public void setupDriver() throws InterruptedException, IOException
	{
		// TODO Auto-generated method stub
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("JustDial Data");
		
		Object[][] obj = new Object[10][3];
		obj[0][0] = "Name";
		obj[0][1] = "Address";
		obj[0][2] = "PhoneNumber";
		int rowCount = 1;
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		List<WebElement> elements = driver.findElements(By.className("store-details"));
		String[] phone = null;
		String phoneString = null;
		String[] stringArray = new String[0];
		int j = 0;
		float rat[]=new float[elements.size()]; 
		for(int i=0;i<elements.size();i++) {
			String temp =elements.get(i).findElement(By.className("green-box")).getText();
			rat[i]= Float.parseFloat(temp);
			String Vote = elements.get(i).findElement(By.xpath("//*[@id=\"bcard"+i+"\"]/div[1]/section/div[1]/p[1]/a/span[3]")).getText();
			String numberOnly= Vote.replaceAll("[^0-9]", "");
			int VoteInteger = Integer.parseInt(numberOnly);
			List<WebElement> PhoneNumberString =  driver.findElements(By.className("mobilesv"));
			if(flag == 0) {
				for(int k = 0;k<PhoneNumberString.size();k++) {
					phoneString = PhoneNumberString.get(k).getAttribute("class").split("-")[1];
					regexMethod r = new regexMethod();
					String temp2 = r.MobileClass(phoneString);
					b += temp2;
				}
				flag = 1;
				while(j<b.length()) {
					if(b.charAt(j) == '0') {
						int count = 0;  
						String var = "";
						while(j<b.length() && count <= 10) {
							var += b.charAt(j); 
							j++;count++;
						}
						stringArray = Arrays.copyOf(stringArray,stringArray.length+1);
						stringArray[stringArray.length - 1] = var;
					}
					else if(b.charAt(j) == '+') {
						int count = 0;
						String var = "";
						while(j<b.length() && count <= 15) {
							var += b.charAt(j); 
							j++;count++;
						}
						stringArray = Arrays.copyOf(stringArray,stringArray.length+1);
						stringArray[stringArray.length - 1] = var;
					}
				}
			}
			if(rat[i]>=4.0 && VoteInteger > 20) {
				System.out.print("Name: ");
				System.out.print(elements.get(i).findElement(By.className("lng_cont_name")).getText());
				System.out.println();
				obj[rowCount][0] = elements.get(i).findElement(By.className("lng_cont_name")).getText();
				
				System.out.print("Address: ");
				System.out.print(elements.get(i).findElement(By.className("cont_sw_addr")).getText());
				System.out.println();
				obj[rowCount][1] = elements.get(i).findElement(By.className("cont_sw_addr")).getText();
				
				System.out.print("PhoneNumber: "); 
				System.out.print(stringArray[i]);
				System.out.println();
				System.out.println();
				obj[rowCount][2] = stringArray[i];
			}
			rowCount++;
		}
		
		int rows = obj.length;
		int cols = obj[0].length;
		
		
		for(int k=0;k<rows;k++) {
			XSSFRow row = sheet.createRow(k);
			for(int p=0;p<cols;p++) {
				XSSFCell cell = row.createCell(p);
				Object value = obj[k][p];
				if(value instanceof String) {
					cell.setCellValue((String)value);
				}
				if(value instanceof Integer) {
					cell.setCellValue((Integer)value);
				}
			}
		}
		
		String filePath = ".\\ExcelReport\\JustDialService.xlsx";
		FileOutputStream outstream = new FileOutputStream(filePath);
		workbook.write(outstream);
		outstream.close();
	}
	@Test(priority = 2)
	public void Listing() throws IOException{
		FreeListingJava freeListingJava = new FreeListingJava();
		String ErrMessage = freeListingJava.Message(driver);
		System.out.println("Error Message After entering wrong value in Input Field: " + ErrMessage);
		System.out.println();
		
		ScreenShotJava screenShotPage = new ScreenShotJava();
		screenShotPage.screenshot(driver);
		
		driver.navigate().back();
	}
	@SuppressWarnings("static-access")
	@Test(priority = 3)
	public void Fitness() throws IOException {
		
		driver.navigate().back();
		
		FitnessJava fitnessJava = new FitnessJava();
		fitnessJava.fitness(driver);
		
		ScreenShotJava screenShotPage = new ScreenShotJava();
		screenShotPage.screenshot(driver);
	}
	@Test(priority = 4)
	public void closeBrowser() {
		driver.close();	
	}
}

