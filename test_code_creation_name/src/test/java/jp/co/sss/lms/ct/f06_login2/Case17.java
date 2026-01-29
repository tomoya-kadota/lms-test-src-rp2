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
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		final WebElement nextBtn = webDriver.findElement(By.className("btn-primary"));
		final WebElement checkBox = webDriver.findElement(By.cssSelector("input[type='checkbox'][name='securityFlg'][value='1']"));
		
		checkBox.click();
		
		// 「同意します」チェックボックスにチェックを入れた後のエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		nextBtn.click();
		
		// 「次へ」ボタンを押下した後のエビデンス01を取得
		getEvidence(new Object() {}, "02");
		
		assertThat(webDriver.getTitle(), containsString("パスワード変更"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {
		WebElement changeBtn = webDriver.findElement(By.cssSelector("button[type='submit']"));
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA02");
		
		newPassword.clear();
		newPassword.sendKeys("Student8080");
		
		passwordConfirm.clear();
		passwordConfirm.sendKeys("Student8080");
		
		changeBtn.click();
		
		// モーダルウィンドウの出力完了まで待ち処理実施
		visibilityTimeout(By.id("upd-btn"), 5);
		
		// モーダルウィンドウの出力が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		WebElement modalChangeBtn = webDriver.findElement(By.id("upd-btn"));
		
		modalChangeBtn.click();
		
		// 画面遷移完了まで待ち処理実施
		pageLoadTimeout(5);
		
		// 画面遷移が確認できたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertThat(webDriver.getTitle(), containsString("コース詳細"));
		
	}

}
