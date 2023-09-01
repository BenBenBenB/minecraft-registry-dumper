package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        //modEventBus.addListener(this::getRegistered);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static void grabAllBlockInfo(RegistryAccess registryAccess, String filePath)
    {
        Registry<Block> blocks = registryAccess.registry(Registries.BLOCK).get();
        Registry<BlockEntityType<?>> block_entities = registryAccess.registry(Registries.BLOCK_ENTITY_TYPE).get();

        String newLine = "\r\n";
        File myObj = new File(filePath);

        // [{namespace}.{block_id}]
        // [{block_id}.properties]
        // [{block_id}.properties.{prop_name}]
        // default = {default}
        // allowed = [{val_1, val_2 ..., val_n}]
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write("# Generated from Minecraft version 1.20.1");
            myWriter.write(newLine);

            for (var block : blocks) {
                final String blockId = blocks.getKey(block).getPath().toLowerCase();
                final String namespace = blocks.getKey(block).getNamespace().toLowerCase();
                final var statesProps = block.getStateDefinition().getProperties();


                final var statesDef = block.getStateDefinition();
                final var default_block = block.defaultBlockState();


                // [{block_id}]
                myWriter.write(format("[%s.%s]", namespace, blockId));
                myWriter.write(newLine);
                myWriter.write(format("piston_behavior=\"%s\"",  default_block.getPistonPushReaction()));
                myWriter.write(newLine);
                for (var prop : statesProps){
                    // [{block_id}.properties.{prop_name}]
                    final String propName = prop.getName().toLowerCase();
                    myWriter.write(format("[%s.%s.properties.%s]", namespace, blockId, propName));
                    myWriter.write(newLine);

                    // default = {default}
                    final var defaultValue = default_block.getValue(prop);
                    myWriter.write(format("default = \"%s\"", defaultValue.toString().toLowerCase()));
                    myWriter.write(newLine);

                    // allowed = [{val_1, val_2 ..., val_n}]
                    final var allowed = prop.getPossibleValues()
                            .stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining("\", \""));
                    myWriter.write(format("allowed = [\"%s\"]", allowed.toLowerCase()));
                    myWriter.write(newLine);
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote blocks to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void grabAllItemInfo(RegistryAccess registryAccess, String filePath)
    {
        Registry<Item> items = registryAccess.registry(Registries.ITEM).get();

        String newLine = "\r\n";
        File myObj = new File(filePath);

        // [{namespace}.{block_id}]
        // [{block_id}.properties]
        // [{block_id}.properties.{prop_name}]
        // default = {default}
        // allowed = [{val_1, val_2 ..., val_n}]
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write("# Generated from Minecraft version 1.20.1");
            myWriter.write(newLine);

            for (var item : items) {
                final String itemId = Objects.requireNonNull(items.getKey(item)).getPath().toLowerCase();
                final String namespace = Objects.requireNonNull(items.getKey(item)).getNamespace().toLowerCase();
                final ItemStack defaultItem = item.getDefaultInstance();
                // [{block_id}]
                myWriter.write(format("[%s.%s]", namespace, itemId));
                myWriter.write(newLine);
                myWriter.write(format("max_stack_size=%s", defaultItem.getMaxStackSize()));
                myWriter.write(newLine);
                myWriter.write(format("max_damage=%s", defaultItem.getMaxDamage()));
                myWriter.write(newLine);
                myWriter.write(format("is_fire_resistant=%s", item.isFireResistant()));
                myWriter.write(newLine);
                // stack size
            }
            myWriter.close();
            System.out.println("Successfully wrote items to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void grabAllEntityInfo(RegistryAccess registryAccess, String filePath)
    {
        Registry<EntityType<?>> entities = registryAccess.registry(Registries.ENTITY_TYPE).get();

        String newLine = "\r\n";
        File myObj = new File(filePath);

        // [{namespace}.{block_id}]
        // [{block_id}.properties]
        // [{block_id}.properties.{prop_name}]
        // default = {default}
        // allowed = [{val_1, val_2 ..., val_n}]
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write("# Generated from Minecraft version 1.20.1");
            myWriter.write(newLine);

            for (var entity : entities) {
                final String entityId = Objects.requireNonNull(entities.getKey(entity)).getPath().toLowerCase();
                final String namespace = Objects.requireNonNull(entities.getKey(entity)).getNamespace().toLowerCase();

                // [{block_id}]
                myWriter.write(format("[%s.%s]", namespace, entityId));
                myWriter.write(newLine);
                myWriter.write(format("fire_immune=%s", entity.fireImmune()));
                myWriter.write(newLine);
                myWriter.write(format("height=%s", entity.getHeight()));
                myWriter.write(newLine);
                myWriter.write(format("width=%s", entity.getWidth()));
                myWriter.write(newLine);
                var category = entity.getCategory();
                myWriter.write(format("category=\"%s\"", category));
                myWriter.write(newLine);
                // for (var prop : statesProps){
                // [{block_id}.properties.{prop_name}]
                // final String propName = prop.getName().toLowerCase();
                //     myWriter.write(format("[%s.%s.properties.%s]", namespace, blockId, propName));
                // }
            }
            myWriter.close();
            System.out.println("Successfully wrote items to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void grabAllEnchantmentInfo(RegistryAccess registryAccess, String filePath)
    {
        Registry<Enchantment> enchantment_reg = registryAccess.registry(Registries.ENCHANTMENT).get();

        String newLine = "\r\n";
        File myObj = new File(filePath);

        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write("# Generated from Minecraft version 1.20.1");
            myWriter.write(newLine);

            for (var enchantment : enchantment_reg) {
                final String enchantId = Objects.requireNonNull(enchantment_reg.getKey(enchantment)).getPath().toLowerCase();
                final String namespace = Objects.requireNonNull(enchantment_reg.getKey(enchantment)).getNamespace().toLowerCase();
                myWriter.write(format("[%s.%s]", namespace, enchantId));
                myWriter.write(newLine);
                myWriter.write(format("category = \"%s\"", enchantment.category));
                myWriter.write(newLine);
                myWriter.write(format("level = %s}", enchantment.getMaxLevel()));
                myWriter.write(newLine);
                myWriter.write(format("rarity = \"%s\"", enchantment.getRarity()));
                myWriter.write(newLine);
                myWriter.write(format("curse = %s}", enchantment.isCurse()));
                myWriter.write(newLine);
            }
            myWriter.close();
            System.out.println("Successfully wrote items to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void getRegistered() {
        String outPutDir = "C:\\src\\output\\";
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        assert level != null;
        RegistryAccess registryAccess =level.registryAccess();
        grabAllBlockInfo(registryAccess, format("%s%s", outPutDir, "block.toml"));
        grabAllItemInfo(registryAccess, format("%s%s", outPutDir, "item.toml"));
        grabAllEntityInfo(registryAccess, format("%s%s", outPutDir, "entity.toml"));
        grabAllEnchantmentInfo(registryAccess, format("%s%s", outPutDir, "enchantment.toml"));
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        // Do something when the server starts
        getRegistered();
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

}
