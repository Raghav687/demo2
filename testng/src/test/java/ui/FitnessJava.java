package ui;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FitnessJava {
	public static void fitness(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("ContextualHotkey_27")).click();
		driver.findElement(By.xpath("//*[@id=\'mnintrnlbnr\']/ul/li[3]/a/span[2]")).click();
		List<WebElement> options=driver.findElements(By.xpath("//*[@id='mnintrnlbnr']/ul/li/a/span[2]"));
		for (int i=0;i<options.size();i++)
		{
			System.out.println(options.get(i).getAttribute("title"));
		}
	}
}
