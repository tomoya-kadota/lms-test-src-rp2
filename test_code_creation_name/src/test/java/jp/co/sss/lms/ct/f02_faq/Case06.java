package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
 * 結合テスト よくある質問機能 ケース06
 * 
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		final WebElement headerFunction = webDriver.findElement(By.className("dropdown"));

		headerFunction.click();

		final WebElement help = webDriver.findElement(By.linkText("ヘルプ"));

		// ヘルプ画面へ画面遷移する前のエビデンス01を取得
		getEvidence(new Object() {}, "01");

		help.click();

		// ヘルプ画面へ画面遷移した後のエビデンス02を取得
		getEvidence(new Object() {}, "02");

		assertThat(webDriver.getTitle(), containsString("ヘルプ"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		final WebElement faq = webDriver.findElement(By.linkText("よくある質問"));

		faq.click();

		// webDriverにタブの切り替えを認識させるために、現在開いているウィンドウを「開いた順」にリスト要素へ追加
		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		// 別タブで開かれた「よくある質問画面」へ操作を切り替える
		webDriver.switchTo().window((String) windowHandles[1]);

		// よくある質問画面へ画面遷移した後のエビデンス01を取得
		getEvidence(new Object() {}, "01");

		assertThat(webDriver.getTitle(), containsString("よくある質問"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		final WebElement categorySearch = webDriver.findElement(By.linkText("【研修関係】"));
		categorySearch.click();

		// カテゴリ検索の結果を表示させた後、エビデンス01を取得
		getEvidence(new Object() {}, "01");

		assertThat(webDriver.getPageSource(), containsString("研修の申し込みはどのようにすれば良いですか？"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		final WebElement question = webDriver.findElement(By.className("sorting_1"));
		question.click();

		// 検索結果の質問をクリックさせた後、エビデンス01を取得
		getEvidence(new Object() {}, "01");

		final WebElement answer = webDriver.findElement(By.className("fs18"));
		assertEquals("A. 受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、事情をお伺いした上で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。",
				answer.getText());
	}

}
