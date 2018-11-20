package com.loosebazooka.jib.dockerfile.docker;

import java.util.Arrays;
import java.util.List;

public class Entrypoint {

  private final List<String> entrypoint;

  public static Entrypoint parse(String entrypoint) {
    List<String> parts = Arrays.asList(entrypoint.split("\\s+"));
    if (parts.size() == 1) {
      throw new RuntimeException("Cannot parse ENTRYPOINT statement: " + parts);
    }
    if (!parts.get(0).equals("ENTRYPOINT")) {
      throw new RuntimeException("Cannot parse ENTRYPOINT statement: " + parts);
    }
    return new Entrypoint(parts.subList(1, parts.size()));
  }

  private Entrypoint(List<String> entrypoint) {
    this.entrypoint = entrypoint;
  }

  public List<String> getEntrypoint() {
    return entrypoint;
  }
}
