package com.loosebazooka.jib.dockerfile.docker;

import java.util.Arrays;
import java.util.List;

public class Copy {

  private final List<String> source;
  private final String destination;

  public static Copy parse(String copy) {
    List<String> parts = Arrays.asList(copy.split("\\s+"));
    if (parts.size() < 3) {
      throw new RuntimeException("Cannot parse COPY statement: " + parts);
    }
    if (!parts.get(0).equals("COPY")) {
      throw new RuntimeException("Cannot parse COPY statement: " + parts);
    }
    List<String> source = parts.subList(1, parts.size() - 1);
    String destination = parts.get(parts.size() - 1);
    if (source.size() > 1 && !destination.endsWith("/")) {
      throw new RuntimeException("Target of COPY must end in '/' when copying mutiple files: " + parts);
    }
    return new Copy(source, destination);
  }

  private Copy(List<String> source, String destination) {
    this.source = source;
    this.destination = destination;
  }

  public List<String> getSource() {
    return source;
  }

  public String getDestination() {
    return destination;
  }
}
