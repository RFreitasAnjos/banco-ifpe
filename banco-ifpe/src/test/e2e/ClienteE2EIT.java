package br.com.ifpe.banco.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClienteE2EIT {

    private static WebDriver driver;

    private static final String BASE_URL = System.getenv("APP_BASE_URL") != null
            ? System.getenv("APP_BASE_URL")
            : "http://localhost:8080/banco-ifpe";

    private static final int WAIT_SECONDS = 20;

    @BeforeClass
    public static void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        String chromeBinary = resolveChromeBinary();
        if (chromeBinary != null) {
            options.setBinary(chromeBinary);
        }

        try {
            if (chromeBinary != null) {
                WebDriverManager.chromedriver().browserBinary(chromeBinary).setup();
            } else {
                WebDriverManager.chromedriver().setup();
            }
        } catch (Exception e) {
            System.out.println("[E2E] Falha ao resolver chromedriver automaticamente: " + e.getMessage());
        }

        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--disable-extensions",
                "--remote-debugging-port=9222",
                "--window-size=1920,1080",
                "--user-data-dir=/tmp/chrome-e2e-profile-" + UUID.randomUUID()
        );

        driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(WAIT_SECONDS));
    }

    private static String resolveChromeBinary() {
        String envChrome = System.getenv("CHROME_BIN");
        if (envChrome != null && !envChrome.trim().isEmpty()) {
            return envChrome;
        }

        String[] candidates = {
                "/usr/bin/google-chrome",
                "/usr/bin/google-chrome-stable",
                "/usr/bin/chromium-browser",
                "/usr/bin/chromium"
        };

        for (String candidate : candidates) {
            if (Files.exists(Paths.get(candidate))) {
                return candidate;
            }
        }

        return null;
    }

    @AfterClass
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void test01_deveCarregarPaginaLista() {
        driver.get(BASE_URL + "/content/clientes/listarClientes.xhtml");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));
        wait.until(ExpectedConditions.titleContains("Clientes"));

        assertTrue("Titulo deve conter 'Clientes'", driver.getTitle().contains("Clientes"));
        assertTrue("Pagina deve conter link 'Novo Cliente'",
                driver.getPageSource().contains("Novo Cliente"));
        assertTrue("Pagina deve conter titulo da secao",
                driver.getPageSource().contains("Clientes Cadastrados"));
    }

    @Test
    public void test02_deveAbrirFormularioCadastro() {
        driver.get(BASE_URL + "/content/clientes/criarCliente.xhtml");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));
        wait.until(ExpectedConditions.titleContains("Novo Cliente"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("formCadastro:nome")));

        assertTrue("Campo nome deve existir",
                driver.findElements(By.id("formCadastro:nome")).size() > 0);
        assertTrue("Campo CPF deve existir",
            driver.findElements(By.id("formCadastro:cpf")).size() > 0);
        assertTrue("Campo email deve existir",
                driver.findElements(By.id("formCadastro:email")).size() > 0);
        assertTrue("Campo data de nascimento deve existir",
            driver.findElements(By.id("formCadastro:dataNascimento")).size() > 0);
        assertTrue("Campo estado civil deve existir",
            driver.findElements(By.id("formCadastro:estadoCivil")).size() > 0);
        assertTrue("Campo genero deve existir",
            driver.findElements(By.id("formCadastro:genero")).size() > 0);
        assertTrue("Campo score deve existir",
            driver.findElements(By.id("formCadastro:score")).size() > 0);
    }

    @Test
    public void test03_deveValidarCamposObrigatoriosNoCadastro() {
        driver.get(BASE_URL + "/content/clientes/criarCliente.xhtml");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("formCadastro:nome")));

        driver.findElement(By.xpath("//input[@type='submit' and @value='Salvar']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Nome e obrigatorio')]")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'CPF e obrigatorio')]")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Email e obrigatorio')]")));

        // As mensagens de campos com conversao (ex.: LocalDate/enum) podem variar por ambiente.
        assertTrue("A validacao deve apresentar mensagens obrigatorias na tela",
            driver.getPageSource().contains("obrigatorio")
                || driver.getPageSource().contains("obrigatoria"));

        assertTrue("Deve permanecer na pagina de criacao apos validacao",
                driver.getCurrentUrl().contains("criarCliente.xhtml"));
    }

    @Test
    public void test04_naoDeveRedirecionarParaListaQuandoFaltaremCamposDoDominio() {
        driver.get(BASE_URL + "/content/clientes/criarCliente.xhtml");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_SECONDS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("formCadastro:nome")));

        driver.findElement(By.id("formCadastro:nome")).sendKeys("Cliente E2E");
        driver.findElement(By.id("formCadastro:cpf")).sendKeys("12345678901");
        driver.findElement(By.id("formCadastro:email")).sendKeys("e2e_" + System.currentTimeMillis() + "@empresa.com");
        driver.findElement(By.id("formCadastro:telefone")).sendKeys("11999990001");
        driver.findElement(By.xpath("//input[@type='submit' and @value='Salvar']")).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("criarCliente.xhtml"),
                ExpectedConditions.presenceOfElementLocated(By.id("formCadastro:nome"))
        ));

        assertFalse("Nao deve redirecionar para lista enquanto campos obrigatorios do dominio nao estiverem na tela",
                driver.getCurrentUrl().contains("listarClientes.xhtml"));
    }

}


