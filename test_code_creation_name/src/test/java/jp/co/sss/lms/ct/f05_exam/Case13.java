package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト 試験実施機能 ケース13
 * 
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果0点")
public class Case13 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		final List<WebElement> sectionDetailBtnList = webDriver
				.findElements(By.cssSelector("input[type='submit'][value='詳細']"));

		// 「試験有」の研修日の「詳細」ボタンをクリックする
		sectionDetailBtnList.get(1).click();

		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);

		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");

		assertThat(webDriver.getTitle(), containsString("セクション詳細"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {
		final WebElement detailBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='詳細']"));

		detailBtn.click();

		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);

		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");

		assertThat(webDriver.getTitle(), containsString("試験【ITリテラシー①】"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		final WebElement questionStartBtn = webDriver
				.findElement(By.cssSelector("input[type=\"submit\"][value=\"試験を開始する\"]"));

		questionStartBtn.click();

		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);

		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");

		assertThat(webDriver.getTitle(), containsString("ITリテラシー①"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 未回答の状態で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() {
		final WebElement confirmationBtn = webDriver
				.findElement(By.cssSelector("input[type=\"submit\"][value=\"確認画面へ進む\"]"));
		final List<WebElement> panelHeadingList = webDriver.findElements(By.className("panel-heading"));

		for (int i = 0; i < 12; i++) {
			// エビデンス用のサフィックスを整形
			String suffix = String.format("%02d", i + 1);

			// 問題番号要素をリストから取得
			WebElement panelHeading = panelHeadingList.get(i);

			// 問題番号要素（初期値：[0]）ごとにスクロール処理を実行
			((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, arguments[0].getBoundingClientRect().top + window.pageYOffset - 53);",panelHeading);
			
			// スクロール後、エビデンス「suffix」（初期値：01、12まで取得）を取得
			getEvidence(new Object() {}, suffix);
		}

		confirmationBtn.click();

		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);

		// 画面遷移が確認できたらエビデンス13を取得
		getEvidence(new Object() {}, "13");

		assertThat(webDriver.getTitle(), containsString("ITリテラシー①"));

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		final WebElement sendBtn = webDriver.findElement(By.id("sendButton"));
		
		// 待ち処理によって時間を少し経過させ、回答時間に関するエラーを回避する
		Thread.sleep(3000);
		
		scrollBy("2200");
		
		sendBtn.click();
		
		// アラートへ操作を切り替え、「OK」ボタンを押下
		webDriver.switchTo().alert().accept();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);

		// 画面遷移が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		final WebElement scoreResult = webDriver.findElement(By.tagName("h2"));

		assertThat(webDriver.getTitle(), containsString("ITリテラシー①"));
		assertThat(scoreResult.getText(), containsString("あなたのスコア：0.0点"));
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {
		final WebElement returnBtn = webDriver.findElement(By.cssSelector("input[type='submit'][value='戻る']"));
		
		scrollBy("5300");
		
		returnBtn.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
		
		// 過去の試験結果欄「詳細」ボタンをリストで取得
		final List<WebElement> scoreHistoryDetailBtnList = webDriver.findElements(By.cssSelector("input[type=\"submit\"][value=\"詳細\"]"));
		
		// 過去の試験結果から最新の試験結果が表示されるまでスクロール
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, arguments[0].getBoundingClientRect().top + window.pageYOffset - 53);",scoreHistoryDetailBtnList.getLast());
		
		// 最新の試験結果が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		// 直近の試験結果が表示されている要素について、xpathを成型
		String latestHistoryScoreXpath = "//*[@id=\"main\"]/div/table[2]/tbody/tr[" + (scoreHistoryDetailBtnList.size() + 1) +"]/td[2]";
		WebElement latestHistoryScore = webDriver.findElement(By.xpath(latestHistoryScoreXpath));
		
		assertThat(webDriver.getTitle(), containsString("試験【ITリテラシー①】"));
		assertThat(latestHistoryScore.getText(), containsString("0.0点"));
		
	}

}
