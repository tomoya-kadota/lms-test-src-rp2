package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能① ケース02
 * 
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		final WebElement loginId = webDriver.findElement(By.name("loginId"));
		final WebElement password = webDriver.findElement(By.name("password"));
		final WebElement loginBtn = webDriver.findElement(By.className("btn-primary"));

		loginId.clear();
		loginId.sendKeys("StudentAA10");
		password.clear();
		password.sendKeys("StudentAA10");

		// 入力値を入力した際のエビデンス01を取得
		getEvidence(new Object() {}, "01");

		loginBtn.click();

		// エラーメッセージを出力させたエビデンス02を取得
		getEvidence(new Object() {}, "02");

		assertThat(webDriver.getTitle(), containsString("ログイン"));

		// index.html：15行目 <span> より要素を取得
		final WebElement error = webDriver.findElement(By.cssSelector("span[class='help-inline error']"));
		
		// 画面に出力されたエラーメッセージを取得し、「ログインに失敗しました」が部分一致するか確認
		assertThat(error.getText(), containsString("ログインに失敗しました。"));
	}

}
