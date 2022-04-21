package scrapper.mavenproject.webdriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class webdriver {

	private static final String USER_AGENT =
            "user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36";
	
	private WebDriver webdriver;
	private WebDriverWait webdriverwait;
	private JavascriptExecutor executor;
	
	 public webdriver() {
	        ChromeOptions options = new ChromeOptions();
	        options.setHeadless(true);
	        options.addArguments(USER_AGENT); // trick to make headless work

	        webdriver = new ChromeDriver(options);
	        webdriverwait = new WebDriverWait(webdriver, 5);
	        executor = (JavascriptExecutor) webdriver;
	    }
	 
	 public List<String> prepareTwoTabs() {
		 webdriver.get("https://www.google.com");
		 executor.executeScript("window.open()");
	        return new ArrayList<> (webdriver.getWindowHandles());
	    }
	 
	 public List<WebElement> getElementListByScrollingDown(String url, String xpath, String tab) {
	        switchTab(tab);
	        webdriver.get(url);
	        executor.executeScript("window.scrollBy(0,600)");
	        return webdriver.findElements(By.xpath(xpath));
	    }
	 
	 public void getWebpage(String path, String tab) {
	        switchTab(tab);
	        webdriver.get(path);
	    }

	    public void waitOnElement(String xpath) {
	    	webdriverwait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath(xpath)));
	    }

	    public void removeElement(String element) {
	    	executor.executeScript(String.format("document.querySelector('%s').parentElement.remove();", element));
	    }

	    public void scrollDownSmall() {
	    	executor.executeScript("window.scrollBy(0,300)");
	    }

	    public String getText(String xpath) {
	        return webdriver.findElements(By.xpath(xpath)).isEmpty()
	                ? "" : webdriver.findElement(By.xpath(xpath)).getText();
	    }

	    public String getText(String xpath, String attribute) {
	        return webdriver.findElements(By.xpath(xpath)).isEmpty()
	                ? "" : webdriver.findElement(By.xpath(xpath)).getAttribute(attribute);
	    }

	    public void switchTab(String tab) {
	    	webdriver.switchTo().window(tab);
	    }

	    public void quit() {
	    	webdriver.quit();
	    }
	
}
