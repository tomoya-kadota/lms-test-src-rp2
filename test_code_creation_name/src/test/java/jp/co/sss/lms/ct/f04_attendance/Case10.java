package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {
		final WebElement punchIn = webDriver.findElement(By.name("punchIn"));
		
		punchIn.click();
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 完了メッセージの取得
		visibilityTimeout(By.className("alert-info"), 10);
		// 完了メッセージの出力が確認できた後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		final WebElement message = webDriver.findElement(By.className("alert-info"));
		
		assertThat(message.getText(), is(containsString("勤怠情報の登録が完了しました。")));
		
		// 完了メッセージ出力時点での日本時間を取得し、24時間形式で文字列型に整形
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		ZonedDateTime jstNowZdt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        String jstNowTime = jstNowZdt.format(formatter);
        
        // 出勤情報が記録されているHTML要素をリスト型で取得
		List<WebElement> attendanceRecordList = webDriver.findElements(By.cssSelector("table#main tbody tr"));
		
		List<WebElement> startTimeAndEndTimeList = new ArrayList<WebElement>();
		
		// 出勤情報リストから出勤＆退勤のみcss記述（td.w80）から抜き出し、出退勤管理リストに詰める
		for (WebElement attendanceRecord : attendanceRecordList) {
			startTimeAndEndTimeList = attendanceRecord.findElements(By.cssSelector("td.w80"));
			
			// 要素の0番目（出勤時間）を取得
			String startTime = startTimeAndEndTimeList.get(0).getText(); 
			assertEquals(jstNowTime, startTime);
		}
		
		
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {
		final WebElement punchOut = webDriver.findElement(By.name("punchOut"));
		
		punchOut.click();
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 完了メッセージの取得
		visibilityTimeout(By.className("alert-info"), 10);
		
		// 完了メッセージの出力が確認できた後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		final WebElement message = webDriver.findElement(By.className("alert-info"));
		
		assertThat(message.getText(), is(containsString("勤怠情報の登録が完了しました。")));
		
		// 完了メッセージ出力時点での日本時間を取得し、24時間形式で文字列型に整形
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		ZonedDateTime jstNowZdt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        String jstNowTime = jstNowZdt.format(formatter);
        
        // 出勤情報が記録されているHTML要素をリスト型で取得
		List<WebElement> attendanceRecordList = webDriver.findElements(By.cssSelector("table#main tbody tr"));
		
		List<WebElement> startTimeAndEndTimeList = new ArrayList<WebElement>();
		
		// 出勤情報リストから出勤＆退勤のみcss記述（td.w80）から抜き出し、出退勤管理リストに詰める
		for (WebElement attendanceRecord : attendanceRecordList) {
			startTimeAndEndTimeList = attendanceRecord.findElements(By.cssSelector("td.w80"));
			
			// 要素の1番目（退勤時間）を取得
			String endTime = startTimeAndEndTimeList.get(1).getText(); 
			assertEquals(jstNowTime, endTime);
		}
		
		
	}

}
