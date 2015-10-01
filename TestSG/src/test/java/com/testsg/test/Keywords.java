package com.testsg.test;

import static com.testsg.test.DriverScript.configProperties;
import static com.testsg.test.DriverScript.orProperties;
import static com.testsg.test.GetOSName.OsUtils.isWindows;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Screen;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.StandaloneSoapUICore;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.iface.Response;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

import autoitx4java.AutoItX;

public class Keywords {

	ExtentReports extrep = ExtentReports.get(Keywords.class);
	XMLKeyValue keyValues = new XMLKeyValue();
	final Logger logger = Logger.getLogger(Keywords.class);
	public WebDriver driver;
	AutoItX AutoIt = new AutoItX();
	Screen screen = new Screen();

	// Opens browsers IE, Firefox, Chrome by taking the config-browsertype
	// paramater
	public String openBrowser(String locator, String data, String scrpath) {

		// Chrome Driver Path
		System.setProperty("webdriver.chrome.driver", "ChromeDriver/chromedriver.exe");
		// Internet Explorer Path
		if (isWindows()) {
			File file = new File("IEDriver/IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		}

		logger.debug("Opening browser");

		if ("Mozilla".equals(data)) {
			driver = new FirefoxDriver();
		} else if (data.equals("IE")) {

			driver = new InternetExplorerDriver();

		} else if (data.equals("Chrome")) {

			driver = new ChromeDriver();
		}

		long implicitWaitTime = Long.parseLong(configProperties.getProperty("implicitwait"));
		driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
		if (configProperties.getProperty("screenshot_everystep").equals("N")) {
			extrep.log(LogStatus.INFO, "Browser opened successfully");
		} else {
			extrep.log(LogStatus.INFO, "Browser opened successfully", "Screen shot: ", scrpath + ".jpg");
		}

		return Constants.KEYWORD_PASS;

	}

	// Navigates to website using the url provided through data
	public String navigate(String locator, String data, String scrpath) {
		logger.debug("Navigating to " + "'" + data + "'");
		try {
			driver.navigate().to(data);
		} catch (Exception e) {

			extrep.log(LogStatus.FAIL, "Not able to navigate to " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to navigate";
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Navigated sucessfully");
		else
			extrep.log(LogStatus.PASS, "Navigated sucessfully", "Screen shot: ", scrpath + ".jpg");

		return Constants.KEYWORD_PASS;
	}

	// Clicks link in the website using the locator's Id locator
	public String clickLinkId(String locator, String data, String scrpath) {
		logger.debug("Clicking on link ");

		try {
			driver.findElement(By.id(orProperties.getProperty(keyValues.xmlp(locator)))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to click on " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click on link" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Clicks link in the website using the locator's xpath locator
	public String clickLinkXpath(String locator, String data, String scrpath) {
		logger.debug("Clicking on link ");

		try {
			driver.findElement(By.xpath(orProperties.getProperty(keyValues.xmlp(locator)))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to click on " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click on link" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Clicks link in the website using the locator's Css locator
	public String clickLinkCss(String locator, String data, String scrpath) {
		logger.debug("Clicking on link ");
		try {
			driver.findElement(By.cssSelector(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to navigate " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click on link " + locator + e.getMessage();
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Verifies the text of link in the website using the locator's xpath
	// locator
	public String verifyLinkText(String locator, String data, String scrpath) {
		String returnValue = Constants.KEYWORD_PASS;
		logger.debug("Verifying link Text");
		try {
			String actual = driver.findElement(By.xpath(keyValues.xmlp(locator))).getText();
			String expected = data;

			if (actual.equals(expected)) {
				if (configProperties.getProperty("screenshot_everystep").equals("N"))
					extrep.log(LogStatus.PASS, locator + " link text verified sucessfully");
				else
					extrep.log(LogStatus.PASS, locator + " link text verified sucessfully", "Screen shot: ",
							scrpath + ".jpg");
			} else {
				extrep.log(LogStatus.FAIL, "Could not verify link text " + locator, "Screen shot: ", scrpath + ".jpg");
				returnValue = Constants.KEYWORD_FAIL + " -- Link text not verified";
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not verify link text " + locator, "Screen shot: ", scrpath + ".jpg");
			returnValue = Constants.KEYWORD_FAIL + " -- Link text not verified" + e.getMessage();

		}
		return returnValue;

	}

	// Clicks button in the website using the locator's xpath locator
	public String clickButton(String locator, String data, String scrpath) {
		logger.debug("Clicking on Button");
		try {

			driver.findElement(By.xpath(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not click the button " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click on Button " + e.getMessage();
		}

		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String clickButtonCss(String locator, String data, String scrpath) {
		logger.debug("Clicking on Button");
		try {
			driver.findElement(By.cssSelector(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not click the button " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click on Button " + locator + e.getMessage();
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Verifies text of button in the website using the locator's xpath locator
	public String verifyButtonText(String locator, String data, String scrpath) {
		logger.debug("Verifying the button text");
		try {
			String actual = driver.findElement(By.xpath(keyValues.xmlp(locator))).getText();
			String expected = data;

			if (actual.equals(expected)) {
				if ("N".equals(configProperties.getProperty("screenshot_everystep")))
					extrep.log(LogStatus.PASS, "Button text verified sucessfully");
				else
					extrep.log(LogStatus.PASS, locator + " Button text verified successfully", "Screen shot: ",
							scrpath + ".jpg");
				return Constants.KEYWORD_PASS;
			} else {
				extrep.log(LogStatus.FAIL, "Could not verify button text " + locator, "Screen shot: ",
						scrpath + ".jpg");
				return Constants.KEYWORD_FAIL + locator + " Button text not verified " + actual + " -- " + expected;
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not verify button text " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " Button text not verified " + e.getMessage();
		}

	}

	// Selects the list item(provided through data) using the dropdown xpath
	// locator
	public String selectList(String locator, String data, String scrpath) {
		logger.debug("Selecting from list");

		try {
			if (!data.equals(Constants.RANDOM_VALUE)) {
				driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(data);
			} else {
				// logic to find a random value in list
				WebElement droplist = driver.findElement(By.xpath(keyValues.xmlp(locator)));
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random randomNumber = new Random();
				int index = randomNumber.nextInt(droplist_cotents.size());
				String selectedVal = droplist_cotents.get(index).getText();

				driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(selectedVal);
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not select from list " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Selected from the list sucessfully");
		else
			extrep.log(LogStatus.PASS, "Selected from the list successfully");
		return Constants.KEYWORD_PASS;
	}

	// Selects the list item by index(provided through data) using the dropdown
	// xpath locator
	public String selectListIndex(String locator, String data, String scrpath) {
		logger.debug("Selecting from list");
		int ind = (int) Double.parseDouble(data);
		try {
			if (!data.equals(Constants.RANDOM_VALUE)) {
				Select sel = new Select(driver.findElement(By.xpath(keyValues.xmlp(locator))));
				sel.selectByIndex(ind);
			} else {
				// logic to find a random value in list
				WebElement droplist = driver.findElement(By.xpath(keyValues.xmlp(locator)));
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random randomNumber = new Random();
				int index = randomNumber.nextInt(droplist_cotents.size());
				String selectedVal = droplist_cotents.get(index).getText();

				driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(selectedVal);
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not select from list " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Selected from the list sucessfully");
		else
			extrep.log(LogStatus.PASS, "Selected from the list successfully");
		return Constants.KEYWORD_PASS;
	}

	// Selects the list item by index(provided through data) using the dropdown
	// xpath locator
	public String selectListText(String locator, String data, String scrpath) {
		logger.debug("Selecting the list text " + "'" + data + "'");

		try {
			if (!data.equals(Constants.RANDOM_VALUE)) {
				Select sel = new Select(driver.findElement(By.xpath(keyValues.xmlp(locator))));
				sel.selectByVisibleText(data);
			} else {
				// logic to find a random value in list
				WebElement droplist = driver.findElement(By.xpath(keyValues.xmlp(locator)));
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random randomNumber = new Random();
				int index = randomNumber.nextInt(droplist_cotents.size());
				String selectedVal = droplist_cotents.get(index).getText();

				driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(selectedVal);
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not select from list " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Selected from the list sucessfully");
		else
			extrep.log(LogStatus.PASS, "Selected from the list successfully");
		return Constants.KEYWORD_PASS;
	}

	// Selects the list item by index(provided through data) using the dropdown
	// xpath locator
	public String selectListValue(String locator, String data, String scrpath) {
		logger.debug("Selecting from list value" + "'" + data + "'");

		try {
			if (!data.equals(Constants.RANDOM_VALUE)) {
				Select sel = new Select(driver.findElement(By.xpath(keyValues.xmlp(locator))));
				sel.selectByValue(data);
			} else {
				// logic to find a random value in list
				WebElement droplist = driver.findElement(By.xpath(keyValues.xmlp(locator)));
				List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
				Random randomNumber = new Random();
				int index = randomNumber.nextInt(droplist_cotents.size());
				String selectedVal = droplist_cotents.get(index).getText();

				driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(selectedVal);
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not select from list " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not select from list. " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Selected from the list sucessfully");
		else
			extrep.log(LogStatus.PASS, "Selected from the list successfully");
		return Constants.KEYWORD_PASS;
	}

	// Verifies all the list items(provided through data) using the dropdown
	// xpath locator with dropdown list in the application
	public String verifyAllListElements(String locator, String data, String scrpath) {
		logger.debug("Verifying the selection of the list");
		try {
			WebElement droplist = driver.findElement(By.xpath(keyValues.xmlp(locator)));
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));

			// extract the expected values from OR. properties
			String temp = data;
			String allElements[] = temp.split(",");
			// check if size of array == size if list
			if (allElements.length != droplist_cotents.size())
				return Constants.KEYWORD_FAIL + "- size of lists do not match";

			for (int index = 0; index < droplist_cotents.size(); index++) {
				if (!allElements[index].equals(droplist_cotents.get(index).getText())) {
					return Constants.KEYWORD_FAIL + "- Element not found - " + allElements[index];
				}
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not verify the list elements " + locator, "Screen shot: ",
					scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not verify the list elements. " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Verify the list elements sucessfully");
		else
			extrep.log(LogStatus.PASS, "Verify the list elements successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Verifies the selection of the list item(provided through data) using the
	// dropdown xpath locator
	public String verifyListSelection(String locator, String data, String scrpath) {
		logger.debug("Verifying all the list elements");
		try {
			String expectedVal = data;
			// System.out.println(driver.findElement(By.xpath(x.xmlp(locator))).getText());
			WebElement droplist = driver.findElement(By.xpath(keyValues.xmlp(locator)));
			List<WebElement> droplist_cotents = droplist.findElements(By.tagName("option"));
			String actualVal = null;
			for (int index = 0; index < droplist_cotents.size(); index++) {
				String selected_status = droplist_cotents.get(index).getAttribute("selected");
				if (selected_status != null)
					actualVal = droplist_cotents.get(index).getText();
			}

			if (actualVal != null && !actualVal.equals(expectedVal))
				return Constants.KEYWORD_FAIL + "Value not in list - " + expectedVal;

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " - Could not find list. " + e.getMessage();

		}

		return Constants.KEYWORD_PASS;

	}

	// Selects the radio button using the locator xpath locator
	public String selectRadio(String locator, String data, String scrpath) {
		logger.debug("Selecting a radio button");
		try {
			String temp[] = locator.split(Constants.DATA_SPLIT);
			driver.findElement(By.xpath(orProperties.getProperty(temp[0]) + data + orProperties.getProperty(temp[1])))
					.click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to find radio button " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + "- Not able to find radio button";

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " Selected the radiobutton sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " Selected the radiobutton successfully", "Screen shot: ",
					scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	// Verifies the selected button using the locator xpath locator
	public String verifyRadioSelected(String locator, String data, String scrpath) {
		logger.debug("Verifying Radio button selection");
		try {
			String temp[] = locator.split(Constants.DATA_SPLIT);
			String checked = driver
					.findElement(By.xpath(orProperties.getProperty(temp[0]) + data + orProperties.getProperty(temp[1])))
					.getAttribute("checked");
			if (checked == null) {
				extrep.log(LogStatus.FAIL, "Radiobutton not selected " + locator, "Screen shot: ", scrpath + ".jpg");
				return Constants.KEYWORD_FAIL + "- Radio not selected";
			}

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to find radio button " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + "- Not able to find radio button " + locator;

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Selected the radiobutton sucessfully");
		else
			extrep.log(LogStatus.PASS, "Selected the radiobutton successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	// Select the checkbox using the locator xpath locator
	public String checkCheckBox(String locator, String data, String scrpath) {
		logger.debug("Checking checkbox");
		try {
			// true or null
			String checked = driver.findElement(By.xpath(keyValues.xmlp(locator))).getAttribute("checked");
			if (checked == null) // checkbox is unchecked
				driver.findElement(By.xpath(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not find checkbox " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not find checkbox " + locator;
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Selected the checkbox sucessfully");
		else
			extrep.log(LogStatus.PASS, "Selected the checkbox successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	// Uncheck the checkbox using the locator xpath locator
	public String unCheckCheckBox(String locator, String data, String scrpath) {
		logger.debug("Unchecking checkBox");
		try {
			String checked = driver.findElement(By.xpath(keyValues.xmlp(locator))).getAttribute("checked");
			if (checked != null)
				driver.findElement(By.xpath(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not find checkbox " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not find checkbox " + locator;
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Unchecked the checkbox sucessfully");
		else
			extrep.log(LogStatus.PASS, "Unchecked the checkbox successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	// Verifies the selected checkbox using the locator xpath locator
	public String verifyCheckBoxSelected(String locator, String data, String scrpath) {
		logger.debug("Verifying checkbox selection");
		try {
			String checked = driver.findElement(By.xpath(keyValues.xmlp(locator))).getAttribute("checked");
			if (checked != null) {
				if (configProperties.getProperty("screenshot_everystep").equals("N"))
					extrep.log(LogStatus.PASS, "Checkbox selected sucessfully");
				else
					extrep.log(LogStatus.PASS, "Checked selected successfully " + locator, "Screen shot: ",
							scrpath + ".jpg");

				return Constants.KEYWORD_PASS;
			} else
				extrep.log(LogStatus.FAIL, " Checkbox Not selected " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + "Checkbox Not selected " + locator;

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not find checkbox " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " - Could not find checkbox";

		}

	}

	// Verifies the text of an element using its xpath locator
	public String verifyText(String locator, String data, String scrpath) {
		logger.debug("Verifying text " + "'" + data + "'");
		try {
			String actual = driver.findElement(By.xpath(keyValues.xmlp(locator))).getText();
			String expected = data;
			if (actual.equals(expected)) {
				if (configProperties.getProperty("screenshot_everystep").equals("N"))
					extrep.log(LogStatus.PASS, "Text verified sucessfully");
				else
					extrep.log(LogStatus.PASS, "Text verified successfully", "Screen shot: ", scrpath + ".jpg");
				return Constants.KEYWORD_PASS;
			} else
				extrep.log(LogStatus.FAIL,
						"Text does not match, Expected text - " + expected + "; Actual text - " + actual,
						"Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Text does not match, Expected text - " + expected + "; Actual text - "
					+ actual;
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not find locator " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " locator not found " + e.getMessage();
		}

	}

	// Output the text of an element using its xpath locator
	public String outputData(String locator, String data, String scrpath) {
		String returnValue = Constants.KEYWORD_PASS;
		logger.debug("Output the element data");
		try {
			String output = driver.findElement(By.xpath(keyValues.xmlp(locator))).getText();

			if (configProperties.getProperty("screenshot_everystep").equals("N")) {
				extrep.log(LogStatus.PASS, "Extracted the text sucessfully");
				returnValue = Constants.KEYWORD_PASS + output;

			} else {
				extrep.log(LogStatus.PASS, "Extracted the text successfully", "Screen shot: ", scrpath + ".jpg");
				returnValue = Constants.KEYWORD_PASS;
			}

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to extract the text of  " + locator, "Screen shot: ", scrpath + ".jpg");
			returnValue = Constants.KEYWORD_FAIL + " Unable to extract " + e.getMessage();

		}
		return returnValue;
	}

	// Writes data(provide through data) in to the field using its xpath locator
	public String writeInInput(String locator, String data, String scrpath) {
		logger.debug("Writing in text box");
		// int num = Integer.valueOf(data);
		try {
			driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(data);
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to write in  " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " Unable to write " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Text is written sucessfully");
		else
			extrep.log(LogStatus.PASS, "Text is written successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	public String writeInInputId(String locator, String data, String scrpath) {
		logger.debug("Writing in text box");
		// int num = Integer.valueOf(data);
		try {
			driver.findElement(By.id(keyValues.xmlp(locator))).sendKeys(data);
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to write in  " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " Unable to write " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Text is written sucessfully");
		else
			extrep.log(LogStatus.PASS, "Text is written successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	// Writes data(provide through data) in to the field using its Css locator
	public String writeInInputCss(String locator, String data, String scrpath) {
		logger.debug("Writing in text box");

		try {
			driver.findElement(By.cssSelector(keyValues.xmlp(locator))).sendKeys(data);
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to write in  " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " Unable to write " + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Text is written sucessfully");
		else
			extrep.log(LogStatus.PASS, "Text is written successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	// Writes data(provide through data) in to the field using its id locator
	public String writeInInput_JS(String locator, String data) {
		logger.debug("Writing in text box using JavaScript");
		try {
			((JavascriptExecutor) driver)
					.executeScript("document.getElementById('" + locator + "').value='" + data + "'");
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to write, Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	// Verifies written data(provide through data) in the field using its xpath
	// locator
	public String verifyTextinInput(String locator, String data, String scrpath) {
		logger.debug("Verifying the text in input box");
		try {
			String actual = driver.findElement(By.xpath(keyValues.xmlp(locator))).getAttribute("value");
			String expected = data;

			if (actual.equals(expected)) {
				if (configProperties.getProperty("screenshot_everystep").equals("N"))
					extrep.log(LogStatus.PASS, "Text verified sucessfully");
				else
					extrep.log(LogStatus.PASS, "Text verified successfully", "Screen shot: ", scrpath + ".jpg");
				return Constants.KEYWORD_PASS;
			} else {
				extrep.log(LogStatus.FAIL, "Text not matching with expected text " + expected, "Screen shot: ",
						scrpath + ".jpg");
				return Constants.KEYWORD_FAIL + " Not matching ";
			}

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to find input box ", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " Unable to find input box " + e.getMessage();

		}
	}

	// Clicks image using its xpath locator
	public String clickImage(String locator, String data, String scrpath) {
		logger.debug("Clicking the image");
		try {

			driver.findElement(By.xpath(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not click the image " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click on image " + e.getMessage();
		}

		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, "Clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Verifies page title with the expected title(provide through data)
	public String verifyTitle(String locator, String data, String scrpath) {
		logger.debug("Verifying title");
		try {
			String actualTitle = driver.getTitle();
			String expectedTitle = data;
			if (actualTitle.equals(expectedTitle)) {
				if (configProperties.getProperty("screenshot_everystep").equals("N"))
					extrep.log(LogStatus.PASS, "Title verified sucessfully");
				else
					extrep.log(LogStatus.PASS, "Title verified successfully", "Screen shot: ", scrpath + ".jpg");
				return Constants.KEYWORD_PASS;
			} else {
				extrep.log(LogStatus.FAIL, "Title not verified " + expectedTitle + " -- " + actualTitle);
				return Constants.KEYWORD_FAIL + " -- Title not verified " + expectedTitle + " -- " + actualTitle;
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Error in retrieving title");
			return Constants.KEYWORD_FAIL + " Error in retrieving title";
		}
	}

	// Verifies if locator exists or not using its xpath locator
	public String exist(String locator, String data, String scrpath) {
		logger.debug("Checking existance of element");
		try {
			driver.findElement(By.xpath(keyValues.xmlp(locator)));
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "; " + locator + "- locator doest not exist", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + "; " + locator + " - locator doest not exist";
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "; " + locator + " - locator exists");
		else
			extrep.log(LogStatus.PASS, "; " + locator + " - locator exists", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Clicks the locator using its xpath locator
	public String click(String locator, String data, String scrpath) {
		logger.debug("Clicking on any element");
		try {
			driver.findElement(By.xpath(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to click " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " Not able to click";
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Clicked successfully");
		else
			extrep.log(LogStatus.PASS, "Clicked successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Clicks the locator using its Css locator
	public String clickCss(String locator, String data, String scrpath) {
		logger.debug("Clicking on any element");
		try {
			driver.findElement(By.cssSelector(keyValues.xmlp(locator))).click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to click " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " Not able to click";
		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Clicked successfully");
		else
			extrep.log(LogStatus.PASS, "Clicked successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Clicks the locator using its id locator
	public String click_JS(String locator, String data, String scrpath) {
		logger.debug("Clicking on any element using JavaScript");
		try {
			((JavascriptExecutor) driver).executeScript("document.getElementById('" + locator + "').click()");
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to click, Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	// Waits until page loads completely
	public String synchronize(String locator, String data, String scrpath) {
		logger.debug("Waiting for page to load");
		((JavascriptExecutor) driver).executeScript("function pageloadingtime()" + "{"
				+ "return 'Page has completely loaded'" + "}" + "return (window.onload=pageloadingtime());");
		extrep.log(LogStatus.INFO, "Page loaded successfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Waits until element is visible
	public String waitForElementVisibility(String locator, String data, String scrpath) {
		logger.debug("Waiting for an element to be visible");
		int start = 1;
		int time = Integer.parseInt(data);
		try {
			while (time >= start) {
				if (driver.findElements(By.xpath(keyValues.xmlp(locator))).size() == 0) {
					Thread.sleep(1000L);
					start++;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to find element " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + "Unable to find element" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Element exists", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	// Closes the browser
	public String closeBrowser(String locator, String data, String scrpath) {
		logger.debug("Closing the browser");
		try {
			driver.close();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to close browser. Check if its open");
			return Constants.KEYWORD_FAIL + "Unable to close browser. Check if its open" + e.getMessage();
		}
		extrep.log(LogStatus.PASS, "Browser closed successfully");
		return Constants.KEYWORD_PASS;

	}

	// Deletes the cookoes from the machine
	public String deleteAllCookies(String locator, String data, String scrpath) {
		logger.debug("Deleting all the Browser cookies");
		try {
			driver.manage().deleteAllCookies();
			driver.navigate().refresh();
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable delete all the cookies from Browser");
			return Constants.KEYWORD_FAIL + "Unable delete all the cookies from Browser" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Deleted cookied successfully");
		return Constants.KEYWORD_PASS;

	}

	// Closes all the instances of browser
	public String quitBrowser(String locator, String data, String scrpath) {
		logger.debug("Closing the browser");
		try {
			driver.quit();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to close browser. Check if its open");
			return Constants.KEYWORD_FAIL + "Unable to close browser. Check if its open" + e.getMessage();
		}
		extrep.log(LogStatus.PASS, "All browser sessions has been closed successfuly");
		return Constants.KEYWORD_PASS;

	}

	// Waits for given number of seconds (provided through data)
	public String pause(String locator, String data, String scrpath)
			throws NumberFormatException, InterruptedException {
		int time = (int) Double.parseDouble(data);
		System.out.println("time is " + time);
		Thread.sleep(time * 1000L);
		extrep.log(LogStatus.INFO, "Paused");
		return Constants.KEYWORD_PASS;
	}

	// Clicks keyboard tab
	public String tab(String locator, String data, String scrpath) {
		logger.debug("Moving focus to next field");
		try {
			driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(Keys.TAB);
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to go move focus to another field");
			return Constants.KEYWORD_FAIL + "Unable to go move focus to another field" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Clicked tab successfuly");
		return Constants.KEYWORD_PASS;

	}

	// Clicks keyboard enter on the element using the locators Css locator
	public String enterCss(String locator, String data, String scrpath) {
		logger.debug("Going back one page");
		try {
			driver.findElement(By.cssSelector(keyValues.xmlp(locator))).sendKeys(Keys.ENTER);
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to click enter");
			return Constants.KEYWORD_FAIL + "- Unable to click enter -" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Clicked enter successfuly");
		return Constants.KEYWORD_PASS;

	}

	// Clicks keyboard enter on the element using the locators xpath locator
	public String enter(String locator, String data, String scrpath) {
		logger.debug("Going back one page");
		try {
			driver.findElement(By.xpath(keyValues.xmlp(locator))).sendKeys(Keys.ENTER);
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to click enter");
			return Constants.KEYWORD_FAIL + "Unable to click enter" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Clicked enter successfuly");
		return Constants.KEYWORD_PASS;

	}

	// Switch between windows
	public String windowHandler(String locator, String data, String scrpath) {
		logger.debug("Handling multiple windows");

		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String mainWindowHandle = driver.getWindowHandle();
			driver.findElement(By.xpath(keyValues.xmlp(locator))).click();
			Set<String> window = driver.getWindowHandles();
			for (String popupHandle : window) {
				if (!popupHandle.contains(mainWindowHandle)) {
					driver.switchTo().window(popupHandle);
				}
			}

			// Back to main window
			// driver.switchTo().window(mainWindowHandle);

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to handle windows, See log4j report for more info" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	// << Go back by one page
	public String goBack(String locator, String data, String scrpath) {
		logger.debug("Going back one page");
		try {
			driver.navigate().back();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to go back, Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	// >> Go forward by one page
	public String goForward(String locator, String data, String scrpath) {
		logger.debug("Going back one page");
		try {
			driver.navigate().forward();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to go back, Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;

	}

	// Clears the text in the field using the field's xpath locator
	public String clearInputText(String locator, String data, String scrpath) {
		logger.debug("Clearing input text box");

		try {
			driver.findElement(By.xpath(keyValues.xmlp(locator))).clear();
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to clear input text " + locator);
			return Constants.KEYWORD_FAIL + " Unable to clear input text " + e.getMessage();

		}
		extrep.log(LogStatus.INFO, "Input text cleared");
		return Constants.KEYWORD_PASS;

	}

	// Clears the text in the field using the field's Css locator
	public String clearInputText_css(String locator, String data, String scrpath) {
		logger.debug("Clearing input text box");

		try {
			driver.findElement(By.cssSelector(keyValues.xmlp(locator))).clear();
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to clear input text " + locator);
			return Constants.KEYWORD_FAIL + " Unable to clear input text " + e.getMessage();

		}
		extrep.log(LogStatus.INFO, "Input text cleared");
		return Constants.KEYWORD_PASS;

	}

	// Mousehover on the field using the field's xpath locator
	public String mouseHover(String locator, String data, String scrpath) {
		logger.debug("Mouse Hovering on the element");
		try {

			Thread.sleep(3000L);
			WebElement menu = driver.findElement(By.xpath(keyValues.xmlp(locator)));
			Actions builder = new Actions(driver);
			builder.moveToElement(menu).build().perform();

		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to mouse hover " + locator);
			return Constants.KEYWORD_FAIL + "Unable to mouse hover" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Mouse hovered sucessfully");
		return Constants.KEYWORD_PASS;

	}

	// Mousehover on the field using the field's Css locator
	public String mouseHoverCss(String locator, String data, String scrpath) {
		logger.debug("Mouse Hovering to the element");
		try {

			Thread.sleep(3000L);
			WebElement menu = driver.findElement(By.cssSelector(keyValues.xmlp(locator)));
			Actions builder = new Actions(driver);
			builder.moveToElement(menu).build().perform();

		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to mouse hover " + locator);
			return Constants.KEYWORD_FAIL + "Unable to mouse hover" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Mouse hovered sucessfully");
		return Constants.KEYWORD_PASS;

	}

	// Doubleclick on the field using the field's xpath locator
	public String doubleClick(String locator, String data, String scrpath) {
		logger.debug("Double clicking the element");
		try {

			Thread.sleep(3000L);
			WebElement menu = driver.findElement(By.xpath(keyValues.xmlp(locator)));
			Actions builder = new Actions(driver);
			builder.doubleClick(menu).build().perform();

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to double click " + locator);
			return Constants.KEYWORD_FAIL + "Unable to double click" + e.getMessage();
		}
		extrep.log(LogStatus.PASS, "Double clicked sucessfully");
		return Constants.KEYWORD_PASS;

	}

	// Switch between frames using xpath
	public String switchToFrameIndex(String locator, String data, String scrpath) {
		logger.debug("Switching to frame " + data);

		try {
			int ind = (int) Double.parseDouble(data);
			driver.switchTo().frame(ind);
			System.out.println(driver.getTitle());
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to frame", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to switch to frame" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String switchToFrameXpath(String locator, String data, String scrpath) {
		logger.debug("Switching to frame ");

		try {
			driver.switchTo().frame(driver.findElement(By.xpath(keyValues.xmlp(locator))));
			System.out.println(driver.getTitle());
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to frame", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to switch to frame" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String switchToFrameId(String locator, String data, String scrpath) {
		logger.debug("Switching to frame ");

		try {
			driver.switchTo().frame(driver.findElement(By.id(keyValues.xmlp(locator))));
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to frame", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to switch to frame" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String switchToMainWindow(String locator, String data, String scrpath) {
		logger.debug("Switching to frame ");

		try {
			driver.switchTo().defaultContent();
			System.out.println(driver.getTitle());
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to main window ", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to switch to main window" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " Switched to main window sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " Switched to main window sucessfully", "Screen shot: ",
					scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String switchToFrameCss(String locator, String data, String scrpath) {
		logger.debug("Switching to frame ");

		try {
			driver.switchTo().frame(driver.findElement(By.cssSelector(keyValues.xmlp(locator))));
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to frame", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to switch to frame" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " Switched to frame sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String maximizeWindow(String locator, String data, String scrpath) {
		logger.debug("Maximizig the window ");

		try {
			driver.manage().window().maximize();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to maximize window ", "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to maximize window" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " maximize window sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " maximize window sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String acceptAlert(String locator, String data, String scrpath) {
		logger.debug("accepting the alert");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			Alert alert = driver.switchTo().alert();
			wait.until(ExpectedConditions.alertIsPresent());
			alert.accept();
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to accept alert");
			return Constants.KEYWORD_FAIL + "Unable to accept alert" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Accepted alert successfuly");
		return Constants.KEYWORD_PASS;

	}

	public String dismissAlert(String locator, String data, String scrpath) {
		logger.debug("accepting the alert");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			Alert alert = driver.switchTo().alert();
			wait.until(ExpectedConditions.alertIsPresent());
			alert.dismiss();
		} catch (Exception e) {
			extrep.log(LogStatus.INFO, "Unable to accept alert");
			return Constants.KEYWORD_FAIL + "Unable to accept alert" + e.getMessage();
		}
		extrep.log(LogStatus.INFO, "Accepted alert successfuly");
		return Constants.KEYWORD_PASS;

	}

	public void captureScreenshot(String filename, String keyword_execution_result) throws IOException {
		// take screen shots

		if (configProperties.getProperty("screenshot_everystep").equals("Y")) {
			// capturescreen

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(filename + ".jpg"));
		} else if (keyword_execution_result.startsWith(Constants.KEYWORD_FAIL)
				&& configProperties.getProperty("screenshot_error").equals("Y")) {
			// capture screenshot
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(filename + ".jpg"));
		}
	}

	/************************
	 * Windows specific Keywords - AutoIt Methods
	 ********************************/

	public String autoItRun(String locator, String data, String scrpath) {

		logger.debug("Running " + "'" + data + "'");
		try {

			AutoIt.run(data);

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to run " + "'" + data + "'");
			return Constants.KEYWORD_FAIL + "Unable to run " + "'" + data + "'" + e.getMessage();
		}
		extrep.log(LogStatus.PASS, "'" + data + "'" + " Opened successfuly");
		return Constants.KEYWORD_PASS;
	}

	public String autoItClose(String locator, String data, String scrpath) {

		logger.debug("Closing " + "'" + data + "'");
		try {

			AutoIt.winClose(data);

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to close " + "'" + data + "'");
			return Constants.KEYWORD_FAIL + "Unable to close " + "'" + data + "'";
		}
		extrep.log(LogStatus.PASS, "'" + data + "'" + " closed successfuly");
		return Constants.KEYWORD_PASS;
	}

	public String autoItActivate(String locator, String data, String scrpath) {

		logger.debug("'" + data + "'" + " activated");
		try {

			AutoIt.winActivate(data);
			AutoIt.winWaitActive(data);

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to activate " + "'" + data + "'");
			return Constants.KEYWORD_FAIL + "Unable to activate " + "'" + data + "'";
		}
		extrep.log(LogStatus.PASS, "'" + data + "'" + " activated successfuly");
		return Constants.KEYWORD_PASS;
	}

	public String autoItCtrlSetText(String locator, String data, String scrpath) {

		logger.debug("Entering text " + "'" + data + "'");
		try {

			String object = keyValues.xmlp(locator);
			String title = object.split(Constants.DATA_SPLIT)[0];
			String text = object.split(Constants.DATA_SPLIT)[1];
			String control = object.split(Constants.DATA_SPLIT)[2];
			String string = data;
			AutoIt.winWait(title, text, 5);
			AutoIt.ControlSetText(title, text, control, string);

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to enter text " + "'" + data + "'");
			return Constants.KEYWORD_FAIL + "Unable to enter text " + "'" + data + "'";
		}
		extrep.log(LogStatus.PASS, "Text " + "'" + data + "'" + " entered successfuly");
		return Constants.KEYWORD_PASS;
	}

	public String autoItSend(String locator, String data, String scrpath) {

		logger.debug("Sending keyboard imput " + "'" + data + "'");
		try {

			AutoIt.send(data);

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Unable to send keyboard input " + "'" + data + "'");
			return Constants.KEYWORD_FAIL + "Unable to send keyboard input " + "'" + data + "'";
		}
		extrep.log(LogStatus.PASS, "Sent keyboard input " + "'" + data + "'" + " successfuly");
		return Constants.KEYWORD_PASS;
	}

	public String autoItControlClick(String locator, String data, String scrpath) {

		logger.debug("Clicking " + "'" + locator + "'");
		try {
			String object = keyValues.xmlp(locator);
			String title = object.split(Constants.DATA_SPLIT)[0];
			String text = object.split(Constants.DATA_SPLIT)[1];
			String id = object.split(Constants.DATA_SPLIT)[2];

			AutoIt.controlClick(title, text, id);

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, " Unable to click " + "'" + locator + "'");
			return Constants.KEYWORD_FAIL + " Unable to click " + "'" + locator + "'";
		}
		extrep.log(LogStatus.PASS, "Clicked " + locator + " successfuly");
		return Constants.KEYWORD_PASS;
	}

	/************************
	 * Windows specific Keywords - AutoIt Methods sdriver is a
	 * SikuliFirefoxDriver to drive interactions with a web page using both
	 * image recognition (Sikuli) and DOM (Selenium). screen is a
	 * 
	 * 
	 * 
	 ********************************/

	public String sikuScrnClick(String locator, String data, String scrpath) {
		logger.debug("Clicking on button ");

		try {
			screen.click(keyValues.xmlp(locator));

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to click on " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click button" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	/************************
	 * SoapUI Keywords
	 ********************************/

	public String createSoapTest(String locator, String data, String scrpath) {
		logger.debug("create a SOAPUI project using a wsdl ");

		try {

			String projectFile ="src/test/java/com/testsg/config/soapui-project1.xml";
	        WsdlProject project = new WsdlProject (projectFile);
	        //WsdlProject project = new WsdlProject();
	        WsdlInterface[] wsdls = WsdlImporter.importWsdl(project, "http://www.webservicex.com/globalweather.asmx?WSDL");
	        WsdlInterface wsdl = wsdls[0];
	        for (Operation operation : wsdl.getOperationList()){
	            WsdlOperation op = (WsdlOperation) operation;
	            System.out.println("OP:"+op.getName());System.out.println(op.createRequest(true));
	            System.out.println("Response:");System.out.println(op.createResponse(true));
	        }
	        } catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to create a soap test " + locator, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to create a soap test" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " soap project created sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " soap project created sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}
	
	public String sendSoapTest(String locator, String data, String scrpath) {
		logger.debug("create a SOAPUI project using a wsdl ");
		String result = "";
		try {

			//File projectFile = new File(data);
			SoapUI.setSoapUICore(new StandaloneSoapUICore(true));
			WsdlProject project = new WsdlProject();
			int c = project.getInterfaceCount();
			System.out.println("The interface count =" + c);

			for (int i = 0; i < c; i++)

			{

				WsdlInterface wsdl = (WsdlInterface) project.getInterfaceAt(i);
				String soapVersion = wsdl.getSoapVersion().toString();
				System.out.println("The SOAP version =" + soapVersion);
				System.out.println("The binding name = " + wsdl.getBindingName());
				int opc = wsdl.getOperationCount();
				System.out.println("Operation count =" + opc);
			//	String result = "";

				for (int j = 0; j < opc; j++) {
					WsdlOperation op = wsdl.getOperationAt(j);
					String opName = op.getName();
					System.out.println("OPERATION:" + opName);
					WsdlRequest req = op.getRequestByName("Req_" + soapVersion + "_" + opName);
					System.out.println("REQUEST :" + req.getName());
					System.out.println("The request content is =" + req.getRequestContent());
					System.out.println("The action is =" + req.getAction());
					req.setEndpoint("your_end_Point");

					// Assigning u/p to an operation: Generate
					if (opName.equals("Generate")) {
					//	req.getConfig().getCredentials().setAuthType("PREEMPTIVE"); // Setting the
													// Authorization type
						req.setUsername("u1");
						req.setPassword("u1");
					}

					WsdlSubmitContext wsdlSubmitContext = new WsdlSubmitContext(req);
					WsdlSubmit<?> submit = (WsdlSubmit<?>) req.submit(wsdlSubmitContext, false);
					Response response = submit.getResponse();
					result = response.getContentAsString();
				}
			}
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to create a soap test " + locator, "Screen shot: ", scrpath + ".jpg");
			return result + Constants.KEYWORD_FAIL + " -- Not able to create a soap test" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, locator + " soap project created sucessfully");
		else
			extrep.log(LogStatus.PASS, locator + " soap project created sucessfully", "Screen shot: ", scrpath + ".jpg");
		return result+Constants.KEYWORD_PASS;
		
	}

	/************************
	 * Application specific Keywords - NAC
	 ********************************/
	public String selectItem(String object, String data, String scrpath) {
		logger.debug("Inputing quantity for products");

		try {
			String str = data;
			String[] s = str.split(",");
			/*
			 * String str1=qty; String[] q1=str1.split("/");
			 */

			List<WebElement> items = driver.findElements(By.xpath(".//*[@id='productList']/tbody/tr/td[4]"));
			Iterator<WebElement> itr1 = items.iterator();
			String[] pageLink = new String[items.size()];
			int in = 0;
			while (itr1.hasNext()) {
				System.out.println("########################");
				pageLink[in] = itr1.next().getText().toString();
				in++;
			}
			int pointer = 6;

			String itemname = s[0];
			System.out.println("Page Links size= " + pageLink.length);
			System.out.println("***************pageLink[0]***********" + pageLink[0]);
			Thread.sleep(5000L);
			for (int j = 0; j < pageLink.length; j++) {
				if (pageLink[j].equalsIgnoreCase(itemname)) {
					System.out.println("************pageLink[j]***********" + pageLink[j]);
					driver.findElement(By.xpath("//table/tbody/tr/td[2]/table[1]/tbody/tr/td/table[2]/tbody/tr["
							+ pointer + "]/td[2]/input")).sendKeys(s[1]);
					Thread.sleep(1000L);
					driver.findElement(By.xpath("//table/tbody/tr/td[2]/table[1]/tbody/tr/td/table[2]/tbody/tr["
							+ (pointer + 1) + "]/td[2]/input")).click();
					Thread.sleep(2000L);
					Alert alert = driver.switchTo().alert();
					alert.accept();
					/*
					 * WebDriverWait wait = new WebDriverWait(driver, 2); Alert
					 * alert = driver.switchTo().alert();
					 * wait.until(ExpectedConditions.alertIsPresent());
					 * alert.accept();
					 */
				}
				pointer = pointer + 7;
			}
			pointer = 6;

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to select item" + object, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to select item" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, object + " Item selected sucessfully");
		else
			extrep.log(LogStatus.PASS, object + " Item selected sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	public String moveToElement(String object, String data, String scrpath) {
		logger.debug("Inputing quantity for products");

		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(By.xpath(".//*[@id='TimeTable']/tbody/tr[3]/td[2]/img"))).click()
					.perform();

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to " + object, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to switch to Element" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, object + " Switched to Element sucessfully");
		else
			extrep.log(LogStatus.PASS, object + " Switched to Element sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	public String selectfastEntryitem(String object, String data, String scrpath) {
		logger.debug("Inputing quantity for products");

		try {
			String str = data;
			String[] s = str.split(",");
			for (String s2 : s) {
				System.out.println(s2);
			}

			System.out.println("---------------data-------" + data);

			int i = 0;
			WebElement FastEntry = driver
					.findElement(By.xpath(".//*[@id='ctl00_MainContent_FELVew_ctrl" + i + "_vItemNo']"));
			WebElement FastEntryqty = driver
					.findElement(By.xpath(".//*[@id='ctl00_MainContent_FELVew_ctrl" + i + "_vItemQty']"));
			if ((s[i] == s[i])) {
				System.out.println("---------------data-------" + data);
				System.out.println(s.length);
				// System.out.println(s[i]);
				Thread.sleep(5000L);
				FastEntry.sendKeys(s[0]);

				FastEntryqty.sendKeys(s[1]);

			} else
				System.out.println("No Selection");

			WebElement cartFastEntry = driver.findElement(By.cssSelector("#MainContent_Img2"));
			cartFastEntry.click();

			WebDriverWait wait = new WebDriverWait(driver, 2);
			Alert alert = driver.switchTo().alert();
			wait.until(ExpectedConditions.alertIsPresent());
			alert.accept();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to select item " + object, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to select item" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, object + "Item selected sucessfully");
		else
			extrep.log(LogStatus.PASS, object + "Item selected sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	@SuppressWarnings("deprecation")
	public String selectlocation(String object, String data, String scrpath) {
		logger.debug("selecting locations");

		try {
			String str = data;
			String[] s = str.split("&");
			List<WebElement> loc = null;
			Thread.sleep(3000L);
			for (int i = 1; i < 17; i++) {

				loc = driver.findElements(By
						.xpath("//table/tbody/tr[2]/td/table/tbody/tr[7]/td[1]/table/tbody/tr[2]/td/div/div/div/div[1]/div/div["
								+ i + "]/span"));

				System.out.println("List Size = " + loc.size());
				System.out.println("***************loc********" + loc);
				int elementsSize = loc.size();
				for (int j = 0; j < s.length; j++) {
					for (WebElement loc1 : loc) {

						while (elementsSize != 0 && !loc1.isDisplayed()) {
							WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver,
									"//table/tbody/tr[2]/td/table/tbody/tr[7]/td[1]/table/tbody/tr[2]/td/div/div/div/div[1]/div/div["
											+ i + "]/span");
							selenium.doubleClickAt(
									"//table/tbody/tr[2]/td/table/tbody/tr[7]/td[1]/table/tbody/tr[2]/td/div/div/div/div[1]/div/div["
											+ i + "]/span",
									loc1.getText());

						}

						if (loc1.getText().equalsIgnoreCase(s[j])) {
							WebElement btnMoveRight = driver.findElement(By.xpath(".//*[@id='imgbtnMoveRight']"));
							System.out.println("Success");
							System.out.println("Element is displayed = " + loc1.isDisplayed());
							loc1.click();
							System.out.println(
									"%%%%%%%%%%%%%%%%%%%%%%%%%% ENTERED INTO IF CONDITION %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
							btnMoveRight.click();
							Thread.sleep(3000L);
							// System.out.println("------------address
							// selected----------"+loc);
						}
					}
				}
			}

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to " + object, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to select location" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, object + " selected location sucessfully");
		else
			extrep.log(LogStatus.PASS, object + "selected location sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String clickHiddenImage(String object, String data, String scrpath) {
		logger.debug("Clicking the image");
		try {

			WebElement worklogbutton = driver.findElement(By.xpath(".//*[@id='TimeTable']/tbody/tr[3]/td[2]"));
			WebElement worklogbuttonimage = driver.findElement(By.xpath(".//*[@id='TimeTable']/tbody/tr[3]/td[2]/img"));
			Actions builder = new Actions(driver);
			builder.moveToElement(worklogbutton).click().build().perform();

			worklogbuttonimage.click();
		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Could not click the hidden element " + object, "Screen shot: ",
					scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to click the hidden image " + e.getMessage();
		}

		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, "Clicked sucessfully");
		else
			extrep.log(LogStatus.PASS, "Clicked sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;
	}

	public String selectPromoItem(String object, String data, String scrpath) {
		logger.debug("Inputing quantity for products");

		try {
			String str = data;
			String[] s = str.split(",");
			/*
			 * String str1=qty; String[] q1=str1.split("/");
			 */
			int i = 2;
			List<WebElement> items = driver.findElements(By
					.xpath("//table/tbody/tr/td[2]/table[1]/tbody/tr/td/table[2]/tbody/tr[" + i + "]/td[2]/a/span[1]"));
			Iterator<WebElement> itr1 = items.iterator();
			String[] pageLink = new String[items.size()];
			int in = 0;
			while (itr1.hasNext()) {
				System.out.println("########################");
				pageLink[in] = itr1.next().getText().toString();
				in++;
			}

			String itemname = s[0];
			System.out.println("Page Links size= " + pageLink.length);
			System.out.println("***************pageLink[0]***********" + pageLink[0]);
			Thread.sleep(5000L);
			for (int j = 0; j < pageLink.length; j++) {
				if (pageLink[j].equalsIgnoreCase(itemname)) {
					System.out.println("************pageLink[j]***********" + pageLink[j]);

					Thread.sleep(1000L);
					driver.findElement(By.xpath(
							"//table/tbody/tr/td[2]/table[1]/tbody/tr/td/table[2]/tbody/tr[" + i + "]/td[2]/a/span[1]"))
							.click();
					Thread.sleep(2000L);
					// driver.switchTo().frame(1);
					Alert alert = driver.switchTo().alert();
					alert.accept();

				}
				i = i + 7;
			}

		} catch (Exception e) {
			extrep.log(LogStatus.FAIL, "Not able to switch to " + object, "Screen shot: ", scrpath + ".jpg");
			return Constants.KEYWORD_FAIL + " -- Not able to select promo item" + e.getMessage();

		}
		if (configProperties.getProperty("screenshot_everystep").equals("N"))
			extrep.log(LogStatus.PASS, object + " promo item selected sucessfully");
		else
			extrep.log(LogStatus.PASS, object + " promo item selected  sucessfully", "Screen shot: ", scrpath + ".jpg");
		return Constants.KEYWORD_PASS;

	}

	/*
	 * public String myAccountDropList(String locator, String data,String
	 * scrpath){ logger.debug("Selecting Random Color"); try { WebElement
	 * myAccountDropList = driver.findElement(By.cssSelector("#userAccount"));
	 * List<WebElement> list =
	 * myAccountDropList.findElements(By.cssSelector(".dropdown-menu>li>a"));
	 * list.get(1).click();
	 * 
	 * }catch(Exception e){ return Constants.KEYWORD_FAIL +
	 * " - Could not select from list. "+ e.getMessage();
	 * 
	 * }
	 * 
	 * return Constants.KEYWORD_PASS; }
	 * 
	 * 
	 * public String selectCreditCard(String locator, String data,String
	 * scrpath){ logger.debug("Selecting Credit Card");
	 * 
	 * int num = Integer.parseInt(data);
	 * 
	 * try { List<WebElement> selectSize =
	 * driver.findElements(By.xpath(x.xmlp(locator)));
	 * selectSize.get(num).click();
	 * 
	 * }catch(Exception e){ return Constants.KEYWORD_FAIL +
	 * " - Could not select from list. "+ e.getMessage();
	 * 
	 * }
	 * 
	 * return Constants.KEYWORD_PASS; }
	 * 
	 * 
	 * // Verify list of items after clicking on drop-down list like Newborn,
	 * Shoes etc.
	 * 
	 * public String verifyAllItems(String locator,String data,String scrpath){
	 * logger.debug("Verifying link Text"); try{ for (int i = 0; i <=150; i++) {
	 * List<WebElement> gridBoxes =
	 * driver.findElements(By.className("events-div")); System.out.println(
	 * "Number of boxes " + gridBoxes.size()); // Pick random link box Random
	 * gridnum = new Random(); int grids = gridnum.nextInt(gridBoxes.size());
	 * WebElement grid = gridBoxes.get(grids); //WebElement box =
	 * link_boxes.get(0); List<WebElement> ItemBoxes =
	 * grid.findElements(By.className("product-image")); System.out.println(
	 * "Total links are -- " + ItemBoxes.size()); // Pick random item Random
	 * itemnum = new Random(); int items = itemnum.nextInt(ItemBoxes.size());
	 * WebElement item = ItemBoxes.get(items); item.click();
	 * Thread.sleep(4000L); System.out.println(driver.getTitle());
	 * driver.navigate().back();
	 * 
	 * } }catch(Exception e){ return Constants.KEYWORD_FAIL+
	 * " -- Link text not verified"+e.getMessage();
	 * 
	 * } return Constants.KEYWORD_PASS; }
	 * 
	 * 
	 * 
	 * public String selectFrom(String locator, String data, String scrpath){
	 * logger.debug("Selecting from city"); try {
	 * driver.findElement(By.id(locator)).sendKeys(data);; new
	 * WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(
	 * By.xpath(".//*[@id='ui-id-1']//a[contains(.,'" + data + "')]")))
	 * .click(); } catch (Exception e) { extrep.log(LogStatus.FAIL,
	 * "Unable to select from city " + locator); return Constants.KEYWORD_FAIL +
	 * "Unable to select from city" + e.getMessage(); }
	 * extrep.log(LogStatus.PASS, "Selected sucessfully from city"); return
	 * Constants.KEYWORD_PASS; }
	 * 
	 * 
	 * 
	 * //Output the text of an element using its xpath locator public String
	 * outputVerifyPage(String locator, String data, String scrpath) { String
	 * returnValue = Constants.KEYWORD_PASS; logger.debug(
	 * "Output the element data"); try { String output =
	 * driver.findElement(By.xpath(keyValues.xmlp(locator))).getText();
	 * driver.navigate().to("https://www.google.com/flights"); String actual =
	 * driver.findElement(By.xpath(
	 * "//*[@id='root']/div[3]/table/tbody/tr[1]/td/div/div[1]/div[2]")).getText
	 * (); if (actual.equals(output)) { if
	 * (configProperties.getProperty("screenshot_everystep").equals("N")){
	 * extrep.log(LogStatus.PASS, "Extracted the text sucessfully"+ " Exp - "
	 * +output + " Act - " + actual); returnValue = Constants.KEYWORD_PASS +
	 * " Exp - "+output + " Act - " + actual;
	 * 
	 * } else{ extrep.log(LogStatus.PASS, "Extracted the text successfully"+
	 * " Exp - "+output + " Act - " + actual, "Screen shot: ", scrpath +
	 * ".jpg"); returnValue = Constants.KEYWORD_PASS + " Exp - "+output +
	 * " Act - " + actual; } } } catch (Exception e) {
	 * extrep.log(LogStatus.FAIL, "Unable to extract the text of  " + locator,
	 * "Screen shot: ", scrpath + ".jpg"); returnValue = Constants.KEYWORD_FAIL
	 * + " Unable to extract " + e.getMessage();
	 * 
	 * } return returnValue; }
	 * 
	 * 
	 */

}
