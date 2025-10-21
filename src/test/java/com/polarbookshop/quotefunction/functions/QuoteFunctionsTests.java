package com.polarbookshop.quotefunction.functions;

import com.polarbookshop.quotefunction.domain.Genre;
import com.polarbookshop.quotefunction.domain.Quote;
import com.polarbookshop.quotefunction.domain.QuoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteFunctionsTests {

    @Mock
    private QuoteService quoteService;

    private final QuoteFunctions functions = new QuoteFunctions();

    @Test
    void allQuotes_isDelayedByOneSecond_andEmitsAllQuotes() {
        Quote q1 = new Quote("content-1", "author-1", Genre.ADVENTURE);
        Quote q2 = new Quote("content-2", "author-2", Genre.FANTASY);

        when(quoteService.getAllQuotes()).thenReturn(Flux.just(q1, q2));

        Supplier<Flux<Quote>> supplier = functions.allQuotes(quoteService);

        StepVerifier.withVirtualTime(supplier::get)
                .thenAwait(Duration.ofSeconds(1))
                .expectNext(q1)
                .expectNext(q2)
                .expectComplete()
                .verify();
    }

    @Test
    void randomQuote_returnsMonoWithQuote() {
        Quote q = new Quote("rand-content", "rand-author", Genre.SCIENCE_FICTION);
        when(quoteService.getRandomQuote()).thenReturn(Mono.just(q));

        Supplier<Mono<Quote>> supplier = functions.randomQuote(quoteService);

        StepVerifier.create(supplier.get())
                .expectNext(q)
                .expectComplete()
                .verify();
    }

    @Test
    void randomQuoteByGenre_returnsMonoWithGenreFilteredQuote() {
        Quote q = new Quote("genre-content", "genre-author", Genre.FANTASY);
        when(quoteService.getRandomQuoteByGenre(Genre.FANTASY)).thenReturn(Mono.just(q));

        Function<Genre, Mono<Quote>> fn = functions.randomQuoteByGenre(quoteService);

        StepVerifier.create(fn.apply(Genre.FANTASY))
                .expectNext(q)
                .expectComplete()
                .verify();
    }

    @Test
    void logQuote_subscribesToProvidedMono() {
        AtomicBoolean subscribed = new AtomicBoolean(false);
        Quote q = new Quote("log-content", "log-author", Genre.ADVENTURE);

        // Mono that sets a flag when subscribed
        Mono<Quote> mono = Mono.fromSupplier(() -> {
            subscribed.set(true);
            return q;
        });

        Consumer<Mono<Quote>> consumer = functions.logQuote();

        consumer.accept(mono);

        assertTrue(subscribed.get(), "Consumer should subscribe to the provided Mono");
    }
}

