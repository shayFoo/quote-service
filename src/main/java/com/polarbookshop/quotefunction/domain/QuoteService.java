package com.polarbookshop.quotefunction.domain;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Flux<Quote> getAllQuotes() {
        return quoteRepository.getAllQuotes();
    }

    public Mono<Quote> getRandomQuote() {
        return quoteRepository.getRandomQuote();
    }

    public Mono<Quote> getRandomQuoteByGenre(Genre genre) {
        return quoteRepository.getRandomQuotesByGenre(genre);
    }
}
