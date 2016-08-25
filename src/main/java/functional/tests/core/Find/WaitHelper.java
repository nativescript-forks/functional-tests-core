package functional.tests.core.Find;

import functional.tests.core.Appium.Client;
import functional.tests.core.Element.UIElement;
import functional.tests.core.Exceptions.AppiumException;
import functional.tests.core.Log.Log;
import functional.tests.core.Settings.Settings;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.Date;
import java.util.List;

public class WaitHelper {
    private Client client;
    private FindHelper find;

    public WaitHelper(Client client) {
        this.client = client;
        this.find = new FindHelper(this.client);
    }

    public boolean waitForVisible(By locator, int timeOut, boolean failOnNotVisible) {
        this.client.setWait(timeOut);
        UIElement result;
        try {
            result = this.find.byLocator(locator);
        } catch (Exception e) {
            result = null;
        }
        this.client.setWait(Settings.defaultTimeout);
        if (result != null) {
            return true;
        } else {
            if (failOnNotVisible) {
                Assert.fail("Failed to find element: " + locator.toString());
            }
            return false;
        }
    }

    public boolean waitForVisible(By locator, boolean failOnNotVisible) {
        return waitForVisible(locator, Settings.defaultTimeout, failOnNotVisible);
    }

    public boolean waitForVisible(By locator) {
        return waitForVisible(locator, Settings.defaultTimeout, false);
    }

    public boolean waitForNotVisible(By locator, int timeOut, boolean failOnVisble) {
        this.client.setWait(1);
        long startTime = new Date().getTime();
        boolean found = true;
        for (int i = 0; i < 1000; i++) {
            long currentTime = new Date().getTime();
            if ((currentTime - startTime) < timeOut * 1000) {
                List<UIElement> elements = null;
                try {
                    elements = Find.findElementsByLocator(locator);
                } catch (Exception e) {
                }

                if ((elements != null) && (elements.size() != 0)) {
                    Log.debug("OldElement exists: " + locator.toString());
                } else {
                    found = false;
                    break;
                }
            }
        }
        this.client.setWait(Settings.defaultTimeout);
        if (found) {
            String error = "OldElement still visible: " + locator.toString();
            Log.error(error);
            if (failOnVisble) {
                Assert.fail(error);
            }
        } else {
            Log.debug("OldElement not found: " + locator.toString());
        }
        return found;
    }

    public boolean waitForNotVisible(By locator, boolean failOnVisible) {
        return waitForNotVisible(locator, Settings.shortTimeout, failOnVisible);
    }

    public boolean waitForNotVisible(By locator) throws AppiumException {
        return waitForNotVisible(locator, Settings.shortTimeout, true);
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}