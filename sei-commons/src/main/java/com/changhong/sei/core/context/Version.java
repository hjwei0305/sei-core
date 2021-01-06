package com.changhong.sei.core.context;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 16:29
 */
public class Version {
    private static final Logger LOG = LoggerFactory.getLogger(Version.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String LATEST = "latest";
    private static final String UNDEFINED = "Undefined";

    private String version = LATEST;
    private String name = UNDEFINED;
    private String description;
    private String fullVersion;
    private String buildTime = UNDEFINED;

    private Version() {

    }

    public Version(Class<? extends Version> clazz) {
        if (null != clazz) {
            try {
                String className = clazz.getSimpleName() + ".class";
                String classPath = clazz.getResource(className).toString();
                if (classPath.startsWith("jar")) {
                    String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
                    LOG.debug("manifestPath={}", manifestPath);
                    readVersionFrom(manifestPath);
                } else {
                    // Class not from JAR
                    name = ApplicationContextHolder.getProperty("sei.application.code", UNDEFINED);
                    description = ApplicationContextHolder.getProperty("sei.application.description", name);
                    if (description.startsWith("@")) {
                        description = name;
                    }
                    version = ApplicationContextHolder.getProperty("sei.application.version", LATEST);
                    if (version.startsWith("@")) {
                        version = LATEST;
                    }
                    fullVersion = name + " " + version;
                    buildTime = FORMATTER.format(LocalDateTime.now());

//                String relativePath = clazz.getName().replace('.', File.separatorChar) + ".class";
//                String classFolder = classPath.substring(0, classPath.length() - relativePath.length() - 1);
//                String manifestPath = classFolder + "/META-INF/MANIFEST.MF";
//                LOG.debug("manifestPath={}", manifestPath);
//                readVersionFrom(manifestPath);
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
    public final String getName() {
        return name;
    }

    /**
     * 应用描述
     */
    public final String getDescription() {
        return description;
    }

    /**
     * 应用版本
     */
    public final String getCurrentVersion() {
        return version;
    }

    /**
     * Returns version string as normally used in print, such as SEI 6.0.1
     */
    public final String getCompleteVersionString() {
        return fullVersion;
    }

    /**
     * 构建时间
     */
    public final String getBuildTime() {
        return buildTime;
    }

    protected static Version buildDefaultVersion() {
        Version verObj = new Version();
        verObj.name = ApplicationContextHolder.getProperty("sei.application.code", UNDEFINED);
        verObj.description = ApplicationContextHolder.getProperty("sei.application.description", verObj.getName());
        if (verObj.getDescription().startsWith("@")) {
            verObj.description = verObj.getName();
        }
        verObj.version = ApplicationContextHolder.getProperty("sei.application.version", LATEST);
        if (verObj.getCurrentVersion().startsWith("@")) {
            verObj.version = LATEST;
        }
        verObj.fullVersion = verObj.getName() + " " + verObj.getCurrentVersion();
        verObj.buildTime = FORMATTER.format(LocalDateTime.now());

        return verObj;
    }

    /**
     * 通过字符解析，MANIFEST.MF 文件中的属性值
     */
    private void readVersionFrom(String manifestPath) {
        try {
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attrs = manifest.getMainAttributes();

            String appDescription = attrs.getValue("Description");
            LOG.debug("Read Description: {}", appDescription);
            if (StringUtils.isNotBlank(appDescription)) {
                description = appDescription;
            }
            String implementationTitle = attrs.getValue("Implementation-Title");
            LOG.debug("Read Implementation-Title: {}", implementationTitle);
            if (StringUtils.isNotBlank(implementationTitle)) {
                name = implementationTitle;
            }
            String implementationVersion = attrs.getValue("Implementation-Version");
            LOG.debug("Read Implementation-Version: {}", implementationVersion);
            if (StringUtils.isNotBlank(implementationVersion)) {
                version = implementationVersion;
            }
            fullVersion = name + " " + version;

            String bTime = attrs.getValue("Build-Time");
            LOG.debug("Read Build-Time: {}", bTime);
            if (StringUtils.isNotBlank(bTime)) {
                buildTime = bTime;
            }
        } catch (Exception e) {
            LOG.error("读取MANIFEST.MF文件异常", e);
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
