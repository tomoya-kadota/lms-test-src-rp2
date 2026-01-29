package jp.co.sss.lms.ct.f03_report;

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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		final List<WebElement> sectionDetailBtnList = webDriver.findElements(By.cssSelector("input[type='submit'][value='詳細']"));
		sectionDetailBtnList.get(0).click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
		
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		assertThat(webDriver.getTitle(), containsString("セクション詳細"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		final WebElement reportRegistBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='日報【デモ】を提出する']"));
		reportRegistBtn.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
				
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
				
		assertThat(webDriver.getTitle(), containsString("レポート登録"));
		
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		final WebElement dailyReport = webDriver.findElement(By.id("content_0"));
		final WebElement dailyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
		
		dailyReport.clear();
		dailyReport.sendKeys("今日はやりました。");
		
		// 日報内容を記入した際にエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		dailyReportRegistBtn.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
						
		// 画面遷移が確認できたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
						
		assertThat(webDriver.getTitle(), containsString("セクション詳細"));
		
		final WebElement reportRegisteredBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='提出済み日報【デモ】を確認する']"));
		assertTrue(reportRegisteredBtn.isDisplayed());
		
	}

}
