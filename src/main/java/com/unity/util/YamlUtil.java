package com.unity.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import org.apache.commons.lang3.StringUtils;

public class YamlUtil {

    /**
     * Same instance of {@link Remark} class reused for better performance according to authors recommendations.
     * <p>
     * It wrapped in ThreadLocal because of its non-thread safe nature
     */
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory()
        .disable(Feature.WRITE_DOC_START_MARKER)
        .disable(Feature.SPLIT_LINES)
    )
        .setSerializationInclusion(Include.NON_NULL)
        .setSerializationInclusion(Include.NON_EMPTY);

    public static String objectToYamlString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException("Could not serialize object to yaml string", jpe);
        }
    }
}
