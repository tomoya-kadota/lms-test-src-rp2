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
import org.openqa.selenium.support.ui.Select;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		final List<WebElement> sectionDetailBtnList = webDriver.findElements(By.cssSelector("input[type='submit'][value='詳細']"));
		
		// 週報が編集できる＆すでに登録済のコース詳細をクリックする
		sectionDetailBtnList.get(1).click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
		
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		assertThat(webDriver.getTitle(), containsString("セクション詳細"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		final WebElement weeklyReportCheckBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='提出済み週報【デモ】を確認する']"));
		weeklyReportCheckBtn.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
				
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
				
		assertThat(webDriver.getTitle(), containsString("レポート登録"));
		
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		final WebElement sectionName = webDriver.findElement(By.id("intFieldName_0"));
		final Select comprehensionLevel = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		final WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		final WebElement impressions = webDriver.findElement(By.id("content_1"));
		final WebElement review = webDriver.findElement(By.id("content_2"));
		final WebElement weeklyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
		
		sectionName.clear();
		sectionName.sendKeys("開発演習(Java+Oracle)");
		
		comprehensionLevel.selectByVisibleText("3");

		achievementLevel.clear();
		achievementLevel.sendKeys("10");
		
		impressions.clear();
		impressions.sendKeys("2026/01/29：（Test）登録済の報告レポート【所感】欄を修正しました。");
		
		review.clear();
		review.sendKeys("2026/01/29：（Test）登録済の報告レポート【一週間の振り返り】欄を修正しました。");
		
		// 入力値の入力を終えた段階でエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		weeklyReportRegistBtn.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
				
		// 画面遷移が確認できたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
				
		assertThat(webDriver.getTitle(), containsString("セクション詳細"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		final WebElement userDetail = webDriver.findElement(By.partialLinkText("ようこそ"));
		userDetail.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
						
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
						
		assertThat(webDriver.getTitle(), containsString("ユーザー詳細"));
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		final List<WebElement> detailBtnList = webDriver.findElements(By.className("btn-default"));

		detailBtnList.get(3).click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
								
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
								
		assertThat(webDriver.getTitle(), containsString("レポート詳細"));
		
		// 「学習項目」欄
		final WebElement sectionName = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/table/tbody/tr[2]/td[1]/p"));
		// 「理解度（1段階評価）」欄
		final WebElement comprehensionLevel = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/table/tbody/tr[2]/td[2]/p"));
		// 「目標の達成度」欄
		final WebElement achievementLevel = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[1]/td"));
		// 「所感」欄
		final WebElement impressions = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[2]/td"));
		// 「一週間の振り返り」欄
		final WebElement review = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[3]/td"));
		
		assertEquals("開発演習(Java+Oracle)", sectionName.getText());
		assertEquals("3", comprehensionLevel.getText());
		assertEquals("10", achievementLevel.getText());
		assertEquals("2026/01/29：（Test）登録済の報告レポート【所感】欄を修正しました。", impressions.getText());
		assertEquals("2026/01/29：（Test）登録済の報告レポート【一週間の振り返り】欄を修正しました。", review.getText());
	}

}
