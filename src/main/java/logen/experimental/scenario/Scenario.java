package logen.experimental.scenario;

import logen.experimental.scenario.common.Frequency;
import logen.experimental.scenario.common.LogSpec;
import logen.experimental.scenario.group.Group;
import logen.experimental.scenario.common.PropagationContainer;
import logen.experimental.scenario.time.TimePeriod;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Scenario {
    private static final int DEFAULT_LOG_COUNT = 200;
    private static final List<String> DEFAULT_HEADERS = Arrays.asList(
            "Time", "Description", "Type", "Subject", "Remark"
    );

    /**
     * Not required.
     */
    private int logCount;
    /**
     * Not required.
     */
    private List<String> headers;
    /**
     * Not required.
     */
    private TimePeriod timePeriod;
    /**
     * Not required.
     */
    private List<String> descriptions;
    /**
     * Not required.
     */
    private List<String> types;
    /**
     * Not required.
     */
    private List<String> subjects;
    /**
     * Not required.
     */
    private List<String> remarks;
    private Frequency frequency;
    /**
     * Not required.
     */
    private List<Group> groups;
    /**
     * Not required.
     */
    private List<LogSpec> logSpecs;

    /**
     * Sets the values of missing properties with their default values
     * and sets the values of properties where it is specified that the
     * values to be set are left to the system to choose.
     *
     * Missing properties refer to properties which are not present in
     * the scenario configuration file or properties which are present
     * in the scenario configuration file but which sub-properties are
     * not present.
     *
     * Missing properties with no default values will not be modified.
     */
    public void setAttributesIfAbsent() {
        setLogCountIfAbsent();
        setHeadersIfAbsent();
        TimePeriod.setAttributesIfAbsent(timePeriod);
        setGroupsAttributesIfAbsent();
    }

    private void setLogCountIfAbsent() {
        if (logCount == 0) {
            logCount = DEFAULT_LOG_COUNT;
        }
    }

    private void setHeadersIfAbsent() {
        if (headers == null || headers.isEmpty()) {
            headers = DEFAULT_HEADERS;
        }
    }

    private void setGroupsAttributesIfAbsent() {
        if (groups == null) {
            groups = Collections.emptyList();
        }
        groups.forEach(group -> group.setAttributesIfAbsent(timePeriod));
    }

    /**
     * Sets the values of missing property values if not specified but
     * are specified higher up in the chain
     */
    public void propagateValuesIfAbsent() {
        PropagationContainer container = toPropagationContainer();
        groups.forEach(group -> group.propagateValuesIfAbsent(container));
        logSpecs.forEach(logSpec -> logSpec.propagateValuesIfAbsent(container));
    }

    private PropagationContainer toPropagationContainer() {
        return new PropagationContainer(
                descriptions,
                types,
                subjects,
                remarks,
                frequency
        );
    }

    /**
     * Verify that all required properties are set.
     * Required properties refer to properties which values must be set.
     */
    public void verifyRequiredPropertiesPresent() {

    }

    /**
     * Verify that the values of all required properties are set correctly.
     */
    public void verifyRequiredPropertiesSetCorrectly() {

    }

    public int getLogCount() {
        return logCount;
    }

    public void setLogCount(int logCount) {
        this.logCount = logCount;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<LogSpec> getLogSpecs() {
        return logSpecs;
    }

    public void setLogSpecs(List<LogSpec> logSpecs) {
        this.logSpecs = logSpecs;
    }
}
