package com.cnhindustrial.telemetry.common.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GroovyScript implements Serializable {

    private static final long serialVersionUID = 1268867314519259065L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyScript.class);

    private final String scriptText;

    public GroovyScript(String scriptText) {
        this.scriptText = scriptText;
    }

    public String getScriptText() {
        return scriptText;
    }

    public List<Error> evaluate(Object value) {
        Binding sharedData = new Binding();
        GroovyShell shell = new GroovyShell(sharedData);
        sharedData.setProperty("value", value);
        Object evaluate = shell.evaluate(scriptText);
        LOGGER.debug("Groovy script evaluation: {}", evaluate);
        return
                Collections.emptyList();
    }
}
