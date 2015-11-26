package functional.tests.core.Find;

import functional.tests.core.Appium.Client;
import functional.tests.core.Enums.PlatformType;
import functional.tests.core.Log.Log;
import functional.tests.core.Settings.Settings;
import io.appium.java_client.MobileElement;
import org.apache.commons.lang.NotImplementedException;
import org.openqa.selenium.By;

import java.util.List;

public class Find {

    private static MobileElement element(By locator) {
        return (MobileElement) Client.driver.findElement(locator);
    }

    private static List<MobileElement> elements(By locator) {
        return (List<MobileElement>) Client.driver.findElements(locator);
    }

    private static By findByTextLocator(String controlType, String value,
                                        boolean exactMatch) {
        if (Settings.platform == PlatformType.Andorid) {
            if (exactMatch) {
                return By.xpath("//" + controlType + "[@content-desc=\""
                        + value + "\" or @resource-id=\"" + value
                        + "\" or @text=\"" + value + "\"]");
            } else {
                return By.xpath("//" + controlType + "[@content-desc=\""
                        + value + "\" or @resource-id=\"" + value
                        + "\" or @text=\"" + value
                        + "\"] | //*[contains(translate(@content-desc,\""
                        + value + "\",\"" + value + "\"), \"" + value
                        + "\") or contains(translate(@text,\"" + value
                        + "\",\"" + value + "\"), \"" + value
                        + "\") or @resource-id=\"" + value + "\"]");
            }
        } else if (Settings.platform == PlatformType.iOS) {
            if (exactMatch) {
                // TODO : Fix the logic in this if statement
                String up = value.toUpperCase();
                String down = value.toLowerCase();
                return By.xpath("//" + controlType
                        + "[@visible=\"true\" and (contains(translate(@name,\""
                        + up + "\",\"" + down + "\"), \"" + down
                        + "\") or contains(translate(@hint,\"" + up + "\",\""
                        + down + "\"), \"" + down
                        + "\") or contains(translate(@label,\"" + up + "\",\""
                        + down + "\"), \"" + down
                        + "\") or contains(translate(@value,\"" + up + "\",\""
                        + down + "\"), \"" + down + "\"))]");
            } else {
                String up = value.toUpperCase();
                String down = value.toLowerCase();
                return By.xpath("//" + controlType
                        + "[@visible=\"true\" and (contains(translate(@name,\""
                        + up + "\",\"" + down + "\"), \"" + down
                        + "\") or contains(translate(@hint,\"" + up + "\",\""
                        + down + "\"), \"" + down
                        + "\") or contains(translate(@label,\"" + up + "\",\""
                        + down + "\"), \"" + down
                        + "\") or contains(translate(@value,\"" + up + "\",\""
                        + down + "\"), \"" + down + "\"))]");
            }
        } else {
            String error = "findByText not implemented for platform: " + Settings.platform;
            Log.fatal(error);
            throw new NotImplementedException(error);
        }
    }

    /**
     * Find an element that has some attribute with specified value.
     */
    public static MobileElement findByText(String controlType, String value, boolean exactMatch) {
        return element(findByTextLocator(controlType, value, exactMatch));
    }

    /**
     * Find an element that has some attribute with specified value.
     */
    public static MobileElement findByText(String value, boolean exactMatch) {
        return element(findByTextLocator("*", value, exactMatch));
    }

    /**
     * Find an element that has some attribute with specified value.
     */
    public static MobileElement findByText(String value) {
        return element(findByTextLocator("*", value, true));
    }
}
