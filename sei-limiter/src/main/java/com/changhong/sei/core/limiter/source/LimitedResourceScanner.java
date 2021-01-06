//package com.changhong.sei.core.limiter.source;
//
//import com.changhong.sei.core.limiter.LimiterAnnotationParser;
//import com.changhong.sei.core.limiter.resource.LimitedResource;
//import org.springframework.core.annotation.AnnotationAttributes;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.core.io.support.ResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternUtils;
//import org.springframework.core.type.MethodMetadata;
//import org.springframework.core.type.classreading.*;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.CollectionUtils;
//
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.*;
//
///**
// *
// */
//public class LimitedResourceScanner implements LimitedResourceSource {
//
//    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
//
//    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
//
//    private ResourcePatternResolver resourcePatternResolver;
//
//    private MetadataReaderFactory metadataReaderFactory;
//
//    private String basePackage;
//
//    private Collection<LimiterAnnotationParser> limiterAnnotationParsers;
//
//    Map<String, LimitedResource> limitedResourceMap = new HashMap<>();
//
//    Map<String, Collection<LimitedResource>> limitedResourceRegistry = new HashMap<>();
//
//    public LimitedResourceScanner(String basePackage, Collection<LimiterAnnotationParser> limiterAnnotationParsers, ResourceLoader resourceLoader) {
//        this.basePackage = basePackage;
//        this.limiterAnnotationParsers = limiterAnnotationParsers;
//        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
//        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
//    }
//
//    public void scanLimitedResource() {
//        try {
//            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
//                    ClassUtils.convertClassNameToResourcePath(basePackage) + '/' + this.resourcePattern;
//            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
//            for (Resource resource : resources) {
//                if (resource.isReadable()) {
//                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
//                    AnnotationMetadataReadingVisitor classVisitor = (AnnotationMetadataReadingVisitor) metadataReader.getClassMetadata();
//                    if (classVisitor.isInterface() || classVisitor.isAbstract()) {
//                        continue;
//                    }
//                    for (LimiterAnnotationParser parser : limiterAnnotationParsers) {
//                        Set<MethodMetadata> methodMetadata = classVisitor.getAnnotatedMethods(parser.getSupportAnnotation().getName());
//                        if (CollectionUtils.isEmpty(methodMetadata)) {
//                            continue;
//                        }
//                        for (MethodMetadata metadata : methodMetadata) {
//                            MethodMetadataReadingVisitor methodVisitor = (MethodMetadataReadingVisitor) metadata;
//                            AnnotationAttributes attributes = (AnnotationAttributes) methodVisitor.getAnnotationAttributes(parser.getSupportAnnotation().getName());
//                            if (attributes != null) {
//                                LimitedResource limitedResource = parser.parseLimiterAnnotation(attributes);
//                                if (limitedResource != null) {
//                                    String key = methodVisitor.getDeclaringClassName() + "#" +
//                                            methodVisitor.getMethodName() + "@"
//                                            + parser.getSupportAnnotation().getSimpleName();
//                                    limitedResourceMap.put(key, limitedResource);
//                                    // add to registry
//                                    String classMethod = methodVisitor.getDeclaringClassName()
//                                            + "#" + methodVisitor.getMethodName();
//                                    if (!limitedResourceRegistry.containsKey(classMethod)) {
//                                        List<LimitedResource> tempList = new ArrayList<>();
//                                        tempList.add(limitedResource);
//                                        limitedResourceRegistry.put(classMethod, tempList);
//                                    } else {
//                                        Collection<LimitedResource> tempList = limitedResourceRegistry.get(classMethod);
//                                        tempList.add(limitedResource);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public Collection<LimitedResource> getLimitedResource(Class<?> targetClass, Method method) {
//        String key = targetClass.getName() + "#" + method.getName();
//        return limitedResourceRegistry.get(key);
//    }
//}
