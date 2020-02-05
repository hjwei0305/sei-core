package com.changhong.sei.core.context;

import java.util.Objects;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 16:29
 */
public final class Version {
    private static String version;
    private static String name;
    private static String fullVersion;

    static {
        name = Version.class.getPackage().getImplementationTitle();
        name = Objects.isNull(name) ? "SEI" : name;
        version = Version.class.getPackage().getImplementationVersion();
        version = Objects.isNull(version) ? "dev" : version;
        fullVersion = name + " " + version;
    }

    private Version() {
    }

    public static String getCurrentVersion() {
        return version;
    }

    public static String getName() {
        return name;
    }

    /**
     * Returns version string as normally used in print, such as ECMP 3.0.4
     */
    public static String getCompleteVersionString() {
        return fullVersion;
    }

    @Override
    public String toString() {
        return fullVersion;
    }
}
