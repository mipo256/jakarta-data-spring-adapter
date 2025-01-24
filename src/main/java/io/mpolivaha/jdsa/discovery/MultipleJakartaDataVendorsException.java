package io.mpolivaha.jdsa.discovery;

import io.mpolivaha.jdsa.JakartaDataVendor;

import java.util.Set;

/**
 *
 *
 * @author Mikhail Polivakha
 */
public class MultipleJakartaDataVendorsException extends RuntimeException {

    public MultipleJakartaDataVendorsException(Set<JakartaDataVendor> vendors) {
        super(
                "Expected a single Jakarta Data Implementation to be found in classpath, but found : %s".formatted(vendors)
        );
    }
}
