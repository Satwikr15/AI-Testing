package com.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ProxyTest {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();

        Proxy proxy = new Proxy();
        proxy.setHttpProxy("your.proxy.host:port");
        proxy.setSslProxy("your.proxy.host:port");

        ChromeOptions options = new ChromeOptions();
        options.setProxy(proxy);

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://httpbin.org/ip"); // shows your public IP
    }
}

