package io.mpolivaha.jdsa.discovery;

import io.mpolivaha.jdsa.JakartaDataVendor;

/**
 * Implementations are capable to discover the specific Jakarta Data providers to be used
 *
 * @author Mikhail Polivakha
 */
public interface JakartaDataVendorDiscoverer {

    JakartaDataVendor discover();
}
