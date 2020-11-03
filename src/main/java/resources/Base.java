package resources;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Base {

	public static Properties po;
	public static WebDriver driver;
	public static String InputFilepath;
	public String browser;

	public static String getprop(String prop) throws IOException {
		po = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\resources\\config.properties");
		po.load(fis);
		String propvalue = po.getProperty(prop);
		return propvalue;

	}

	public static ArrayList<String> GetFiledetails(String filetype) throws IOException {
		InputFilepath = getprop(filetype);
		System.out.println("Parameter sent is " + filetype);
		System.out.println("Parameter found is " + InputFilepath);
		Path file = Paths.get(InputFilepath);
		ArrayList<String> ExpectedArray = new ArrayList<String>();
		if (filetype == "input") {

			try (InputStream in = Files.newInputStream(file);
					BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				while ((line = reader.readLine()) != null) {

					String text = line.replaceAll("\\s+", "");
					Pattern pattern = Pattern.compile("[A-Z]{2}[0-9]{2}[A-Z]{3}");
					Matcher matcher = pattern.matcher(text);
					while (matcher.find()) {
						ExpectedArray.add(matcher.group());

					}
				}
			}
		} else if (filetype == "output") {

			try (InputStream in = Files.newInputStream(file);
					BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					String[] ExpectedValues = line.split(",");
					for (int j = 0; j < ExpectedValues.length; j++) {
						ExpectedArray.add(ExpectedValues[j]);
					}
				}
			} catch (IOException x) {
				System.err.println(x);
			}

		}
		return ExpectedArray;

	}

	public static WebDriver Getdriver() throws IOException {
		String browser = getprop("browser");
		if (browser.contains("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"C:\\Anu\\Croydon\\RegistrationDetails\\src\\main\\java\\resources\\chromedriver.exe");
			driver = new ChromeDriver();
		}
		return driver;
	}

	public String gettesturl() throws IOException {
		browser = getprop("url");
		return browser;
	}

}
