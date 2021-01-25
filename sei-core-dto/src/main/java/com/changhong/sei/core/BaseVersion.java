package com.changhong.sei.core;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-22 16:06
 */
public abstract class BaseVersion implements Version {
    private String version = LATEST;
    private String name = UNDEFINED;
    private String description;
    private String fullVersion;
    private String buildTime = UNDEFINED;

    public BaseVersion(Class<? extends BaseVersion> clazz) {
        if (null != clazz) {
            try {
                String className = clazz.getSimpleName() + ".class";
                String classPath = clazz.getResource(className).toString();
                if (classPath.startsWith("jar")) {
                    String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
                    readVersionFrom(manifestPath);
//                } else {
                    // Class not from JAR
//                    String relativePath = clazz.getName().replace('.', File.separatorChar) + ".class";
//                    String classFolder = classPath.substring(0, classPath.length() - relativePath.length() - 1);
//                    String manifestPath = classFolder + "/META-INF/MANIFEST.MF";
//                    readVersionFrom(manifestPath);
                }
            } catch (Exception e) {
                Package pkg = clazz.getPackage();
                if (Objects.nonNull(pkg)) {
                    name = pkg.getImplementationTitle();
                    name = Objects.isNull(name) ? UNDEFINED : name;
                    description = name;
                    version = pkg.getImplementationVersion();
                    version = Objects.isNull(version) ? UNDEFINED : version;
                    fullVersion = name + " " + version;
                    buildTime = FORMATTER.format(LocalDateTime.now());
                }
            }
        }
    }

    /**
     * 应用代码
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * 应用描述
     */
    @Override
    public final String getDescription() {
        return description;
    }

    /**
     * 应用版本
     */
    @Override
    public final String getCurrentVersion() {
        return version;
    }

    /**
     * Returns version string as normally used in print, such as SEI 6.0.1
     */
    @Override
    public final String getCompleteVersionString() {
        return fullVersion;
    }

    /**
     * 构建时间
     */
    @Override
    public final String getBuildTime() {
        return buildTime;
    }

    /**
     * 通过字符解析，MANIFEST.MF 文件中的属性值
     */
    private void readVersionFrom(String manifestPath) {
        try {
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attrs = manifest.getMainAttributes();

            String appDescription = attrs.getValue("Description");
//            LOG.debug("Read Description: {}", appDescription);
            if (StringUtils.isNotBlank(appDescription)) {
                description = appDescription;
            }
            String implementationTitle = attrs.getValue("Implementation-Title");
//            LOG.debug("Read Implementation-Title: {}", implementationTitle);
            if (StringUtils.isNotBlank(implementationTitle)) {
                name = implementationTitle;
            }
            String implementationVersion = attrs.getValue("Implementation-Version");
//            LOG.debug("Read Implementation-Version: {}", implementationVersion);
            if (StringUtils.isNotBlank(implementationVersion)) {
                version = implementationVersion;
            }
            fullVersion = name + " " + version;

            String bTime = attrs.getValue("Build-Time");
//            LOG.debug("Read Build-Time: {}", bTime);
            if (StringUtils.isNotBlank(bTime)) {
                buildTime = bTime;
            }
        } catch (Exception e) {
            System.err.println("读取MANIFEST.MF文件异常: " + e.getMessage());
        }
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

        BaseVersion version1 = (BaseVersion) o;

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
