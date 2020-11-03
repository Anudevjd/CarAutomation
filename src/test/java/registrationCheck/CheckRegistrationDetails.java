package registrationCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import resources.Base;

public class CheckRegistrationDetails extends Base {
	public ArrayList<String> GetCarNumbers;
	public ArrayList<String> GetExpectedResults;
	public ArrayList<String> ActualValue = new ArrayList<String>();
	public String url;

	@BeforeTest
	public void getdata() throws IOException {
		GetCarNumbers = GetFiledetails("input");
		System.out.println("The Car registration numbers provided in input file :" + GetCarNumbers);
		GetExpectedResults = GetFiledetails("output");
		driver = Getdriver();

		url = gettesturl();
		driver.get(url);
	}

	@Test
	public void CheckRegDetails() throws InterruptedException {
		String[][] Expected = new String[5][5];
		int k = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Expected[i][j] = GetExpectedResults.get(k);
				k++;
			}
		}

		for (int i = 0; i < GetCarNumbers.size(); i++) {
			String numberplate = GetCarNumbers.get(i);

			if (GetExpectedResults.contains(numberplate)) {
				System.out.println("Output needs to be verified");

				for (int a = 0; a < 5; a++) {
					if (Expected[a][0].equalsIgnoreCase(numberplate)) {
						ActualValue.add(Expected[a][0]);
						ActualValue.add(Expected[a][1]);
						ActualValue.add(Expected[a][2]);
						ActualValue.add(Expected[a][3]);
						ActualValue.add(Expected[a][4]);
						System.out.println("Values for " + numberplate + " in the Expected file is" + ActualValue);
					}
				}
			} else {
				System.out.println("****************************************************");
				System.out.println("The Number " + numberplate + " is not expected in output file");
				System.out.println("*****************************************************");
				continue;
			}
			// Finding the actual Values in the Application
			driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//*[@id='vrm-input']")).clear();
			driver.findElement(By.xpath("//*[@id='vrm-input']")).sendKeys(numberplate);
			driver.findElement(By.xpath("//button[contains(text(),'Free Car Check')]")).click();

			String field1 = ActualValue.get(i);
			// Getting corresponding Make,Model,colour and year for specific registration
			// number
			ArrayList<String> Output = new ArrayList<String>();
			Thread.sleep(1000);
			String reg = driver.findElement(By.xpath("//*[@id='m']/div[2]/div[4]/div[1]/div/span/div[2]/dl[1]/dd"))
					.getText();
			Output.add(reg);
			reg = driver.findElement(By.xpath("//*[@id='m']/div[2]/div[4]/div[1]/div/span/div[2]/dl[2]/dd")).getText();
			Output.add(reg);
			reg = driver.findElement(By.xpath("//*[@id='m']/div[2]/div[4]/div[1]/div/span/div[2]/dl[3]/dd")).getText();
			Output.add(reg);
			reg = driver.findElement(By.xpath("//*[@id='m']/div[2]/div[4]/div[1]/div/span/div[2]/dl[4]/dd")).getText();
			Output.add(reg);
			reg = driver.findElement(By.xpath("//*[@id='m']/div[2]/div[4]/div[1]/div/span/div[2]/dl[5]/dd")).getText();
			Output.add(reg);
			driver.navigate().back();
			System.out.println("Values from the application for " + numberplate + " is " + Output);
			Assert.assertEquals(ActualValue, Output, "The output doesn't seem to match with Expected Value");
			ActualValue.clear();

		}

	}

	@AfterTest
	public void teardown() {
		 driver.quit();
	}

}
