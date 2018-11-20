package com.loosebazooka.jib.dockerfile.docker;

import java.util.Arrays;
import java.util.List;

public class From {

  private final String image;

  public static From parse(String from) {
    List<String> parts = Arrays.asList(from.split("\\s+"));
    if (parts.size() != 2) {
      throw new RuntimeException("Cannot parse FROM statement: " + parts);
    }
    if (!parts.get(0).equals("FROM")) {
      throw new RuntimeException("Cannot parse COPY statement: " + parts);
    }
    return new From(parts.get(1));
  }

  private From(String image) {
    this.image = image;
  }

  public String getImage() {
    return image;
  }
}
