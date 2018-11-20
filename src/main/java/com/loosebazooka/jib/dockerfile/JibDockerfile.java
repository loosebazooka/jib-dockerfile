package com.loosebazooka.jib.dockerfile;

import com.google.cloud.tools.jib.api.Containerizer;
import com.google.cloud.tools.jib.api.DockerDaemonImage;
import com.google.cloud.tools.jib.api.Jib;
import com.google.cloud.tools.jib.api.JibContainerBuilder;
import com.google.cloud.tools.jib.configuration.CacheDirectoryCreationException;
import com.google.cloud.tools.jib.event.EventHandlers;
import com.google.cloud.tools.jib.filesystem.AbsoluteUnixPath;
import com.google.cloud.tools.jib.image.InvalidImageReferenceException;
import com.loosebazooka.jib.dockerfile.docker.Copy;
import com.loosebazooka.jib.dockerfile.docker.Entrypoint;
import com.loosebazooka.jib.dockerfile.docker.From;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JibDockerfile {
  public static void main(String[]args)
      throws IOException, InvalidImageReferenceException, InterruptedException, ExecutionException, CacheDirectoryCreationException {
    List<String> commands = Files.lines(Paths.get("Dockerfile"))
        .map(String::trim)
        .filter(((Predicate<String>) String::isEmpty).negate())
        .collect(Collectors.toList());

    if (commands.size() < 3) {
      throw new RuntimeException("JibDockerfile must contain at least one FROM, COPY and ENTRYPOINT");
    }

    From from = From.parse(commands.get(0));
    List<Copy> copys = commands.subList(1, commands.size() - 1).stream().map(Copy::parse).collect(Collectors.toList());
    Entrypoint entrypoint = Entrypoint.parse(commands.get(commands.size() - 1));

    JibContainerBuilder jcb = Jib.from(from.getImage());
    for (Copy copy : copys) {
      jcb.addLayer(copy.getSource().stream().map(Paths::get).collect(Collectors.toList()),
          AbsoluteUnixPath.get(copy.getDestination()));
    }

    jcb.setEntrypoint(entrypoint.getEntrypoint());
    ExecutorService customService = Executors.newCachedThreadPool();
    jcb.containerize(Containerizer
        .to(DockerDaemonImage.named("potato"))
        .setExecutorService(customService)
    );
    customService.shutdownNow();
  }
}
