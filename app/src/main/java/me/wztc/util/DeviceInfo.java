package me.wztc.util;

import me.wztc.api.Environment;

import android.location.Location;

public class DeviceInfo {

    private static Environment dev;
    private static Environment html;
    private static String version;
    private static String deviceId;
    private static Location location;
    private static String ip;

    public static void setDevEnvironment(String schema, String host, int port, String path) {
        dev = new Environment(schema, host, port, path, null, null);
    }

    public static Environment getDev() {
        return dev;
    }

    public static void setHtmlEnvironment(String schema, String host, int port, String path) {
        html = new Environment(schema, host, port, path, null, null);
    }

    public static Environment getHtml() {
        return html;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        DeviceInfo.version = version;
    }

    public static String getDeviceId() {
        return deviceId;
    }

    public static void setDeviceId(String deviceId) {
        DeviceInfo.deviceId = deviceId;
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        DeviceInfo.location = location;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        DeviceInfo.ip = ip;
    }
}
