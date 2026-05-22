package io.github.makaseloli.ae2randomsorting.mdk.config;

interface ConfigElement {
    void bindTo(ConfigVisitor visitor);
}
