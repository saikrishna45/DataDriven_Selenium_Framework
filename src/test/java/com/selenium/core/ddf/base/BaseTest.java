package com.selenium.core.ddf.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.selenium.core.ddf.util.ExtentManager;

public class BaseTest {
	
	public WebDriver driver;
	public Properties prop;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	
	public void init(){
		
		//initialize Properties file
				if(prop==null){
				   prop = new Properties();
					 try {
							FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//projectconfiguration.properties");
							prop.load(fs);
								
						} catch (Exception e) {
								
							e.printStackTrace();
						}
							
				}
				
	}
	
	public void openBrowser(String bType){
		
		
		if(bType.equals("Mozilla"))
			driver = new FirefoxDriver();
		
		else if (bType.equals("Chrome")){
			
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver_exe"));
			driver = new ChromeDriver();
		}
		
		else if (bType.equals("IE")){
			driver = new InternetExplorerDriver();
		}
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		
		
	}
	
	//finding element on page and returning it 
	
	public WebElement getElement(String locatorKey){
		WebElement e =null;
		
		try {
			if(locatorKey.endsWith("_id")){
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			}
			else if (locatorKey.endsWith("_name")){
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
			}
			else if (locatorKey.endsWith("_xpath")){
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			}
			else{
				
				reportFailure("Locator not found  " +locatorKey);
				Assert.fail("Locator not corret  " +locatorKey);
			}
			
		} catch (Exception e1) {
			
			reportFailure(e1.getMessage());
			e1.printStackTrace();
			Assert.fail("Failed the test " +e1.getMessage());
		}
		return e;
	}
	
	
	public void navigate(String urlKey){
		
		driver.get(prop.getProperty(urlKey));
	}
	
	public void type(String locatorKey, String data){
		getElement(locatorKey).sendKeys(data);
	
	}
	
	public void click(String locatorKey){
		
		getElement(locatorKey).click();
	}
	
	/***************************************Validations************************************************/
	
	public boolean verifyTitle(){
		return false;
	}
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementList = null;
		if(locatorKey.endsWith("_id")){
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		}
		else if (locatorKey.endsWith("_name")){
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		}
		else if (locatorKey.endsWith("_xpath")){
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		}
		else{
			
			reportFailure("Locator not found  " +locatorKey);
			Assert.fail("Locator not corret  " +locatorKey);
		}
		
		if(elementList.size()==0)
			return false;
		else
			return true;
	}
	
	public boolean verifyText(String locatorKey,String expectedTextKey){
		String actualText = getElement(locatorKey).getText().trim();
		String expectedText= prop.getProperty(expectedTextKey);
		if(actualText.equals(expectedText))
			return true;
		else
			return false;
	}
	
	
	/***********************************************Reporting**************************************/
	
	public void reportPass(String msg){
		
		test.log(LogStatus.PASS, msg);
	}
	
	public void reportFailure(String msg){
		
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
	}
	
	public void takeScreenShot(){
		
		//fileName of the screenshot
		
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_")+".png";
		//store screenshot in that file
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try{
			
			FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
			
		}catch(IOException e ){
			
			e.printStackTrace();
		}
		
		//put screenshot file in reports
		
		test.log(LogStatus.INFO, "Screen Shot  " +test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
	}



}