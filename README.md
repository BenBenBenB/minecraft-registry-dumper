# minecraft-registry-dumper
Minecraft mod that gets registered blocks, items, entities, etc. from a running instance of minecraft.

Currently, there is just barely enough here to generate the toml files that I wanted for [minecraft-object-utils](https://github.com/BenBenBenB/minecraft-object-utils).

Steps:
1. Open project by choosing build.gradle in IntelliJ IDEA.
1. Run Configurations: runClient
1. Open minecraft world. Files will be saved to hardcoded path: `C:\src\output\`
