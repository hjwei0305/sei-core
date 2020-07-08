package com.changhong.sei.core.limiter;

import java.util.Map;

public interface ArgumentInjector {

    Map<String, Object> inject(Object... args);
}
