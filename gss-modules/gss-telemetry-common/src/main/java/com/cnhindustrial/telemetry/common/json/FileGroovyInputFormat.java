package com.cnhindustrial.telemetry.common.json;

import com.cnhindustrial.telemetry.common.groovy.GroovyScript;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.flink.api.common.io.FileInputFormat;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.core.fs.FileInputSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Read file content into String in one split.
 * For reading file formats that are considered un-splittable, like XML, JSON, etc.
 * This implementation is usable if the files are very small.
 */
public class FileGroovyInputFormat extends FileInputFormat<GroovyScript> implements ResultTypeQueryable<GroovyScript> {

    private static final long serialVersionUID = 6852770535501944201L;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileGroovyInputFormat.class);

    private boolean end;

    public FileGroovyInputFormat() {
        unsplittable = true;
    }

    @Override
    public void open(FileInputSplit split) throws IOException {
        super.open(split);

        LOGGER.debug("Opening file input split {} [{}, {}]", split.getPath(), this.splitStart, this.splitLength);
        end = false;
    }

    @Override
    public GroovyScript nextRecord(GroovyScript reuse) throws IOException {
        byte[] bytes = IOUtils.toByteArray(stream);
        end = true;

        LOGGER.debug("Number of bytes read from file input split {}: {}", this.currentSplit.getPath(), bytes.length);
        return new GroovyScript(new String(bytes));
    }

    @Override
    public boolean reachedEnd() {
        return end;
    }

    @Override
    public TypeInformation<GroovyScript> getProducedType() {
        return TypeInformation.of(new TypeHint<GroovyScript>() {});
    }
}
