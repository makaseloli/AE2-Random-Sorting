package io.github.makaseloli.ae2randomsorting.config;

import io.github.makaseloli.ae2randomsorting.mdk.config.ConfigDeclaration;
import io.github.makaseloli.ae2randomsorting.mdk.config.ConfigSide;

import java.util.List;

public final class ModConfigs {
    public static final ConfigDeclaration SERVER = ConfigDeclaration.of(ConfigSide.SERVER, ServerConfig.ENTRIES);

    public static final List<ConfigDeclaration> ALL = List.of(SERVER);

    private ModConfigs() {}
}
