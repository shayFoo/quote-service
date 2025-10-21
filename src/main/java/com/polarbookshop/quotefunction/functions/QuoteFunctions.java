package com.polarbookshop.quotefunction.functions;

import com.polarbookshop.quotefunction.domain.Genre;
import com.polarbookshop.quotefunction.domain.Quote;
import com.polarbookshop.quotefunction.domain.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class QuoteFunctions {
    private static final Logger log = LoggerFactory.getLogger(QuoteFunctions.class);

    @Bean
    Supplier<Flux<Quote>> allQuotes(QuoteService quoteService) {
        return () -> {
            log.info("Fetching the list of quotes");
            return quoteService.getAllQuotes()
                    .delaySequence(Duration.ofSeconds(1));
        };
    }

    @Bean
    Supplier<Mono<Quote>> randomQuote(QuoteService quoteService) {
        return () -> {
            log.info("Fetching a random quote");
            return quoteService.getRandomQuote();
        };
    }

    @Bean
    Function<Genre, Mono<Quote>> genreQuote(QuoteService quoteService) {
        return genre -> {
            log.info("Fetching a random quote for genre: {}", genre);
            return quoteService.getRandomQuoteByGenre(genre);
        };
    }

    @Bean
    Consumer<Mono<Quote>> logQuote() {
        return quote ->
                quote.subscribe(q -> log.info("Received quote: {} by {}", q.content(), q.author()));

    }
}
