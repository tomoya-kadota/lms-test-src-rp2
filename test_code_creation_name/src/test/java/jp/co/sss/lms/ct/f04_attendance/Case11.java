package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms/");
		
		// ウィンドウサイズ最大化処理
		webDriver.manage().window().maximize();
		
		// トップページに遷移した際のエビデンス01を取得
		getEvidence(new Object() {}, "01");

		String currentUrl = webDriver.getCurrentUrl();
		assertEquals(currentUrl, "http://localhost:8080/lms/");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		final WebElement password = webDriver.findElement(By.name("password"));
		final WebElement loginBtn = webDriver.findElement(By.className("btn-primary"));

		loginId.clear();
		loginId.sendKeys("StudentAA01");
		password.clear();
		password.sendKeys("Student8080");

		// 入力値を入力した際のエビデンス01を取得
		getEvidence(new Object() {}, "01");

		loginBtn.click();

		// コース詳細画面へ画面遷移する際のエビデンス02を取得
		getEvidence(new Object() {}, "02");

		assertThat(webDriver.getTitle(), containsString("コース詳細"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		final WebElement attendance = webDriver.findElement(By.linkText("勤怠"));

		attendance.click();

		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();

		// 勤怠管理画面へ画面遷移し、JavaScriptのアラートを閉じた後、エビデンス01を取得
		getEvidence(new Object() {}, "01");

		assertThat(webDriver.getTitle(), containsString("勤怠情報変更"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		final WebElement attendanceUpdateLink = webDriver.findElement(By.linkText("勤怠情報を直接編集する"));
		
		attendanceUpdateLink.click();
		
		// 勤怠情報直接変更画面へ画面遷移した後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
				
		assertEquals(webDriver.getCurrentUrl(), "http://localhost:8080/lms/attendance/update");
		
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {
		WebElement attendanceTable = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div/form/table"));
		WebElement updateBtn = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div/form/div/input"));

		List<WebElement> regularHoursBtns = attendanceTable.findElements(By.cssSelector(".btn.btn-success.default-button"));

		for (WebElement regularHoursBtn : regularHoursBtns) {
			regularHoursBtn.click();
		}

		// 勤怠情報直接変更画面にて定時ボタンをすべてクリックした後、エビデンス01を取得
		getEvidence(new Object() {}, "01");

		updateBtn.click();

		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();

		// 勤怠情報直接変更画面にて更新ボタンをクリックし、JavaScriptのアラートを閉じた後、エビデンス02を取得
		getEvidence(new Object() {}, "02");

		assertEquals(webDriver.getCurrentUrl(), "http://localhost:8080/lms/attendance/update");

		// 出勤情報が記録されているHTML要素をリスト型で取得
		List<WebElement> attendanceRecordList = webDriver.findElements(By.cssSelector("table#main tbody tr"));
		
		List<WebElement> startTimeAndEndTimeList = new ArrayList<WebElement>();
		
		// 出勤情報リストから出勤＆退勤のみcss記述（td.w80）から抜き出し、出退勤管理リストに詰める
		for (WebElement attendanceRecord : attendanceRecordList) {
			startTimeAndEndTimeList = attendanceRecord.findElements(By.cssSelector("td.w80"));
			
			// 要素の0番目（出勤時間）を取得
			String startTime = startTimeAndEndTimeList.get(0).getText();
			// 要素の1番目（退勤時間）を取得
			String endTime = startTimeAndEndTimeList.get(1).getText();

			assertEquals("09:00", startTime);
			assertEquals("18:00", endTime);
		}	
	}
}
