package com.unity.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import org.junit.Test;

public class OptionsFileUtilTest {

    private final String PARAMS_DIR = "src/test/resources/test-doclet-params.txt";

    @Test
    public void processOptionsFile() {
        String[] strings = OptionsFileUtil.processOptionsFile(PARAMS_DIR);

        assertThat("Wrong result", Arrays.asList(strings), hasItems(
            "-doclet", "com.unity.doclet.DocFxDoclet",
            "-sourcepath", "./src/test/java",
            "-outputpath", "./target/test-out",
            "-encoding", "UTF-8",
            "-excludepackages",
            "com\\.unity\\.samples\\.someexcludedpack.*:com\\.unity\\.samples\\.someunexistingpackage",
            "-excludeclasses",
            "com\\.unity\\.samples\\.subpackage\\.SomeExcluded.*:com\\.unity\\.samples\\.subpackage\\.SomeUnexistingClass",
            "-subpackages", "com.unity.samples"));
    }
}
