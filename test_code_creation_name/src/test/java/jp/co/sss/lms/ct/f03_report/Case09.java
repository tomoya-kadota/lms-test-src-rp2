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
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		final WebElement userDetail = webDriver.findElement(By.partialLinkText("ようこそ"));
		userDetail.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
						
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
						
		assertThat(webDriver.getTitle(), containsString("ユーザー詳細"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		final List<WebElement> reviseBtnList = webDriver.findElements(By.cssSelector("input[type=\"submit\"][value=\"修正する\"]"));

		reviseBtnList.get(0).click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
								
		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
								
		assertThat(webDriver.getTitle(), containsString("レポート登録"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		final WebElement sectionName = webDriver.findElement(By.id("intFieldName_0"));
		final Select comprehensionLevel = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		final WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		final WebElement impressions = webDriver.findElement(By.id("content_1"));
		final WebElement review = webDriver.findElement(By.id("content_2"));
		final WebElement weeklyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
		
		// 入力値の入力操作
		sectionName.clear();
		comprehensionLevel.selectByVisibleText("3");
		achievementLevel.clear();
		achievementLevel.sendKeys("10");
		impressions.clear();
		impressions.sendKeys("2026/01/29：（Test）登録済の報告レポート【所感】欄を修正しました。");
		review.clear();
		review.sendKeys("2026/01/29：（Test）登録済の報告レポート【一週間の振り返り】欄を修正しました。");
		
		// 入力値【学習項目：未入力　理解度：入力】の操作処理ができたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		weeklyReportRegistBtn.click();
		
		// 入力値【学習項目：未入力　理解度：入力】のエラー出力処理ができたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		WebElement errorInput = webDriver.findElement(By.className("errorInput"));
		
		assertTrue(errorInput.isDisplayed());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		final WebElement sectionName = webDriver.findElement(By.id("intFieldName_0"));
		final Select comprehensionLevel = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		final WebElement weeklyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
		
		// 入力値の入力操作
		sectionName.clear();
		sectionName.sendKeys("開発演習(Java+Oracle)");
		comprehensionLevel.selectByVisibleText("");
		
		// 入力値【学習項目：開発演習(Java+Oracle)　理解度：空白】の操作処理ができたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		weeklyReportRegistBtn.click();
		
		WebElement errorInput = webDriver.findElement(By.className("errorInput"));
		
		// 入力値【学習項目：開発演習(Java+Oracle)　理解度：空白】のエラー出力処理ができたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertTrue(errorInput.isDisplayed());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		final Select comprehensionLevel = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		final WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		final WebElement weeklyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
		
		// 入力値の入力操作
		comprehensionLevel.selectByVisibleText("3");
		achievementLevel.clear();
		achievementLevel.sendKeys("十");
		
		// 入力値【目標の達成度：十　※10の漢数字】の操作処理ができたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
				
		weeklyReportRegistBtn.click();
				
		WebElement errorInput = webDriver.findElement(By.className("errorInput"));
				
		// 入力値【目標の達成度：十　※10の漢数字】のエラー出力処理ができたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
				
		assertTrue(errorInput.isDisplayed());
		
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		final WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		final WebElement weeklyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
	
		// 入力値の入力操作
		achievementLevel.clear();
		achievementLevel.sendKeys("11");
		
		// 入力値【目標の達成度：11　※入力範囲外】の操作処理ができたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
				
		weeklyReportRegistBtn.click();
				
		WebElement errorInput = webDriver.findElement(By.className("errorInput"));
				
		// 入力値【目標の達成度：11　※入力範囲外】のエラー出力処理ができたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
				
		assertTrue(errorInput.isDisplayed());
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		final WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		final WebElement impressions = webDriver.findElement(By.id("content_1"));
		final WebElement weeklyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
		
		// 入力値の入力操作
		achievementLevel.clear();
		impressions.clear();

		// 入力値【目標の達成度：未入力　所感：未入力】の操作処理ができたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		weeklyReportRegistBtn.click();
		
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		
		// 入力値【目標の達成度：未入力　所感：未入力】のエラー出力処理ができたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertTrue(errorInputList.get(0).isDisplayed());
		assertTrue(errorInputList.get(1).isDisplayed());
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		final WebElement achievementLevel = webDriver.findElement(By.id("content_0"));
		final WebElement impressions = webDriver.findElement(By.id("content_1"));
		final WebElement review = webDriver.findElement(By.id("content_2"));
		final WebElement weeklyReportRegistBtn = webDriver.findElement(By.className("btn-primary"));
		
		// 入力値の入力操作
		review.clear();
		
		achievementLevel.sendKeys("10");
		impressions.sendKeys("あああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		review.sendKeys("あああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああああ");

		// 入力値【所感：2001文字　一週間の振り返り：2001文字】の操作処理ができたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		weeklyReportRegistBtn.click();
		
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		
		// 入力値【所感：2000文字　一週間の振り返り：2000文字】のエラー出力処理ができたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertTrue(errorInputList.get(0).isDisplayed());
		assertTrue(errorInputList.get(1).isDisplayed());
	}

}
