package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
		
		// ウィンドウサイズ最大化 
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
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {
		// 出勤簿の一番先頭の日付でテスト実施
		Select startTimeHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startTimeMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endTimeHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endTimeMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		
		WebElement updateBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		// 入力値の初期化
		startTimeHour.selectByIndex(0);
		startTimeMinute.selectByIndex(0);
		endTimeHour.selectByIndex(0);
		endTimeMinute.selectByIndex(0);
		
		// 【入力値①】出勤（時）：（空白）
		startTimeHour.selectByVisibleText("");
		// 【入力値②】出勤（分）：00
		startTimeMinute.selectByVisibleText("00");
		// 【入力値③】退勤（時）：18	
		endTimeHour.selectByVisibleText("18");
		// 【入力値④】退勤（分）：（空白）
		endTimeMinute.selectByVisibleText("");
		
		// 入力値の入力後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		updateBtn.click();
		
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 更新ボタンを押下し、JavaScriptのアラートを閉じた後、エビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		String currentUrl = webDriver.getCurrentUrl();
		assertEquals(currentUrl, "http://localhost:8080/lms/attendance/update");
		
		List<WebElement> errorMessageList = webDriver.findElements(By.className("error"));
		
		assertThat(errorMessageList.get(0).getText(), is(containsString("出勤時間が正しく入力されていません。")));
		assertThat(errorMessageList.get(1).getText(), is(containsString("退勤時間が正しく入力されていません。")));
		
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		assertTrue(errorInputList.get(0).isDisplayed());
		assertTrue(errorInputList.get(1).isDisplayed());
		
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		// 出勤簿の一番先頭の日付でテスト実施
		Select startTimeHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startTimeMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endTimeHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endTimeMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		
		WebElement updateBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		// 入力値の初期化
		startTimeHour.selectByIndex(0);
		startTimeMinute.selectByIndex(0);
		endTimeHour.selectByIndex(0);
		endTimeMinute.selectByIndex(0);
		
		// 【入力値①】出勤（時）：（空白）
		startTimeHour.selectByVisibleText("");
		// 【入力値②】出勤（分）：（空白）
		startTimeMinute.selectByVisibleText("");
		// 【入力値③】退勤（時）：18	
		endTimeHour.selectByVisibleText("18");
		// 【入力値④】退勤（分）：00
		endTimeMinute.selectByVisibleText("00");
		
		// 入力値の入力後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		updateBtn.click();
		
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 更新ボタンを押下し、JavaScriptのアラートを閉じた後、エビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		String currentUrl = webDriver.getCurrentUrl();
		assertEquals(currentUrl, "http://localhost:8080/lms/attendance/update");
		
		List<WebElement> errorMessageList = webDriver.findElements(By.className("error"));
		
		assertThat(errorMessageList.get(0).getText(), is(containsString("出勤情報がないため退勤情報を入力出来ません。")));
		
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		assertTrue(errorInputList.get(0).isDisplayed());
		assertTrue(errorInputList.get(1).isDisplayed());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		// 出勤簿の一番先頭の日付でテスト実施
		Select startTimeHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startTimeMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endTimeHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endTimeMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		
		WebElement updateBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		// 入力値の初期化
		startTimeHour.selectByIndex(0);
		startTimeMinute.selectByIndex(0);
		endTimeHour.selectByIndex(0);
		endTimeMinute.selectByIndex(0);
		
		// 【入力値①】出勤（時）：09
		startTimeHour.selectByVisibleText("09");
		// 【入力値②】出勤（分）：00
		startTimeMinute.selectByVisibleText("00");
		// 【入力値③】退勤（時）：08	
		endTimeHour.selectByVisibleText("08");
		// 【入力値④】退勤（分）：00
		endTimeMinute.selectByVisibleText("00");
		
		// 入力値の入力後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		updateBtn.click();
		
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 更新ボタンを押下し、JavaScriptのアラートを閉じた後、エビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		String currentUrl = webDriver.getCurrentUrl();
		assertEquals(currentUrl, "http://localhost:8080/lms/attendance/update");
		
		List<WebElement> errorMessageList = webDriver.findElements(By.className("error"));
		
		assertThat(errorMessageList.get(0).getText(), is(containsString("より後でなければいけません。")));
		
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		assertTrue(errorInputList.get(0).isDisplayed());
		assertTrue(errorInputList.get(1).isDisplayed());	
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		// 出勤簿の一番先頭の日付でテスト実施
		Select startTimeHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startTimeMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endTimeHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endTimeMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		Select blankTime = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		
		WebElement updateBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		// 入力値の初期化
		startTimeHour.selectByIndex(0);
		startTimeMinute.selectByIndex(0);
		endTimeHour.selectByIndex(0);
		endTimeMinute.selectByIndex(0);
		
		// 【入力値①】出勤（時）：09
		startTimeHour.selectByVisibleText("09");
		// 【入力値②】出勤（分）：00
		startTimeMinute.selectByVisibleText("00");
		// 【入力値③】退勤（時）：10	
		endTimeHour.selectByVisibleText("10");
		// 【入力値④】退勤（分）：00
		endTimeMinute.selectByVisibleText("00");
		// 【入力値⑤】中抜け時間：1時15分
		blankTime.selectByVisibleText("1時15分");
		
		// 入力値の入力後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		updateBtn.click();
		
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 更新ボタンを押下し、JavaScriptのアラートを閉じた後、エビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		String currentUrl = webDriver.getCurrentUrl();
		assertEquals(currentUrl, "http://localhost:8080/lms/attendance/update");
		
		List<WebElement> errorMessageList = webDriver.findElements(By.className("error"));
		
		assertThat(errorMessageList.get(0).getText(), is(containsString("中抜け時間が勤務時間を超えています。")));
		
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		assertTrue(errorInputList.get(0).isDisplayed());
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		// 出勤簿の一番先頭の日付でテスト実施
		Select startTimeHour = new Select(webDriver.findElement(By.id("startHour0")));
		Select startTimeMinute = new Select(webDriver.findElement(By.id("startMinute0")));
		Select endTimeHour = new Select(webDriver.findElement(By.id("endHour0")));
		Select endTimeMinute = new Select(webDriver.findElement(By.id("endMinute0")));
		Select blankTime = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		WebElement note = webDriver.findElement(By.name("attendanceList[0].note"));
		
		WebElement updateBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']"));
		
		// 入力値の初期化
		startTimeHour.selectByIndex(0);
		startTimeMinute.selectByIndex(0);
		endTimeHour.selectByIndex(0);
		endTimeMinute.selectByIndex(0);
		blankTime.selectByIndex(0);
		
		// 【入力値①】出勤（時）：09
		startTimeHour.selectByVisibleText("09");
		// 【入力値②】出勤（分）：00
		startTimeMinute.selectByVisibleText("00");
		// 【入力値③】退勤（時）：18	
		endTimeHour.selectByVisibleText("18");
		// 【入力値④】退勤（分）：00
		endTimeMinute.selectByVisibleText("00");
		// 【入力値⑤】備考　　　：「あ」を101文字文
		note.sendKeys("あああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		
		// 入力値の入力後、エビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		updateBtn.click();
		
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 更新ボタンを押下し、JavaScriptのアラートを閉じた後、エビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		String currentUrl = webDriver.getCurrentUrl();
		assertEquals(currentUrl, "http://localhost:8080/lms/attendance/update");
		
		List<WebElement> errorMessageList = webDriver.findElements(By.className("error"));
		
		assertThat(errorMessageList.get(0).getText(), is(containsString("備考の長さが最大値(100)を超えています。")));
		
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		assertTrue(errorInputList.get(0).isDisplayed());
		
	}

}
