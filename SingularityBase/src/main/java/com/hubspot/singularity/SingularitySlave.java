package com.hubspot.singularity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.mesos.json.MesosResourcesObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import java.util.Optional;

@Schema(description = "Singularity's view of a Mesos agent")
public class SingularitySlave extends SingularityMachineAbstraction<SingularitySlave> {
  private final String host;
  private final String rackId;
  private final Map<String, String> attributes;
  private final Optional<MesosResourcesObject> resources;

  public SingularitySlave(
    String slaveId,
    String host,
    String rackId,
    Map<String, String> attributes,
    Optional<MesosResourcesObject> resources
  ) {
    super(slaveId);
    this.host = host;
    this.rackId = rackId;
    this.attributes = attributes;
    this.resources = resources;
  }

  @JsonCreator
  public SingularitySlave(
    @JsonProperty("slaveId") String slaveId,
    @JsonProperty("firstSeenAt") long firstSeenAt,
    @JsonProperty("currentState") SingularityMachineStateHistoryUpdate currentState,
    @JsonProperty("host") String host,
    @JsonProperty("rackId") String rackId,
    @JsonProperty("attributes") Map<String, String> attributes,
    @JsonProperty("resources") Optional<MesosResourcesObject> resources
  ) {
    super(slaveId, firstSeenAt, currentState);
    this.host = host;
    this.rackId = rackId;
    this.attributes = attributes;
    this.resources = resources;
  }

  @Override
  public SingularitySlave changeState(SingularityMachineStateHistoryUpdate newState) {
    return new SingularitySlave(
      getId(),
      getFirstSeenAt(),
      newState,
      host,
      rackId,
      attributes,
      resources
    );
  }

  @Schema(description = "agent hostname")
  public String getHost() {
    return host;
  }

  @JsonIgnore
  @Override
  public String getName() {
    return String.format("%s (%s)", getHost(), getId());
  }

  @JsonIgnore
  @Override
  public String getTypeName() {
    return "Slave";
  }

  @JsonIgnore
  public SingularitySlave withResources(MesosResourcesObject resources) {
    return new SingularitySlave(
      getId(),
      getFirstSeenAt(),
      getCurrentState(),
      host,
      rackId,
      attributes,
      Optional.of(resources)
    );
  }

  @Schema(description = "agent rack ID")
  public String getRackId() {
    return rackId;
  }

  @Schema(description = "Mesos attributes associated with this agent")
  public Map<String, String> getAttributes() {
    return attributes;
  }

  @Schema(description = "Resources available to allocate on this agent")
  public Optional<MesosResourcesObject> getResources() {
    return resources;
  }

  @Override
  public String toString() {
    return (
      "SingularitySlave{" +
      "host='" +
      host +
      '\'' +
      ", rackId='" +
      rackId +
      '\'' +
      ", attributes=" +
      attributes +
      ", resources=" +
      resources +
      "} " +
      super.toString()
    );
  }
}
