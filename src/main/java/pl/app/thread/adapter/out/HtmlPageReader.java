package pl.app.thread.adapter.out;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.app.config.CacheCustomizer;
import pl.app.thread.application.port.out.ReadPage;

import java.io.IOException;
import java.util.Optional;

@Service
class HtmlPageReader implements ReadPage {
    private static final Logger logger = LoggerFactory.getLogger(HtmlPageReader.class);

    @Override
    @Cacheable(value = CacheCustomizer.HTML_READ_PAGE_CACHE_NAME, unless = "#result == T(java.util.Optional).empty()")
    public Optional<Document> readPage(String url) {
        try {
            logger.debug("Reading page form url: " + url);
            return Optional.of(Jsoup.connect(url).ignoreHttpErrors(true).get());
        } catch (IOException exception) {
            logger.error("Failed to read page form url: " + url);
            return Optional.empty();
        }
    }

    @Caching(evict = {
            @CacheEvict(value = CacheCustomizer.HTML_READ_PAGE_CACHE_NAME, allEntries = true)
    })
    @Scheduled(fixedRateString = "90000") // 5*60*1000
    public void emptyCache() {
        logger.info("emptying cache");
    }
}
