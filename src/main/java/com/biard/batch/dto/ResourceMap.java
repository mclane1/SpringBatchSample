package com.biard.batch.dto;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Get a map with resource attribut.
 */
public class ResourceMap extends HashMap<String, Object> implements ResourceAware {
    private transient Resource resource;

    /**
     * <p> Get the field resource. </p>
     *
     * @return org.springframework.core.io.Resource : value of field
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * <p> Set the field resource. </p>
     *
     * @param pResource the field to set
     */
    @Override
    public void setResource(final Resource pResource) {
        resource = pResource;
    }

    /**
     * Compares the specified object with this map for equality.  Returns
     * <tt>true</tt> if the given object is also a map and the two maps
     * represent the same mappings.  More formally, two maps <tt>m1</tt> and
     * <tt>m2</tt> represent the same mappings if
     * <tt>m1.entrySet().equals(m2.entrySet())</tt>.  This ensures that the
     * <tt>equals</tt> method works properly across different implementations
     * of the <tt>Map</tt> interface.
     * <p/>
     * <p>This implementation first checks if the specified object is this map;
     * if so it returns <tt>true</tt>.  Then, it checks if the specified
     * object is a map whose size is identical to the size of this map; if
     * not, it returns <tt>false</tt>.  If so, it iterates over this map's
     * <tt>entrySet</tt> collection, and checks that the specified map
     * contains each mapping that this map contains.  If the specified map
     * fails to contain such a mapping, <tt>false</tt> is returned.  If the
     * iteration completes, <tt>true</tt> is returned.
     *
     * @param o object to be compared for equality with this map
     * @return <tt>true</tt> if the specified object is equal to this map
     */
    @Override
    public boolean equals(final Object o) {
        final boolean out;
        if (super.equals(o)) {
            if (o instanceof ResourceMap) {
                if (resource == null) {
                    out = ((ResourceMap) o).getResource() == null;
                } else {
                    out = resource.equals(((ResourceMap) o).getResource());
                }
            } else {
                out = false;
            }
        } else {
            out = false;
        }
        return out;
    }

    /**
     * Returns the hash code value for this map.  The hash code of a map is
     * defined to be the sum of the hash codes of each entry in the map's
     * <tt>entrySet()</tt> view.  This ensures that <tt>m1.equals(m2)</tt>
     * implies that <tt>m1.hashCode()==m2.hashCode()</tt> for any two maps
     * <tt>m1</tt> and <tt>m2</tt>, as required by the general contract of
     * {@link Object#hashCode}.
     * <p/>
     * <p>This implementation iterates over <tt>entrySet()</tt>, calling
     * {@link Map.Entry#hashCode hashCode()} on each element (entry) in the
     * set, and adding up the results.
     *
     * @return the hash code value for this map
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        return result;
    }
}
