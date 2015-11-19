package com.biard.batch.mapper;

import com.biard.batch.dto.ResourceMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.springframework.batch.item.file.LineMapper;

/**
 * {@code org.springframework.batch.item.file.mapping.JSonLineMapper} without Map<String, Object>.
 * ResourceMap is an extends of HashMap<String, Object> with resource to have the resource of file.
 */
public class JSonResourceLineMapper implements LineMapper<ResourceMap> {
    private final MappingJsonFactory factory = new MappingJsonFactory();

    /**
     * Constructor empty by default
     */
    public JSonResourceLineMapper() {
        // Constructor empty by default
    }

    /**
     * Interpret the line as a Json object and create a Map from it.
     *
     * @see LineMapper#mapLine(String, int)
     */
    @Override
    public ResourceMap mapLine(String line, int lineNumber) throws Exception {
        final ResourceMap result;
        final JsonParser parser = factory.createJsonParser(line);
        @SuppressWarnings("unchecked")
        final ResourceMap token = parser.readValueAs(ResourceMap.class);
        result = token;
        return result;
    }
}
