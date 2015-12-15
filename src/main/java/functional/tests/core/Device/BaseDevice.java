package functional.tests.core.Device;

import functional.tests.core.Appium.Client;
import functional.tests.core.Device.Android.Adb;
import functional.tests.core.Device.Android.AndroidDevice;
import functional.tests.core.Device.iOS.iOSDevice;
import functional.tests.core.Enums.DeviceType;
import functional.tests.core.Enums.PlatformType;
import functional.tests.core.Exceptions.DeviceException;
import functional.tests.core.Exceptions.UnknownPlatformException;
import functional.tests.core.OSUtils.Archive;
import functional.tests.core.OSUtils.FileSystem;
import functional.tests.core.Settings.Settings;
import org.openqa.selenium.logging.LogEntry;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class BaseDevice {

    private static List<String> uninstallAppsList() {
        return Arrays.asList("org.nativescript", "com.telerik");
    }

    public static void initDevice() throws UnknownPlatformException, TimeoutException, InterruptedException, DeviceException {
        if (Settings.platform == PlatformType.Andorid) {
            AndroidDevice.initDevice();
        } else if (Settings.platform == PlatformType.iOS) {
            iOSDevice.initDevice();
        }
    }

    public static void stopDevice() throws UnknownPlatformException {
        if (Settings.platform == PlatformType.Andorid) {
            AndroidDevice.stopDevice();
        } else if (Settings.platform == PlatformType.iOS) {
            iOSDevice.stopDevice();
        }
    }

    public static void initTestApp() throws IOException {

        // Uninstall apps from real devices
        if (Settings.deviceType == DeviceType.Android) {
            AndroidDevice.uninstallApps(uninstallAppsList());
        } else if (Settings.deviceType == DeviceType.iOS) {
            iOSDevice.uninstallApps(uninstallAppsList());
        } else if (Settings.deviceType == DeviceType.Simulator) {
            // Delete existing extracted applications
            FileSystem.deletePath(Settings.baseTestAppDir + File.separator + Settings.testAppName);

            // Extact test app archive
            File tarFile = new File(Settings.baseTestAppDir + File.separator + Settings.testAppArchive);
            File dest = new File(Settings.baseTestAppDir);
            Archive.extractArchive(tarFile, dest);
        }
    }

    public static void stopTestApp() {
        if (Settings.deviceType == DeviceType.Android) {
            AndroidDevice.stopApps(uninstallAppsList());
        } else if (Settings.deviceType == DeviceType.iOS) {
            iOSDevice.stopApps(uninstallAppsList());
        }
    }

    public static void pushFile(String deviceId, String localPath, String remotePath) throws Exception {
        if (Settings.platform == PlatformType.Andorid) {
            Adb.pushFile(deviceId, localPath, remotePath);
        } else {
            throw new NotImplementedException();
        }
    }

    public static void cleanConsoleLog() {
        if (Settings.platform == PlatformType.Andorid) {
            Client.driver.manage().logs().get("logcat");
        } else {
            Client.driver.manage().logs().get("syslog");
            Client.driver.manage().logs().get("crashlog");
        }
    }

    public static void getConsoleLog(String fileName) throws IOException {
        if (Settings.platform == PlatformType.Andorid) {
            List<LogEntry> logEntries = Client.driver.manage().logs().get("logcat").getAll();
            String logLocation = Settings.consoleLogDir + File.separator + "logcat_" + fileName + ".log";
            FileWriter writer = new FileWriter(logLocation);
            for (LogEntry log : logEntries) {
                writer.write(log.toString());
                writer.write(System.lineSeparator());
            }
            writer.close();
        } else {
            List<LogEntry> logEntries = Client.driver.manage().logs().get("syslog").getAll();
            String logLocation = Settings.consoleLogDir + File.separator + "syslog_" + fileName + ".log";
            FileWriter writer = new FileWriter(logLocation);
            for (LogEntry log : logEntries) {
                writer.write(log.toString());
                writer.write(System.lineSeparator());
            }
            writer.close();

            logEntries = Client.driver.manage().logs().get("crashlog").getAll();
            logLocation = Settings.consoleLogDir + File.separator + "crashlog_" + fileName + ".log";
            writer = new FileWriter(logLocation);
            for (LogEntry log : logEntries) {
                writer.write(log.toString());
                writer.write(System.lineSeparator());
            }
            writer.close();
        }
    }
}
