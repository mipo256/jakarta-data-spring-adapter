package io.mpolivaha.jdsa.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit tests for {@link Lazy}.
 *
 * @author Mikhail Polivakha
 */
class LazyTest {

    @Test
    void testEndToEnd() {
        AtomicInteger invocationCounter = new AtomicInteger(0);

        String value = "string";

        Lazy<String> lazy = Lazy.of(() -> {
            invocationCounter.incrementAndGet();
            return value;
        });

        String first = lazy.get();
        String second = lazy.get();

        Assertions.assertThat(first).isSameAs(second).isEqualTo(value);
        Assertions.assertThat(invocationCounter.get()).isEqualTo(1);
    }
}