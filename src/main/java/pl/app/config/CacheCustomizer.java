package pl.app.config;


import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    public final static String HTML_READ_PAGE_CACHE_NAME = "htmlReadPageCache";

    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(Arrays.asList(
                HTML_READ_PAGE_CACHE_NAME
        ));
    }
}