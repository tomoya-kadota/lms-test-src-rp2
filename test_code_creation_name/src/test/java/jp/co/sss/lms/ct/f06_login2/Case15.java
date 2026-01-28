package jp.co.sss.lms.ct.f06_login2;

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
 * 結合テスト ログイン機能②
 * ケース15
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース15 受講生 初回ログイン 利用規約に不同意")
public class Case15 {

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		final WebElement password = webDriver.findElement(By.name("password"));
		final WebElement loginBtn = webDriver.findElement(By.className("btn-primary"));

		loginId.clear();
		loginId.sendKeys("StudentAA02");
		password.clear();
		password.sendKeys("StudentAA02");

		// 入力値を入力した際のエビデンス01を取得
		getEvidence(new Object() {}, "01");

		loginBtn.click();

		// 利用規約画面へ画面遷移する際のエビデンス02を取得
		getEvidence(new Object() {}, "02");

		assertThat(webDriver.getTitle(), containsString("セキュリティ規約"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックをせず「次へ」ボタンを押下")
	void test03() {
		final WebElement nextBtn = webDriver.findElement(By.className("btn-primary"));
		
		nextBtn.click();
		
		// エラーメッセージ出力されるまでの待ち時間を準備
		visibilityTimeout(By.className("error"), 10);
		
		WebElement errorMessage = webDriver.findElement(By.className("error"));
		
		// 「次へ」ボタンを押下した後のエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		assertThat(errorMessage.getText(), is(containsString("セキュリティ規約への同意は必須です。")));
	}

}
