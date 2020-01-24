package com.cnhindustrial.telemetry.common.groovy;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GroovyScript implements Serializable {

    private static final long serialVersionUID = 1268867314519259065L;

    private final String script;

    public GroovyScript(String script) {
        this.script = script;
    }

    public List<Error> evaluate(Object value) {
        Binding sharedData = new Binding()
        GroovyShell shell = new GroovyShell(sharedData)
        def now = new Date()
        sharedData.setProperty('text', 'I am shared data!')
        return
                Collections.emptyList();
    }
}
