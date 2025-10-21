package com.polarbookshop.quotefunction.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QuoteRepository {
    Flux<Quote> getAllQuotes();
    Mono<Quote> getRandomQuote();
    Mono<Quote> getRandomQuotesByGenre(Genre genre);
}
