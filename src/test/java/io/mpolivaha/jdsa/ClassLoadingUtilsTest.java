package io.mpolivaha.jdsa;

import io.mpolivaha.jdsa.core.Configuration;
import jakarta.data.repository.Repository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ClassLoadingUtils}
 *
 * @author Mikhail Polivakha
 */
class ClassLoadingUtilsTest {

    @Test
    void test_isPresent() {
        Assertions.assertThat(ClassLoadingUtils.isPresent("io.vavr.control.Try")).isFalse();
        Assertions.assertThat(ClassLoadingUtils.isPresent(Configuration.class.getName())).isTrue();
        Assertions.assertThat(ClassLoadingUtils.isPresent(Repository.class.getName())).isTrue();
    }

    @Test
    void testTryLoading() {
        Assertions.assertThat(ClassLoadingUtils.tryLoading("io.vavr.control.Try")).isNull();
        Assertions.assertThat(ClassLoadingUtils.tryLoading(Configuration.class.getName())).isSameAs(Configuration.class);
        Assertions.assertThat(ClassLoadingUtils.tryLoading(Repository.class.getName())).isSameAs(Repository.class);
    }
}