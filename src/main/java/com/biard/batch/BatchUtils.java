package com.biard.batch;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

/**
 * Utils for batch.
 */
public final class BatchUtils {
    private BatchUtils() {
        // do nothing
    }

    public static final String getAbsoluthPath4SpringBatch(final File file) {
        return FilenameUtils.separatorsToUnix(file.getAbsolutePath());
    }
}
