package io.mpolivaha.jdsa.discovery;

import io.mpolivaha.jdsa.core.Configuration;
import io.mpolivaha.jdsa.JakartaDataVendor;

import java.util.HashSet;
import java.util.Set;

/**
 * The {@link JakartaDataVendorDiscoverer} that is discovering the Jakarta Data vendor by exploring the
 * available classes in the ClassPath
 *
 * @author Mikhail Polivakha
 */
public class ArtifactsBasedJakartaDataVendorDiscoverer implements JakartaDataVendorDiscoverer {

    @Override
    public JakartaDataVendor discover() {

        JakartaDataVendor jakartaDataVendor = Configuration.getInstance().jakartaDataVendor();

        if (jakartaDataVendor != null) {
            if (jakartaDataVendor.isPresent()) {
                return jakartaDataVendor;
            } else {
                throw new MissingRequiredJakartaDataVendorException(jakartaDataVendor);
            }
        } else {
            Set<JakartaDataVendor> candidates = new HashSet<>();
            for (JakartaDataVendor value : JakartaDataVendor.values()) {
                if (value.isPresent()) {
                    candidates.add(value);
                }
            }

            if (candidates.isEmpty()) {
                throw new UnknownJakartaDataVendorException();
            }

            if (candidates.size() > 1) {
                throw new MultipleJakartaDataVendorsException(candidates);
            }

            return candidates.stream().findFirst().get();
        }
    }
}
