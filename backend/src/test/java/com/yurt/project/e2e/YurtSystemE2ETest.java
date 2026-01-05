package com.yurt.project.e2e;
import io.github.bonigarcia.wdm.WebDriverManager; // Bu importu ekle
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // BeforeAll'ın static olmaması için
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class YurtSystemE2ETest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static String savedRoomNumber;

    // Jenkins'ten parametre olarak URL alabilir, yoksa varsayılanı kullanır
    private final String BASE_URL = System.getProperty("baseUrl", "http://localhost:5173");

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // --- JENKINS AYARI ---
        // Jenkinsfile üzerinden "-Dheadless=true" gönderildiğinde burası çalışır
        String headless = System.getProperty("headless");

        if ("true".equals(headless)) {
            System.out.println("--- HEADLESS MOD AKTİF (JENKINS) ---");
            options.addArguments("--headless"); // Ekranı açma
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080"); // Sanal ekran boyutu
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            System.out.println("--- ARAYÜZ MODU AKTİF (YEREL) ---");
            driver = new ChromeDriver(options); // Önce driver'ı oluştur
            driver.manage().window().maximize(); // Sonra maximize yap
        }

        // Eğer driver yukarıdaki else bloğunda oluşmadıysa (Headless ise) burada oluştur
        if (driver == null) {
            driver = new ChromeDriver(options);
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test @Order(1)
    @DisplayName("Başarılı Giriş Testi")
    void testSuccessfulLogin() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("12345"); // Senin şifren: admin123 mü 12345 mi kontrol et
        driver.findElement(By.id("login-btn")).click();
        wait.until(ExpectedConditions.urlContains("/students"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("students"));
    }

    @Test @Order(2)
    @DisplayName("Hatalı Giriş Testi")
    void testInvalidLogin() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("yanlis_user");
        driver.findElement(By.id("password")).sendKeys("yanlis_sifre");
        driver.findElement(By.id("login-btn")).click();

        WebElement errorDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error-message")));
        String expectedError = "Giriş başarısız! Kullanıcı adı veya şifre hatalı.";
        Assertions.assertEquals(expectedError, errorDiv.getText());
    }

    @Test @Order(3)
    @DisplayName("Mevcut Binaya Yeni Oda Ekle")
    void testCreateRoomInExistingBuilding() {
        String randomNum = "ODA-" + (int)(Math.random() * 9000 + 1000);
        savedRoomNumber = randomNum;
        System.out.println("Oluşturulacak Oda: " + savedRoomNumber);

        driver.get(BASE_URL + "/add-room");

        WebElement bSelectElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select")));
        Select bSelect = new Select(bSelectElem);
        wait.until(d -> bSelect.getOptions().size() > 1);

        try {
            bSelect.selectByVisibleText("A Blok");
        } catch (Exception e) {
            bSelect.selectByIndex(1);
        }

        driver.findElement(By.xpath("//input[@placeholder='Örn: 101']")).sendKeys(savedRoomNumber);
        driver.findElement(By.xpath("//input[@placeholder='Örn: 1']")).sendKeys("2");
        driver.findElement(By.xpath("//input[@placeholder='Örn: 4']")).sendKeys("3");

        WebElement saveBtn = driver.findElement(By.xpath("//button[text()='Odayı Kaydet']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveBtn);
        saveBtn.click();

        try {
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        } catch (Exception e) {
            System.out.println("Alert yakalanamadı.");
        }

        wait.until(ExpectedConditions.urlContains("/rooms"));
        try { Thread.sleep(1000); } catch (InterruptedException e) {} // Backend latency için
        driver.navigate().refresh();

        String xpath = "//*[contains(normalize-space(.), '" + savedRoomNumber + "')]";
        WebElement roomCard = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", roomCard);
        Assertions.assertTrue(roomCard.isDisplayed());
    }

    @Test @Order(4)
    @DisplayName("Yeni Oluşturulan Odaya Öğrenci Kaydet")
    void testRegisterStudentToNewRoom() {
        Assertions.assertNotNull(savedRoomNumber, "Önceki testte oda numarası kaydedilemedi!");
        driver.get(BASE_URL + "/add-student");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Ad']"))).sendKeys("Selenium");
        driver.findElement(By.xpath("//input[@placeholder='Soyad']")).sendKeys("Test");

        String randomTC = String.valueOf((long) (Math.random() * 10000000000L + 10000000000L)).substring(0, 11);
        driver.findElement(By.xpath("//input[@placeholder='TC Kimlik No']")).sendKeys(randomTC);
        driver.findElement(By.xpath("//input[@placeholder='E-posta']")).sendKeys("test" + randomTC + "@yurt.com");

        WebElement bSelectElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[contains(., 'Bina Seçiniz')]")));
        Select bSelect = new Select(bSelectElem);
        wait.until(d -> bSelect.getOptions().size() > 1);
        bSelect.selectByVisibleText("A Blok");

        // Oda Seçimi
        wait.until(ExpectedConditions.elementSelectionStateToBe(By.xpath("//select[contains(., 'Oda Seçiniz')]"), false));
        WebElement rSelectElem = driver.findElement(By.xpath("//select[contains(., 'Oda Seçiniz')]"));
        Select rSelect = new Select(rSelectElem);

        // Odanın listede görünmesini bekle
        String expectedRoomPart = savedRoomNumber;
        wait.until(d -> rSelect.getOptions().stream().anyMatch(opt -> opt.getText().contains(expectedRoomPart)));

        // Odayı seç
        rSelect.getOptions().stream()
                .filter(opt -> opt.getText().contains(expectedRoomPart))
                .findFirst()
                .ifPresent(opt -> rSelect.selectByVisibleText(opt.getText()));

        WebElement submitBtn = driver.findElement(By.xpath("//button[text()='Kaydı Tamamla']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        submitBtn.click();

        try { wait.until(ExpectedConditions.alertIsPresent()).accept(); } catch (Exception e) {}

        wait.until(ExpectedConditions.urlContains("/students"));
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + randomTC + "')]")));
    }

    @Test @Order(5)
    @DisplayName("Öğrenci Arama Testi")
    void testSearchStudent() {
        driver.get(BASE_URL + "/students");
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text']")));

        String searchTerm = "Selenium";
        searchInput.clear();
        searchInput.sendKeys(searchTerm);

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        WebElement resultRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tbody//tr[contains(., '" + searchTerm + "')]")
        ));
        Assertions.assertTrue(resultRow.isDisplayed());
    }

    @Test @Order(6)
    @DisplayName("Mükerrer TC ile Kayıt Denemesi")
    void testDuplicateRegistration() {
        String duplicateTC = "99887766554";

        // --- 1. İlk Kayıt ---
        driver.get(BASE_URL + "/add-student");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Ad']"))).sendKeys("Duplicate");
        driver.findElement(By.xpath("//input[@placeholder='Soyad']")).sendKeys("User");
        driver.findElement(By.xpath("//input[@placeholder='TC Kimlik No']")).sendKeys(duplicateTC);
        driver.findElement(By.xpath("//input[@placeholder='E-posta']")).sendKeys("dup@test.com");

        new Select(driver.findElement(By.xpath("//select[contains(., 'Bina Seçiniz')]"))).selectByIndex(1);

        By roomSelectLoc = By.xpath("//select[contains(., 'Oda Seçiniz')]");
        wait.until(d -> {
            try { return new Select(d.findElement(roomSelectLoc)).getOptions().size() > 1; }
            catch (StaleElementReferenceException e) { return false; }
        });
        selectFirstAvailableOption(roomSelectLoc);

        WebElement submitBtn = driver.findElement(By.xpath("//button[text()='Kaydı Tamamla']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        submitBtn.click();
        try { wait.until(ExpectedConditions.alertIsPresent()).accept(); } catch (Exception e) {}

        // --- 2. İkinci Kayıt (Aynı TC) ---
        driver.get(BASE_URL + "/add-student");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Ad']"))).sendKeys("Duplicate");
        driver.findElement(By.xpath("//input[@placeholder='Soyad']")).sendKeys("User 2");
        driver.findElement(By.xpath("//input[@placeholder='TC Kimlik No']")).sendKeys(duplicateTC);
        driver.findElement(By.xpath("//input[@placeholder='E-posta']")).sendKeys("dup2@test.com");

        new Select(driver.findElement(By.xpath("//select[contains(., 'Bina Seçiniz')]"))).selectByIndex(1);
        wait.until(d -> {
            try { return new Select(d.findElement(roomSelectLoc)).getOptions().size() > 1; }
            catch (StaleElementReferenceException e) { return false; }
        });
        selectFirstAvailableOption(roomSelectLoc);

        WebElement submitBtn2 = driver.findElement(By.xpath("//button[text()='Kaydı Tamamla']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitBtn2);
        submitBtn2.click();

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();
            Assertions.assertFalse(alertText.contains("başarıyla"), "Sistem mükerrer TC'ye izin verdi!");
        } catch (TimeoutException e) {
            Assertions.fail("Beklenen hata uyarısı çıkmadı.");
        }
    }

    @Test @Order(7)
    @DisplayName("Öğrenci Silme Testi")
    void testDeleteStudent() {
        driver.get(BASE_URL + "/students");
        String deleteXpath = "//tr[contains(., 'Selenium')]//button[contains(text(), 'Sil') or contains(@class, 'delete')]";

        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(deleteXpath)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", deleteBtn);
        deleteBtn.click();

        try { wait.until(ExpectedConditions.alertIsPresent()).accept(); } catch (Exception e) {}
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        driver.navigate().refresh();
        boolean isPresent = !driver.findElements(By.xpath("//tr[contains(., 'Selenium')]")).isEmpty();
        Assertions.assertFalse(isPresent, "Öğrenci silinmesine rağmen hala listede!");
    }

    // --- Geri Eklenen Test ---
    @Test @Order(8)
    @DisplayName("Oda Silme Testi")
    void testDeleteRoom() {
        Assertions.assertNotNull(savedRoomNumber);
        driver.get(BASE_URL + "/rooms");

        // Odayı bulmak için genel yapı
        String roomDeleteXpath = "//*[contains(text(), '" + savedRoomNumber + "')]/ancestor::div[contains(@class, 'card')]//button[contains(text(), 'Sil')]";
        if (driver.findElements(By.xpath(roomDeleteXpath)).isEmpty()) {
            // Tablo yapısındaysa
            roomDeleteXpath = "//tr[contains(., '" + savedRoomNumber + "')]//button[contains(text(), 'Sil')]";
        }

        try {
            WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(roomDeleteXpath)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", deleteBtn);
            deleteBtn.click();
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        } catch (Exception e) {
            System.out.println("Oda silme butonu bulunamadı veya alert çıkmadı.");
        }

        driver.navigate().refresh();
        Assertions.assertFalse(driver.getPageSource().contains(savedRoomNumber), "Oda silinemedi!");
    }

    @Test @Order(9)
    @DisplayName("Güvenli Çıkış (Logout) Testi")
    void testLogout() {
        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Çıkış') or contains(text(), 'Logout') or contains(text(), 'Exit')]")
        ));
        logoutBtn.click();

        try {
            wait.until(ExpectedConditions.alertIsPresent()).accept();
        } catch (TimeoutException e) {
            System.out.println("Çıkış alert'i gelmedi.");
        }

        wait.until(ExpectedConditions.urlContains("/login"));
        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(text(), 'Giriş') or @id='login-btn']")
        ));
        Assertions.assertTrue(loginBtn.isDisplayed());
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- YARDIMCI METOT ---
    private void selectFirstAvailableOption(By locator) {
        Select select = new Select(driver.findElement(locator));
        boolean found = false;
        for (WebElement option : select.getOptions()) {
            if (option.isEnabled() && !option.getAttribute("value").isEmpty()) {
                select.selectByVisibleText(option.getText());
                found = true;
                break;
            }
        }
        if (!found) Assertions.fail("Müsait oda bulunamadı!");
    }
}