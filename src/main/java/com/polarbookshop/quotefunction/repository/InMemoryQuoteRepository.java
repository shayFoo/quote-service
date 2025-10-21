package com.polarbookshop.quotefunction.repository;

import com.polarbookshop.quotefunction.domain.Genre;
import com.polarbookshop.quotefunction.domain.Quote;
import com.polarbookshop.quotefunction.domain.QuoteRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

@Repository
public class InMemoryQuoteRepository implements QuoteRepository {
    private static final Random RANDOM = new Random();
    private static final List<Quote> quotes = List.of(
            new Quote("Not all those who wander are lost.", "J.R.R. Tolkien", Genre.ADVENTURE),
            new Quote("The only limit to our realization of tomorrow is our doubts of today.", "Franklin D. Roosevelt", Genre.FANTASY),
            new Quote("In the middle of difficulty lies opportunity.", "Albert Einstein", Genre.SCIENCE_FICTION),
            new Quote("Adventure is worthwhile in itself.", "Amelia Earhart", Genre.ADVENTURE),
            new Quote("Fantasy is a necessary ingredient in living.", "Dr. Seuss", Genre.FANTASY),
            new Quote("Science fiction is the most important literature in the history of the world.", "Ray Bradbury", Genre.SCIENCE_FICTION)
    );

    @Override
    public Flux<Quote> getAllQuotes() {
        return Flux.fromIterable(quotes);
    }

    @Override
    public Mono<Quote> getRandomQuote() {
        return Mono.just(quotes.get(RANDOM.nextInt(quotes.size())));
    }

    @Override
    public Mono<Quote> getRandomQuotesByGenre(Genre genre) {
        List<Quote> list = quotes.stream()
                .filter(q -> q.genre().equals(genre))
                .toList();
        return Mono.just(list.get(RANDOM.nextInt(list.size())));
    }
}
