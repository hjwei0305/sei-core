package com.changhong.sei.core.context;

import java.util.Objects;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 16:29
 */
public abstract class Version {
    public static final String UNDEFINED = "Undefined";
    private String version = UNDEFINED;
    private String name = UNDEFINED;
    private String fullVersion = UNDEFINED;

    public Version(String packageName) {
        Package pkg = Package.getPackage(packageName);
        if (Objects.nonNull(pkg)) {
            name = pkg.getImplementationTitle();
            name = Objects.isNull(name) ? UNDEFINED : name;
            version = pkg.getImplementationVersion();
            version = Objects.isNull(version) ? UNDEFINED : version;
            fullVersion = name + " " + version;
        }
    }

    public final String getCurrentVersion() {
        return version;
    }

    public final String getName() {
        return name;
    }

    /**
     * Returns version string as normally used in print, such as ECMP 3.0.4
     */
    public final String getCompleteVersionString() {
        return fullVersion;
    }

    @Override
    public String toString() {
        return fullVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Version version1 = (Version) o;

        if (!Objects.equals(version, version1.version)) {
            return false;
        }
        return Objects.equals(name, version1.name);
    }

    @Override
    public int hashCode() {
        int result = version != null ? version.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
