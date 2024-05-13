package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePageNavigation {
    private WebDriver driver;

    // Constructor
    public HomePageNavigation(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Define WebElement locators using @FindBy annotations
    @FindBy(xpath = "//android.widget.TextView[@resource-id='com.google.android.gms:id/credential_primary_label' and @text='Skat556']")
    private WebElement credentialLabel;

    @FindBy(xpath = "//android.view.View[@content-desc='skat556']")
    private WebElement viewElement;

    @FindBy(xpath = "//android.widget.Button[@content-desc='Save']/android.view.ViewGroup")
    private WebElement saveButton;

    @FindBy(xpath = "//android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_button']")
    private WebElement allowButton;

    @FindBy(xpath = "//android.widget.Button[@content-desc='5 unread messages']")
    private WebElement unreadMessagesButton;

    // Getter methods
    public WebElement getCredentialLabel() {
        return credentialLabel;
    }

    public WebElement getViewElement() {
        return viewElement;
    }

    public WebElement getSaveButton() {
        return saveButton;
    }

    public WebElement getAllowButton() {
        return allowButton;
    }

    public WebElement getUnreadMessagesButton() {
        return unreadMessagesButton;
    }

    public void clickIdSelect() {
        credentialLabel.click();
    }

    // Define methods to perform actions on elements
    public void clickSaveButton() {
        saveButton.click();
    }

    public void clickAllowButton() {
        allowButton.click();
    }

    // Add more methods as needed
}