package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
		
		// 「次へ」ボタンを押下した後のエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertThat(webDriver.getTitle(), containsString("パスワード変更"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {
		WebElement changeBtn = webDriver.findElement(By.cssSelector("button[type='submit']"));
		
		changeBtn.click();
		
		// モーダルウィンドウの出力完了まで待ち処理実施
		visibilityTimeout(By.id("upd-btn"), 5);
		
		// モーダルウィンドウの出力が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		WebElement modalChangeBtn = webDriver.findElement(By.id("upd-btn"));
		
		modalChangeBtn.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorMassageList = webDriver.findElements(By.className("error"));
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		
		// エラーメッセージの出力が確認できたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertThat(errorMassageList.get(1).getText(), is(containsString("現在のパスワードは必須です。")));
		assertThat(errorMassageList.get(2).getText(), is(containsString("パスワードは必須です。")));
		assertThat(errorMassageList.get(3).getText(), is(containsString("確認パスワードは必須です。")));
		
		assertTrue(errorInputList.get(0).isDisplayed());
		assertTrue(errorInputList.get(1).isDisplayed());
		assertTrue(errorInputList.get(2).isDisplayed());
		
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {
		WebElement changeBtn = webDriver.findElement(By.cssSelector("button[type='submit']"));
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA02");
		
		newPassword.clear();
		newPassword.sendKeys("StudentAA02StudentAA0");
		
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA02StudentAA0");
		
		changeBtn.click();
		
		// モーダルウィンドウの出力完了まで待ち処理実施
		visibilityTimeout(By.id("upd-btn"), 5);
		
		// モーダルウィンドウの出力が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		WebElement modalChangeBtn = webDriver.findElement(By.id("upd-btn"));
		
		modalChangeBtn.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorMassageList = webDriver.findElements(By.className("error"));
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		
		// エラーメッセージの出力が確認できたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertThat(errorMassageList.get(1).getText(), is(containsString("パスワードの長さが最大値(20)を超えています。")));
		
		assertTrue(errorInputList.get(0).isDisplayed());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {
		WebElement changeBtn = webDriver.findElement(By.cssSelector("button[type='submit']"));
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA02");
		
		newPassword.clear();
		newPassword.sendKeys("StudentAAStudentAA");
		
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAAStudentAA");
		
		changeBtn.click();
		
		// モーダルウィンドウの出力完了まで待ち処理実施
		visibilityTimeout(By.id("upd-btn"), 5);
		
		// モーダルウィンドウの出力が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		WebElement modalChangeBtn = webDriver.findElement(By.id("upd-btn"));
		
		modalChangeBtn.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorMassageList = webDriver.findElements(By.className("error"));
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		
		// エラーメッセージの出力が確認できたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertThat(errorMassageList.get(1).getText(), is(containsString("「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。")));
		
		assertTrue(errorInputList.get(0).isDisplayed());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {
		WebElement changeBtn = webDriver.findElement(By.cssSelector("button[type='submit']"));
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		WebElement newPassword = webDriver.findElement(By.id("password"));
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA02");
		
		newPassword.clear();
		newPassword.sendKeys("StudentAA02Student");
		
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA02StudentAA");
		
		changeBtn.click();
		
		// モーダルウィンドウの出力完了まで待ち処理実施
		visibilityTimeout(By.id("upd-btn"), 5);
		
		// モーダルウィンドウの出力が確認できたらエビデンス01を取得
		getEvidence(new Object() {}, "01");
		
		WebElement modalChangeBtn = webDriver.findElement(By.id("upd-btn"));
		
		modalChangeBtn.click();
		
		visibilityTimeout(By.className("error"), 5);
		List<WebElement> errorMassageList = webDriver.findElements(By.className("error"));
		List<WebElement> errorInputList = webDriver.findElements(By.className("errorInput"));
		
		// エラーメッセージの出力が確認できたらエビデンス02を取得
		getEvidence(new Object() {}, "02");
		
		assertThat(errorMassageList.get(1).getText(), is(containsString("パスワードと確認パスワードが一致しません。")));
		
		assertTrue(errorInputList.get(0).isDisplayed());
	}

}
