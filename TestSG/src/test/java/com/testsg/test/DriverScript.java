package com.testsg.test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.jacob.com.LibraryLoader;
import com.relevantcodes.extentreports.ExtentReports;
import com.testsg.xls.read.Xls_Reader;

public class DriverScript {

	static ExtentReports extrep = ExtentReports.get(DriverScript.class);
	public final Logger logger = Logger.getLogger(DriverScript.class);
	public Xls_Reader suiteXLS;
	public int currentSuiteID;
	public String currentTestSuite;
	public static Xls_Reader currentTestSuiteXLS;
	public static int currentTestCaseID;
	public static String currentTestCaseName;
	public static int currentTestStepID;
	public static String currentKeyword;
	public static String proceedOnFail;
	public static int currentTestDataSetID = 2;
	public static Method methods[];
	public static Method capturescreenShot_method;
	public static Keywords keywords;
	public static String keyword_execution_result;
	public static ArrayList<String> resultSet;
	public static String data;
	public static String object;
	// properties
	public static Properties configProperties;
	public static Properties orProperties;
	public static String result_FolderName = null;
	public static String scrsht_FolderName = null;
	public String resultsfolderpath;
	public String path;
	

	public DriverScript() throws NoSuchMethodException, SecurityException {
		keywords = new Keywords();
		methods = keywords.getClass().getMethods();
		capturescreenShot_method = keywords.getClass().getMethod("captureScreenshot", String.class, String.class);
	}

	public static void main(String[] args) throws Exception {
		String jacobDllVersionToUse;
		if (System.getProperty("sun.arch.data.model").contains("32")){
		jacobDllVersionToUse = "jacob-1.18-x86.dll";
		}
		else {
		jacobDllVersionToUse = "jacob-1.18-x64.dll";
		}
		File file = new File("lib", jacobDllVersionToUse);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		//System.out.println(args[0]);
		//System.out.println(System.getProperty("config"));
		//System.out.println(System.getProperty("or"));
		DriverScript driverScript = new DriverScript();
		String resultsfolderpath = driverScript.createhtmlPath();
		String path = resultsfolderpath + "\\AutomationReport.html";
		extrep.init(path, false);
		extrep.config().documentTitle(Constants.DOCTITLE).reportTitle(Constants.REPORTNAME)
				.reportHeadline(Constants.REPORTHEADLINE);
		extrep.config().useExtentFooter(false);

		PropertyConfigurator.configure("log.properties");

		FileInputStream fs = new FileInputStream("src/test/java/com/testsg/config/config.xml");

		configProperties = new Properties();
		configProperties.loadFromXML(fs);
		fs.close();
		fs = new FileInputStream("src/test/java/com/testsg/config/or.xml");
		orProperties = new Properties();
		orProperties.loadFromXML(fs);
		fs.close();
		driverScript.start(resultsfolderpath);

	}

	public void start(String resultsfolderpath) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// initialize the app logs
		
		logger.debug("Properties loaded. Starting testing");
		// 1) check the runmode of test Suite
		// 2) Runmode of the test case in test suite
		// 3) Execute keywords of the test case serially
		// 4) Execute Keywords as many times as
		// number of data sets - set to Y
		logger.debug("Intialize Suite xlsx");
		suiteXLS = new Xls_Reader("src/test/java/com/testsg/xls/Suite.xlsx");

		for (currentSuiteID = 2; currentSuiteID <= suiteXLS.getRowCount(Constants.TEST_SUITE_SHEET); currentSuiteID++) {

			logger.debug(suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.TEST_CASE_ID, currentSuiteID)
					+ " -- " + suiteXLS.getCellData("Test Suite", "Runmode", currentSuiteID));
			// test suite name = test suite xls file having tes cases
			currentTestSuite = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.TEST_CASE_ID,
					currentSuiteID);
			if (suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID)
					.equals(Constants.RUNMODE_YES)) {
				// execute the test cases in the suite
				logger.debug("******Executing the Suite******"
						+ suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.TEST_CASE_ID, currentSuiteID));

				currentTestSuiteXLS = new Xls_Reader("src/test/java/com/testsg/xls/" + currentTestSuite + ".xlsx");
				// iterate through all the test cases in the suite
				for (currentTestCaseID = 2; currentTestCaseID <= currentTestSuiteXLS
						.getRowCount("Test Cases"); currentTestCaseID++) {

					logger.debug(currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID,
							currentTestCaseID) + " -- "
							+ currentTestSuiteXLS.getCellData("Test Cases", "Runmode", currentTestCaseID));
					currentTestCaseName = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID,
							currentTestCaseID);
					extrep.startTest(currentTestCaseName);
					// ExtentTest parent =
					// extrep.startTest(currentTestCaseName);
					if (currentTestSuiteXLS
							.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestCaseID)
							.equals(Constants.RUNMODE_YES)) {
						logger.debug("Executing the test case -> " + currentTestCaseName);
						if (currentTestSuiteXLS.isSheetExist(currentTestCaseName)) {
							// RUN as many times as number of test data sets
							// with runmode Y
							for (currentTestDataSetID = 2; currentTestDataSetID <= currentTestSuiteXLS
									.getRowCount(currentTestCaseName); currentTestDataSetID++) {
								resultSet = new ArrayList<String>();
								
								// checking the runmode for the current data set
								if (currentTestSuiteXLS
										.getCellData(currentTestCaseName, Constants.RUNMODE, currentTestDataSetID)
										.equals(Constants.RUNMODE_YES)) {

									// iterating through all keywords
									logger.debug("Iteration number " + (currentTestDataSetID - 1));
									executeKeywords(resultsfolderpath); // multiple sets of data
								}
								else
									logger.debug("Iteration number " + (currentTestDataSetID - 1)+" skipped");
								createXLSReport();
							}
						} else {
							// iterating through all keywords
							resultSet = new ArrayList<String>();
							executeKeywords(resultsfolderpath);// no data
							createXLSReport();
						}
					}
					extrep.endTest();
				}
			}
		}
	}

	public void executeKeywords(String resultsfolderpath) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// iterating through all keywords

		boolean flagToBreak = false;

		for (currentTestStepID = 2; currentTestStepID <= currentTestSuiteXLS
				.getRowCount(Constants.TEST_STEPS_SHEET); currentTestStepID++) {
			if (flagToBreak) {
				keywords.closeBrowser(null, null, null);
				break;
			}

			// checking TCID
			if (currentTestCaseName.equals(
					currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, currentTestStepID))) {

				data = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID);
				if (data.startsWith(Constants.DATA_START_COL)) {
					// read actual data value from the corresponding column
					data = currentTestSuiteXLS.getCellData(currentTestCaseName, data.split(Constants.DATA_SPLIT)[1],
							currentTestDataSetID);
					} else if (data.startsWith(Constants.CONFIG)) {
					// read actual data value from config.properties
					data = configProperties.getProperty(data.split(Constants.DATA_SPLIT)[1]);
					} else {
					// by default read actual data value from or.properties
					//data = OR.getProperty(data);
					data=currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID);
				}
				object = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT,
						currentTestStepID);
				currentKeyword = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD,
						currentTestStepID);
				logger.debug(currentKeyword);
				proceedOnFail = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.PROCEED_ON_FAIL,
						currentTestStepID);
				
				

				// code to execute the keywords
				
				for (int index = 0; index < methods.length; index++) {

					if (methods[index].getName().equals(currentKeyword)) {
						String scrpath = resultsfolderpath + "\\screenshots\\" + currentTestSuite + "_"
								+ currentTestCaseName + "_TS" + currentTestStepID + "_" + (currentTestDataSetID - 1);

						keyword_execution_result = (String) methods[index].invoke(keywords, object, data, scrpath);
						logger.debug(keyword_execution_result);
						resultSet.add(keyword_execution_result);

						// capture screenshot
						capturescreenShot_method.invoke(keywords, scrpath, keyword_execution_result);
						
						// Terminate if test step is failed and 'proceed_on_fail' flag is 'N'
						/*if (keyword_execution_result.startsWith("PASS") && currentKeyword.equals("outputData")) 
						currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID+1, resultSet.get(index).substring(4));
						*/
						//	Terminate if test step is failed and 'proceed_on_fail' flag is 'N'
						if (keyword_execution_result.startsWith("FAIL") && proceedOnFail.equals("N")) {
							flagToBreak = true;
							break;
						
						} 

					}
				}
			}
		}

	}

	public String createhtmlPath() {
		Date d = new Date();
		String date = d.toString().replaceAll(" ", "_");
		date = date.replaceAll(":", "_");
		date = date.replaceAll("\\+", "_");
		// System.out.println(date);
		String reportsDirPath = System.getProperty("user.dir") + File.separator + "Reports" + File.separator;

		result_FolderName = reportsDirPath + "Reports" + "_" + date;
		new File(result_FolderName).mkdirs();
		scrsht_FolderName = result_FolderName + File.separator  + "screenshots";
		new File(scrsht_FolderName).mkdirs();
		// System.out.println(scrsht_FolderName);
		return result_FolderName;

	}

	public void createXLSReport() {

		String colName = Constants.RESULT + (currentTestDataSetID - 1);

		boolean isColExist = false;

		for (int columnIndex = 0; columnIndex < currentTestSuiteXLS.getColumnCount(Constants.TEST_STEPS_SHEET); columnIndex++) {

			if (currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, columnIndex, 1).equals(colName)) {
				isColExist = true;
				break;
			}
		}

		if (!isColExist)
			currentTestSuiteXLS.addColumn(Constants.TEST_STEPS_SHEET, colName);
		int index = 0;
		for (int rowIndex = 2; rowIndex <= currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET); rowIndex++) {
			if (currentTestCaseName
					.equals(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, rowIndex))) {
				if (resultSet.size() == 0)
					currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, rowIndex, Constants.KEYWORD_SKIP);
				else if (index < resultSet.size())
				{
					currentTestSuiteXLS.setCellData(Constants.TEST_STEPS_SHEET, colName, rowIndex, resultSet.get(index));
				
				}
				else {
					break;
				}
				index++;
			}
		}

		if (resultSet.size() == 0) {
			// skip
			currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID,
					Constants.KEYWORD_SKIP);
			return;
		} else {
			for (int i = 0; i < resultSet.size(); i++) {
				if (!resultSet.get(i).equals(Constants.KEYWORD_PASS)) {
					currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID,
							resultSet.get(i));
					return;
				}
			}
		}
		currentTestSuiteXLS.setCellData(currentTestCaseName, Constants.RESULT, currentTestDataSetID,
				Constants.KEYWORD_PASS);
		
	}
}
