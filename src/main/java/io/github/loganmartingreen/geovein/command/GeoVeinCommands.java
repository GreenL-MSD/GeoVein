package io.github.loganmartingreen.geovein.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.loganmartingreen.geovein.data.OreDefinition;
import io.github.loganmartingreen.geovein.data.OreDefinitionLoader;
import io.github.loganmartingreen.geovein.worldgen.Deposit;
import io.github.loganmartingreen.geovein.worldgen.DepositGenerator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import io.github.loganmartingreen.geovein.component.ModDataComponents;
import io.github.loganmartingreen.geovein.item.ModItems;
import io.github.loganmartingreen.geovein.ore.OreGrade;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class GeoVeinCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("geovein")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("deposits")
                                .executes(context -> showDeposits(context.getSource())))
                        .then(Commands.literal("test_deposit")
                                .then(Commands.argument("ore", StringArgumentType.word())
                                        .executes(context -> generateTestDeposit(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "ore")
                                        ))))
                        .then(Commands.literal("process_hand")
                                .executes(context -> processHeldOreChunks(context.getSource())))
        );
    }

    private static int showDeposits(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        long worldSeed = level.getSeed();
        BlockPos playerPos = BlockPos.containing(source.getPosition());

        source.sendSuccess(
                () -> Component.literal("GeoVein deposits near your current region:"),
                false
        );

        for (OreDefinition ore : OreDefinitionLoader.getLoadedDefinitions()) {
            int regionSizeBlocks = ore.regionSizeChunks() * 16;

            int regionX = Math.floorDiv(playerPos.getX(), regionSizeBlocks);
            int regionZ = Math.floorDiv(playerPos.getZ(), regionSizeBlocks);

            Optional<Deposit> deposit = DepositGenerator.generateForRegion(
                    ore,
                    worldSeed,
                    regionX,
                    regionZ
            );

            if (deposit.isPresent()) {
                Deposit foundDeposit = deposit.get();

                source.sendSuccess(
                        () -> Component.literal(
                                ore.displayName()
                                        + " deposit: center "
                                        + formatPos(foundDeposit.center())
                                        + ", shape " + ore.shape().name().toLowerCase()
                                        + ", size "
                                        + foundDeposit.length()
                                        + "x"
                                        + foundDeposit.width()
                                        + "x"
                                        + foundDeposit.height()
                        ),
                        false
                );
            } else {
                source.sendSuccess(
                        () -> Component.literal(
                                ore.displayName() + ": no deposit in this region"
                        ),
                        false
                );
            }
        }

        return 1;
    }

    private static int generateTestDeposit(CommandSourceStack source, String oreId) {
        ServerLevel level = source.getLevel();
        BlockPos center = BlockPos.containing(source.getPosition()).below(8);

        Optional<OreDefinition> oreOptional = OreDefinitionLoader.getById(oreId);

        if (oreOptional.isEmpty()) {
            source.sendFailure(Component.literal("Unknown GeoVein ore: " + oreId));
            return 0;
        }

        OreDefinition ore = oreOptional.get();

        Deposit testDeposit = new Deposit(
                ore,
                center,
                ore.length(),
                ore.width(),
                ore.height(),
                level.getSeed() ^ ore.id().hashCode()
        );

        int placed = 0;

        int xRadius = ore.length() / 2;
        int yRadius = ore.height() / 2;
        int zRadius = ore.width() / 2;

        for (int x = center.getX() - xRadius; x <= center.getX() + xRadius; x++) {
            for (int y = center.getY() - yRadius; y <= center.getY() + yRadius; y++) {
                for (int z = center.getZ() - zRadius; z <= center.getZ() + zRadius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (!testDeposit.contains(pos)) {
                        continue;
                    }

                    if (!shouldReplace(level.getBlockState(pos))) {
                        continue;
                    }

                    double distance = testDeposit.getNormalizedDistanceFromCore(pos);
                    double density = calculateDebugDensity(distance);

                    long noiseSeed = testDeposit.seed()
                            ^ ((long) x * 341873128712L)
                            ^ ((long) y * 132897987541L)
                            ^ ((long) z * 42317861L);

                    double noise = new java.util.Random(noiseSeed).nextDouble();

                    if (noise > density) {
                        continue;
                    }

                    Optional<Block> oreBlockOptional = getOreBlockForY(ore, pos.getY());

                    if (oreBlockOptional.isEmpty()) {
                        continue;
                    }

                    level.setBlock(pos, oreBlockOptional.get().defaultBlockState(), 2);
                    placed++;
                }
            }
        }

        int placedCount = placed;

        source.sendSuccess(
                () -> Component.literal(
                        "Generated test " + ore.displayName() + " deposit at "
                                + formatPos(center)
                                + ". Placed " + placedCount + " blocks."
                ),
                true
        );

        return placed;
    }
    private static int processHeldOreChunks(CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Only players can process held ore chunks."));
            return 0;
        }

        ItemStack heldStack = player.getMainHandItem();

        if (!heldStack.is(ModItems.ORE_CHUNK.get())) {
            source.sendFailure(Component.literal("Hold GeoVein ore chunks in your main hand."));
            return 0;
        }

        String oreId = heldStack.getOrDefault(ModDataComponents.ORE_ID.get(), "unknown");
        OreGrade grade = heldStack.getOrDefault(ModDataComponents.ORE_GRADE.get(), OreGrade.COMMON);

        Optional<OreDefinition> oreOptional = OreDefinitionLoader.getById(oreId);

        if (oreOptional.isEmpty()) {
            source.sendFailure(Component.literal("Unknown ore chunk type: " + oreId));
            return 0;
        }

        OreDefinition ore = oreOptional.get();

        if (ore.processedItem().isBlank()) {
            source.sendFailure(Component.literal("Ore has no processed_item configured: " + oreId));
            return 0;
        }

        ResourceLocation outputItemId = ResourceLocation.parse(ore.processedItem());
        Optional<Item> outputItemOptional = BuiltInRegistries.ITEM.getOptional(outputItemId);

        if (outputItemOptional.isEmpty()) {
            source.sendFailure(Component.literal("Unknown processed item: " + ore.processedItem()));
            return 0;
        }

        int inputCount = heldStack.getCount();
        int outputCount = (int) Math.floor(inputCount * grade.getYieldMultiplier());

        if (outputCount <= 0) {
            source.sendFailure(Component.literal(
                    "Not enough " + grade.getDisplayName() + " " + ore.displayName()
                            + " chunks to process. Try at least 4 Poor chunks."
            ));
            return 0;
        }

        heldStack.shrink(inputCount);

        ItemStack outputStack = new ItemStack(outputItemOptional.get(), outputCount);

        if (!player.addItem(outputStack)) {
            player.drop(outputStack, false);
        }

        source.sendSuccess(
                () -> Component.literal(
                        "Processed " + inputCount + " "
                                + grade.getDisplayName() + " "
                                + ore.displayName()
                                + " Ore Chunks into "
                                + outputCount + " "
                                + outputStack.getHoverName().getString()
                ),
                true
        );

        return outputCount;
    }
    private static boolean shouldReplace(BlockState state) {
        return state.is(Blocks.STONE)
                || state.is(Blocks.DEEPSLATE)
                || state.is(Blocks.TUFF)
                || state.is(Blocks.ANDESITE)
                || state.is(Blocks.DIORITE)
                || state.is(Blocks.GRANITE);
    }

    private static String formatPos(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
    private static double calculateDebugDensity(double distance) {
        if (distance <= 0.25) {
            return 1.0;
        }

        double fadeProgress = (distance - 0.25) / 0.75;
        double edgeDensity = 0.30;

        double density = 1.0 - fadeProgress * (1.0 - edgeDensity);

        return Math.max(0.0, Math.min(1.0, density));
    }
    private static Optional<Block> getOreBlockForY(OreDefinition ore, int y) {
        String blockId = ore.sourceOreBlock();

        if (y <= ore.deepslateBelowY() && !ore.deepslateOreBlock().isBlank()) {
            blockId = ore.deepslateOreBlock();
        }

        ResourceLocation resourceLocation = ResourceLocation.parse(blockId);

        return BuiltInRegistries.BLOCK.getOptional(resourceLocation);
    }
}