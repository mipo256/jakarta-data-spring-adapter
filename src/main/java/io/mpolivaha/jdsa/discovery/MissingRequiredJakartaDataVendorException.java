package io.mpolivaha.jdsa.discovery;

import io.mpolivaha.jdsa.JakartaDataVendor;

/**
 *
 * @author Mikhail Polivakha
 */
public class MissingRequiredJakartaDataVendorException extends RuntimeException {

    public MissingRequiredJakartaDataVendorException(JakartaDataVendor required) {
        super(
                "In @EnableJakartaDataRepositories requested the Jakarta Data vendor '%s'. This vendor does not seem to present in the class-path"
                        .formatted(required.name())
        );
    }
}
