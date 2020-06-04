package logen.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import logen.scenario.common.Frequency;
import logen.scenario.common.LogSpec;
import logen.scenario.group.Group;
import logen.scenario.common.PropagationContainer;
import logen.scenario.time.TimePeriod;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Scenario {
    private static final int DEFAULT_LOG_COUNT = 200;
    private static final List<String> DEFAULT_HEADERS = Arrays.asList(
            "Time", "Description", "Type", "Subject", "Remark"
    );

    /**
     * Not required.
     */
    @JsonProperty("minLogCount")
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
    /**
     * Not required.
     */
    private Frequency frequency;
    /**
     * Not required.
     */
    private List<Group> groups;
    /**
     * Not required.
     */
    private List<LogSpec> logSpecs;

    public void completeConfiguration() {
        setAttributesIfAbsent();
        propagateValuesIfAbsent();
    }

    /**
     * Sets the values of missing attributes with their default values
     * and sets the values of attributes where it is specified that the
     * values to be set are left to the system to choose.
     *
     * An attribute is missing if it is not present in the scenario
     * configuration file, either as an attribute in its entirety or as
     * part of another attribute.
     *
     * Missing attributes with no default values will not be modified.
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
        Optional.of(groups).ifPresent(gs -> gs.forEach(g -> g.setAttributesIfAbsent(timePeriod)));
    }

    /**
     * Sets the values of missing attributes values if not specified but
     * are specified in higher levels.
     *
     * An level is higher than another if it has a wider scope of effect.
     * As such, the levels of attributes are: global > group > local.
     */
    public void propagateValuesIfAbsent() {
        PropagationContainer container = toPropagationContainer();
        Optional.of(groups).ifPresent(gs -> gs.forEach(g -> g.propagateValuesIfAbsent(container)));
        Optional.of(logSpecs).ifPresent(ls -> ls.forEach(l -> l.propagateValuesIfAbsent(container)));
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
    public boolean verifyIfRequiredAttributesPresent() {
        boolean arePresent = true;
        if (groups != null) {
            arePresent &= groups.stream().map(Group::verifyIfRequiredAttributesSet).reduce(true, Boolean::logicalAnd);
        }
        if (logSpecs != null) {
            arePresent &= logSpecs.stream().map(LogSpec::verifyIfRequiredAttributesSet).reduce(true, Boolean::logicalAnd);
        }
        return arePresent;
    }

    /**
     * Verify that the values of all required properties are set correctly.
     */
    public void verifyIfRequiredAttributesCorrect() {

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
