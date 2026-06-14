package net.charonus2012.orrery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.charonus2012.orrery.planet.PlanetData;
import net.charonus2012.orrery.planet.PlanetRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Map;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class PlanetCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> command = literal("orrery");

        command.executes(ctx -> {
            CommandSourceStack source = ctx.getSource();
            Map<?, PlanetData> planets = PlanetRegistry.all();

            if (planets.isEmpty()) {
                source.sendSuccess(() -> Component.literal("No planets registered."), false);
                return 1;
            }

            source.sendSuccess(() -> Component.literal("Loaded planets (" + planets.size() + "):"), false);
            for (PlanetData planet : planets.values()) {
                source.sendSuccess(() -> Component.literal(
                        " - " + planet.planetName() + " (" + planet.dimension().location() + ") gravity=" + planet.gravity()
                ), false);
            }
            return 1;
        });

        dispatcher.register(command);
    }
}
