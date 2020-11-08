package com.robertx22.uncommon.utilityclasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class WizardryUtilities {

    // SECTION Block/Entity/World Utilities
    // ===============================================================================================================

    /**
     * Returns the actual light level, taking natural light (skylight) and
     * artificial light (block light) into account. This uses the same logic as mob
     * spawning.
     * 
     * @return The light level, from 0 (pitch darkness) to 15 (full daylight/at a
     *         torch).
     */
    public static int getLightLevel(World world, BlockPos pos) {

	int i = world.getLightFromNeighbors(pos);

	if (world.isThundering()) {
	    int j = world.getSkylightSubtracted();
	    world.setSkylightSubtracted(10);
	    i = world.getLightFromNeighbors(pos);
	    world.setSkylightSubtracted(j);
	}

	return i;
    }

    /**
     * Returns whether the block at the given coordinates can be replaced by another
     * one (works as if a block is being placed by a player). True for air, liquids,
     * vines, tall grass and snow layers but not for flowers, signs etc. This is a
     * shortcut for
     * <code>world.getBlockState(pos).getMaterial().isReplaceable()</code>.
     * 
     * @see WizardryUtilities#canBlockBeReplacedB(World, BlockPos)
     */
    public static boolean canBlockBeReplaced(World world, BlockPos pos) {
	return world.isAirBlock(new BlockPos(pos)) || world.getBlockState(pos).getMaterial().isReplaceable();
    }

    /**
     * Returns whether the block at the given coordinates can be replaced by another
     * one (works as if a block is being placed by a player) and is not a liquid.
     * True for air, vines, tall grass and snow layers but not for flowers, signs
     * etc. or any liquids.
     * 
     * @see WizardryUtilities#canBlockBeReplaced(World, BlockPos)
     */
    public static boolean canBlockBeReplacedB(World world, BlockPos pos) {
	return canBlockBeReplaced(world, pos) && !world.getBlockState(pos).getMaterial().isLiquid();
    }

    /**
     * Returns whether the block at the given coordinates is unbreakable in survival
     * mode. In vanilla this is true for bedrock and end portal frame, for example.
     * This is a shortcut for world.getBlockState(pos).getBlockHardness(world, pos)
     * == -1.0f. Not much of a shortcut any more, since block ids have been phased
     * out.
     */
    public static boolean isBlockUnbreakable(World world, BlockPos pos) {
	return world.isAirBlock(new BlockPos(pos)) ? false
		: world.getBlockState(pos).getBlockHardness(world, pos) == -1.0f;
    }

    /**
     * Finds the nearest floor level to the given y coord within the range specified
     * at the given x and z coords. Liquids and other blocks that cannot be built on
     * top of do not count, but stuff like signs does. (Technically any block is
     * allowed to be the floor according to the code, but seeing as it searches
     * upwards and non-solid blocks usually need a supporting block, the floor is
     * likely to always be solid).
     * 
     * @param world
     * @param x     The x coordinate to search in
     * @param y     The y coordinate to search from
     * @param z     The z coordinate to search in
     * @param range The maximum distance from the given y coordinate to search.
     * @return The y coordinate of the closest floor level, or -1 if there is none.
     *         Returns the actual level of the floor as would be seen in the debug
     *         screen when the player is standing on it.
     * @see WizardryUtilities#getNearestFloorLevelB(World, BlockPos, int)
     */
    public static int getNearestFloorLevel(World world, BlockPos pos, int range) {

	int yCoord = -2;
	for (int i = -range; i <= range; i++) {
	    // The last bit determines whether the block found to be a suitable floor is
	    // closer than the previous one
	    // found.
	    if (world.isSideSolid(pos.up(i), EnumFacing.UP)
		    && (world.isAirBlock(pos.up(i + 1)) || !world.isSideSolid(pos.up(i + 1), EnumFacing.UP))
		    && (i < yCoord - pos.getY() || yCoord == -2)) {
		yCoord = pos.getY() + i;
	    }
	}
	return yCoord + 1;
    }

    /**
     * Finds the nearest floor level to the given y coord within the range specified
     * at the given x and z coords. Only works if the block above the floor is
     * actually air and the floor is solid or a liquid.
     * 
     * @param world
     * @param x     The x coordinate to search in
     * @param y     The y coordinate to search from
     * @param z     The z coordinate to search in
     * @param range The maximum distance from the given y coordinate to search.
     * @return The y coordinate of the closest floor level, or -1 if there is none.
     *         Returns the actual level of the floor as would be seen in the debug
     *         screen when the player is standing on it.
     * @see WizardryUtilities#getNearestFloorLevel(World, BlockPos, int)
     */
    public static int getNearestFloorLevelB(World world, BlockPos pos, int range) {
	int yCoord = -2;
	for (int i = -range; i <= range; i++) {
	    if (world.isAirBlock(new BlockPos(pos.up(i + 1)))
		    && (world.getBlockState(pos.up(i)).getMaterial().isLiquid()
			    || world.isSideSolid(pos.up(i), EnumFacing.UP))
		    && (i < yCoord - pos.getY() || yCoord == -2)) {
		// The last bit determines whether the block found to be a suitable floor is
		// closer than the previous
		// one found.
		yCoord = pos.getY() + i;
	    }
	}
	return yCoord + 1;
    }

    /**
     * Finds the nearest floor level to the given y coord within the range specified
     * at the given x and z coords. Everything that is not air is treated as floor,
     * even stuff that can't be walked on.
     * 
     * @param world
     * @param x     The x coordinate to search in
     * @param y     The y coordinate to search from
     * @param z     The z coordinate to search in
     * @param range The maximum distance from the given y coordinate to search.
     * @return The y coordinate of the closest floor level, or -1 if there is none.
     *         Returns the actual level of the floor as would be seen in the debug
     *         screen when the player is standing on it.
     * @see WizardryUtilities#getNearestFloorLevel(World, BlockPos, int)
     */
    public static int getNearestFloorLevelC(World world, BlockPos pos, int range) {
	int yCoord = -2;
	for (int i = -range; i <= range; i++) {
	    if (world.isAirBlock(new BlockPos(pos.up(i + 1))) && (i < yCoord - pos.getY() || yCoord == -2)) {
		// The last bit determines whether the block found to be a suitable floor is
		// closer than the previous
		// one found.
		yCoord = pos.getY() + i;
	    }
	}
	return yCoord + 1;
    }

    /**
     * Gets a random position on the ground near the player within the specified
     * horizontal and vertical ranges. Used to find a position to spawn entities in
     * summoning spells.
     * 
     * @param entity          The entity around which to search
     * @param horizontalRange The maximum number of blocks on the x or z axis the
     *                        returned position can be from the given entity. <i>The
     *                        number of operations performed by this method is
     *                        proportional to the square of this parameter, so for
     *                        performance reasons it is recommended that it does not
     *                        exceed around 10.</i>
     * @param verticalRange   The maximum number of blocks on the y axis the
     *                        returned position can be from the given entity
     * @return A BlockPos with the coordinates of the block directly above the
     *         ground at the position found, or null if none were found within
     *         range. Importantly, since this method checks <i>all possible</i>
     *         positions within range (i.e. randomness only occurs when deciding
     *         between the possible positions), if it returns null once then it will
     *         always return null given the same circumstances and parameters. What
     *         this means is that you can (and should) immediately stop trying to
     *         cast a summoning spell if this returns null.
     */
    @Nullable
    public static BlockPos findNearbyFloorSpace(Entity entity, int horizontalRange, int verticalRange) {

	World world = entity.world;
	List<BlockPos> possibleLocations = new ArrayList<BlockPos>();
	BlockPos origin = new BlockPos(entity);

	for (int x = -horizontalRange; x <= horizontalRange; x++) {
	    for (int z = -horizontalRange; z <= horizontalRange; z++) {
		int y = WizardryUtilities.getNearestFloorLevel(world, origin.add(x, 0, z), verticalRange);
		if (y > -1)
		    possibleLocations.add(new BlockPos(origin.getX() + x, y, origin.getZ() + z));
	    }
	}

	if (possibleLocations.isEmpty()) {
	    return null;
	} else {
	    return possibleLocations.get(world.rand.nextInt(possibleLocations.size()));
	}

    }

    /**
     * Gets the blockstate of the block the specified entity is standing on. Uses
     * {@link MathHelper#floor_double(double)} because casting to int will not
     * return the correct coordinate when x or z is negative.
     */
    public static IBlockState getBlockEntityIsStandingOn(Entity entity) {
	BlockPos pos = new BlockPos(MathHelper.floor(entity.posX), (int) entity.getEntityBoundingBox().minY - 1,
		MathHelper.floor(entity.posZ));
	return entity.world.getBlockState(pos);
    }

    /**
     * Shorthand for
     * {@link WizardryUtilities#getEntitiesWithinRadius(double, double, double, double, World, Class)}
     * with EntityLivingBase as the entity type. This is by far the most common use
     * for that method, which is why this shorthand exists.
     * 
     * @param radius The search radius
     * @param x      The x coordinate to search around
     * @param y      The y coordinate to search around
     * @param z      The z coordinate to search around
     * @param world  The world to search in
     */
    public static List<EntityLivingBase> getEntitiesWithinRadius(double radius, double x, double y, double z,
	    World world) {
	return getEntitiesWithinRadius(radius, x, y, z, world, EntityLivingBase.class);
    }

    /**
     * Returns all entities of the specified type within the specified radius of the
     * given coordinates. This is different to using a raw AABB because a raw AABB
     * will search in a cube volume rather than a sphere. Note that this does not
     * exclude any entities; if any specific entities are to be excluded this must
     * be checked when iterating through the list.
     * 
     * @see {@link WizardryUtilities#getEntitiesWithinRadius(double, double, double, double, World)}
     * @param radius     The search radius
     * @param x          The x coordinate to search around
     * @param y          The y coordinate to search around
     * @param z          The z coordinate to search around
     * @param world      The world to search in
     * @param entityType The class of entity to search for; pass in Entity.class for
     *                   all entities
     */
    public static <T extends Entity> List<T> getEntitiesWithinRadius(double radius, double x, double y, double z,
	    World world, Class<T> entityType) {
	AxisAlignedBB aabb = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
	List<T> entityList = world.getEntitiesWithinAABB(entityType, aabb);
	for (int i = 0; i < entityList.size(); i++) {
	    if (entityList.get(i).getDistance(x, y, z) > radius) {
		entityList.remove(i);
	    }
	}
	return entityList;
    }

    public static <T extends Entity> List<T> getEntitiesWithinRadius(double radius, Entity entity,
	    Class<T> entityType) {
	double x = entity.posX;
	double y = entity.posY;
	double z = entity.posZ;
	AxisAlignedBB aabb = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
	List<T> entityList = entity.world.getEntitiesWithinAABB(entityType, aabb);
	for (int i = 0; i < entityList.size(); i++) {
	    if (entityList.get(i).getDistance(x, y, z) > radius) {
		entityList.remove(i);
	    }
	}
	return entityList;
    }

    /**
     * Gets an entity from its UUID. Note that you should check this isn't null. If
     * the UUID is known to belong to an EntityPlayer, use the more efficient
     * {@link World#getPlayerEntityByUUID(UUID)} instead.
     * 
     * @param world The world the entity is in
     * @param id    The entity's UUID
     * @return The Entity that has the given UUID, or null if no such entity exists
     *         in the specified world.
     */
    @Nullable
    public static Entity getEntityByUUID(World world, UUID id) {

	for (Entity entity : world.loadedEntityList) {
	    // This is a perfect example of where you need to use .equals() and not ==. For
	    // most applications,
	    // this was unnoticeable until world reload because the UUID instance or entity
	    // instance is stored.
	    // Fixed now though.
	    if (entity.getUniqueID().equals(id)) {
		return entity;
	    }
	}
	return null;
    }

    // No point allowing anything other than players for these methods since other
    // entities can use Entity#playSound.

    /**
     * Shortcut for
     * {@link World#playSound(EntityPlayer, double, double, double, SoundEvent, SoundCategory, float, float)}
     * where the player is null but the x, y and z coordinates are those of the
     * passed in player. Use in preference to
     * {@link EntityPlayer#playSound(SoundEvent, float, float)} if there are
     * client-server discrepancies.
     */
    public static void playSoundAtPlayer(EntityPlayer player, SoundEvent sound, SoundCategory category, float volume,
	    float pitch) {
	player.world.playSound(null, player.posX, player.posY, player.posZ, sound, category, volume, pitch);
    }

    /**
     * See
     * {@link WizardryUtilities#playSoundAtPlayer(EntityPlayer, SoundEvent, SoundCategory, float, float)}.
     * Category defaults to {@link SoundCategory#PLAYERS}.
     */
    public static void playSoundAtPlayer(EntityPlayer player, SoundEvent sound, float volume, float pitch) {
	player.world.playSound(null, player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, volume,
		pitch);
    }

    /**
     * Returns the entity riding the given entity, or null if there is none. Allows
     * for neater code now that entities have a list of passengers, because it is
     * necessary to check that the list is not null or empty first.
     */
    @Nullable
    public static Entity getRider(Entity entity) {
	return entity.getPassengers() != null && !entity.getPassengers().isEmpty() ? entity.getPassengers().get(0)
		: null;
    }

    /**
     * Attacks the given entity with the given damage source and amount, but
     * preserving the entity's original velocity instead of applying knockback, as
     * would happen with
     * {@link EntityLivingBase#attackEntityFrom(DamageSource, float)} <i>(More
     * accurately, calls that method as normal and then resets the entity's velocity
     * to what it was before).</i> Handy for when you need to damage an entity
     * repeatedly in a short space of time.
     * 
     * @param entity The entity to attack
     * @param source The source of the damage
     * @param amount The amount of damage to apply
     * @return True if the attack succeeded, false if not.
     */
    public static boolean attackEntityWithoutKnockback(Entity entity, DamageSource source, float amount) {
	double vx = entity.motionX;
	double vy = entity.motionY;
	double vz = entity.motionZ;
	boolean succeeded = entity.attackEntityFrom(source, amount);
	entity.motionX = vx;
	entity.motionY = vy;
	entity.motionZ = vz;
	return succeeded;
    }

    /**
     * Applies the standard (non-enchanted) amount of knockback to the given target,
     * using the same calculation as
     * {@link EntityLivingBase#attackEntityFrom(DamageSource, float)}. Use in
     * conjunction with
     * {@link WizardryUtilities#attackEntityWithoutKnockback(Entity, DamageSource, float)}
     * to change the source of knockback for an attack.
     * 
     * @param attacker The entity that caused the knockback; the target will be
     *                 pushed away from this entity.
     * @param target   The entity to be knocked back.
     */
    public static void applyStandardKnockback(Entity attacker, EntityLivingBase target) {
	double dx = attacker.posX - target.posX;
	double dz;
	for (dz = attacker.posZ - target.posZ; dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random())
		* 0.01D) {
	    dx = (Math.random() - Math.random()) * 0.01D;
	}
	// The first argument is never used.
	target.knockBack(null, 0.4f, dx, dz);
    }

    // Just what benefit does having posY be the eye position on the first person
    // client actually give?

    /**
     * Gets the y coordinate of the given player's eyes. This is to cover an
     * inconsistency between the value of EntityPlayer.posY on the first person
     * client and everywhere else; in first person (i.e. when
     * Minecraft.getMinecraft().player == player) player.posY is the eye position,
     * but everywhere else it is the feet position. This is intended for use when
     * spawning particles, since this is the only situation where the discrepancy is
     * likely to matter.
     * <p>
     * As of Wizardry 1.2, this is just a shorthand for:
     * <p>
     * <code><center>player.getEntityBoundingBox().minY + player.getEyeHeight()</code></center>
     */
    public static double getPlayerEyesPos(EntityLivingBase player) {
	return player.getEntityBoundingBox().minY + player.getEyeHeight();
    }

    /**
     * Returns a list of the itemstacks in the given player's hotbar. Defined here
     * for convenience and to centralise the (unfortunately unavoidable) use of
     * hardcoded numbers to reference the inventory slots. The returned list is a
     * modifiable copy of part of the player's inventory stack list; as such,
     * changes to the list are <b>not</b> written through to the player's inventory.
     * However, the ItemStack instances themselves are not copied, so changes to any
     * of their fields (size, metadata...) will change those in the player's
     * inventory.
     * 
     * @since Wizardry 1.2
     */
    public static List<ItemStack> getHotbar(EntityPlayer player) {
	NonNullList<ItemStack> hotbar = NonNullList.create();
	hotbar.addAll(player.inventory.mainInventory.subList(0, 9));
	return hotbar;
    }

    /**
     * Returns a list of the itemstacks in the given player's hotbar and offhand,
     * sorted into the following order: main hand, offhand, rest of hotbar
     * left-to-right. The returned list is a modifiable copy of part of the player's
     * inventory stack list; as such, changes to the list are <b>not</b> written
     * through to the player's inventory. However, the ItemStack instances
     * themselves are not copied, so changes to any of their fields (size,
     * metadata...) will change those in the player's inventory.
     * 
     * @since Wizardry 1.2
     */
    public static List<ItemStack> getPrioritisedHotbarAndOffhand(EntityPlayer player) {
	List<ItemStack> hotbar = WizardryUtilities.getHotbar(player);
	// Adds the offhand item to the beginning of the list so it is processed before
	// the hotbar
	hotbar.add(0, player.getHeldItemOffhand());
	// Moves the item in the main hand to the beginning of the list so it is
	// processed first
	hotbar.remove(player.getHeldItemMainhand());
	hotbar.add(0, player.getHeldItemMainhand());
	return hotbar;
    }

    /**
     * Tests whether the specified player has any of the specified item in their
     * entire inventory, including armour slots and offhand.
     */
    public static boolean doesPlayerHaveItem(EntityPlayer player, Item item) {

	for (ItemStack stack : player.inventory.mainInventory) {
	    if (stack.getItem() == item) {
		return true;
	    }
	}

	for (ItemStack stack : player.inventory.armorInventory) {
	    if (stack.getItem() == item) {
		return true;
	    }
	}

	for (ItemStack stack : player.inventory.offHandInventory) {
	    if (stack.getItem() == item) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Checks if the given player is opped on the given server. If the server is a
     * singleplayer or LAN server, this means they have cheats enabled.
     */
    public static boolean isPlayerOp(EntityPlayer player, MinecraftServer server) {
	return server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null;
    }

    /**
     * Returns true if the given entity is an EntityLivingBase and not an armour
     * stand; makes the code a bit neater. This was added because armour stands are
     * a subclass of EntityLivingBase, but shouldn't necessarily be treated as
     * living entities - this depends on the situation. <i>The given entity can
     * safely be cast to EntityLivingBase if this method returns true.</i>
     */
    // In my opinion, it's a bad design choice to have armour stands extend
    // EntityLivingBase directly - it would be
    // better to make a parent class which is extended by both armour stands and
    // EntityLivingBase and contains only
    // the code required by both.
    public static boolean isLiving(Entity entity) {
	return entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand);
    }

    // SECTION Raytracing
    // ===============================================================================================================

    /**
     * Does a block ray trace (NOT entities) from an entity's eyes (i.e.
     * properly...)
     */
    @Nullable
    public static RayTraceResult rayTrace(double range, World world, EntityLivingBase entity, boolean hitLiquids) {

	Vec3d start = new Vec3d(entity.posX, entity.getEntityBoundingBox().minY + entity.getEyeHeight(), entity.posZ);
	Vec3d look = entity.getLookVec();
	Vec3d end = start.add(look.x * range, look.y * range, look.z * range);
	return world.rayTraceBlocks(start, end, hitLiquids);
    }

    /**
     * Helper method which does a rayTrace for entities from an entity's eye level
     * in the direction they are looking with a specified range, using the tracePath
     * method. Tidies up the code a bit. Border size defaults to 1.
     * 
     * @param world
     * @param entity
     * @param range
     * @return
     */
    @Nullable
    public static RayTraceResult standardEntityRayTrace(World world, EntityLivingBase entity, double range) {
	double dx = entity.getLookVec().x * range;
	double dy = entity.getLookVec().y * range;
	double dz = entity.getLookVec().z * range;
	HashSet<Entity> hashset = new HashSet<Entity>(1);
	hashset.add(entity);
	return WizardryUtilities.tracePath(world, (float) entity.posX,
		(float) (entity.getEntityBoundingBox().minY + entity.getEyeHeight()), (float) entity.posZ,
		(float) (entity.posX + dx), (float) (entity.posY + entity.getEyeHeight() + dy),
		(float) (entity.posZ + dz), 1.0f, hashset, false);
    }

    /**
     * Helper method which does a rayTrace for entities from a entity's eye level in
     * the direction they are looking with a specified range and radius, using the
     * tracePath method. Tidies up the code a bit.
     * 
     * @param world
     * @param entity
     * @param range
     * @param borderSize
     * @return
     */
    @Nullable
    public static RayTraceResult standardEntityRayTrace(World world, EntityLivingBase entity, double range,
	    float borderSize) {
	double dx = entity.getLookVec().x * range;
	double dy = entity.getLookVec().y * range;
	double dz = entity.getLookVec().z * range;
	HashSet<Entity> hashset = new HashSet<Entity>(1);
	hashset.add(entity);
	return WizardryUtilities.tracePath(world, (float) entity.posX,
		(float) (entity.getEntityBoundingBox().minY + entity.getEyeHeight()), (float) entity.posZ,
		(float) (entity.posX + dx), (float) (entity.posY + entity.getEyeHeight() + dy),
		(float) (entity.posZ + dz), borderSize, hashset, false);
    }

    /**
     * Method for ray tracing entities (the useless default method doesn't work,
     * despite EnumHitType having an ENTITY field...) You can also use this for
     * seeking.
     * 
     * @param world
     * @param x          startX
     * @param y          startY
     * @param z          startZ
     * @param tx         endX
     * @param ty         endY
     * @param tz         endZ
     * @param borderSize extra area to examine around line for entities
     * @param excluded   any excluded entities (the player, etc)
     * @return a RayTraceResult of either the block hit (no entity hit), the entity
     *         hit (hit an entity), or null for nothing hit
     */
    @Nullable
    public static RayTraceResult tracePath(World world, float x, float y, float z, float tx, float ty, float tz,
	    float borderSize, HashSet<Entity> excluded, boolean collideablesOnly) {

	Vec3d startVec = new Vec3d(x, y, z);
	// Vec3d lookVec = new Vec3d(tx-x, ty-y, tz-z);
	Vec3d endVec = new Vec3d(tx, ty, tz);
	float minX = x < tx ? x : tx;
	float minY = y < ty ? y : ty;
	float minZ = z < tz ? z : tz;
	float maxX = x > tx ? x : tx;
	float maxY = y > ty ? y : ty;
	float maxZ = z > tz ? z : tz;
	AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).grow(borderSize, borderSize,
		borderSize);
	List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);
	RayTraceResult blockHit = world.rayTraceBlocks(startVec, endVec);
	startVec = new Vec3d(x, y, z);
	endVec = new Vec3d(tx, ty, tz);
	float maxDistance = (float) endVec.distanceTo(startVec);
	if (blockHit != null) {
	    maxDistance = (float) blockHit.hitVec.distanceTo(startVec);
	}
	Entity closestHitEntity = null;
	float closestHit = maxDistance;
	float currentHit = 0.f;
	AxisAlignedBB entityBb;// = ent.getBoundingBox();
	RayTraceResult intercept;
	for (Entity ent : allEntities) {
	    if ((ent.canBeCollidedWith() || !collideablesOnly)
		    && ((excluded != null && !excluded.contains(ent)) || excluded == null)) {
		float entBorder = ent.getCollisionBorderSize();
		entityBb = ent.getEntityBoundingBox();
		if (entityBb != null) {
		    entityBb = entityBb.grow(entBorder, entBorder, entBorder);
		    intercept = entityBb.calculateIntercept(startVec, endVec);
		    if (intercept != null) {
			currentHit = (float) intercept.hitVec.distanceTo(startVec);
			if (currentHit < closestHit || currentHit == 0) {
			    closestHit = currentHit;
			    closestHitEntity = ent;
			}
		    }
		}
	    }
	}
	if (closestHitEntity != null) {
	    blockHit = new RayTraceResult(closestHitEntity);
	}
	return blockHit;
    }

    // SECTION Rendering and GUIs
    // ===============================================================================================================

    // Doesn't seem right to put this in the proxies since it should only ever be
    // called from client-side code, and I'm
    // not about to make a whole separate utilities class just for one method. Fully
    // qualified names it is!
    /**
     * <b>[Client-side only]</b> Draws a textured rectangle, taking the size of the
     * image and the bit needed into account, unlike
     * {@link net.minecraft.client.gui.Gui#drawTexturedModalRect(int, int, int, int, int, int)
     * Gui.drawTexturedModalRect(int, int, int, int, int, int)}, which is harcoded
     * for only 256x256 textures. Also handy for custom potion icons.
     * 
     * @param x             The x position of the rectangle
     * @param y             The y position of the rectangle
     * @param u             The x position of the top left corner of the section of
     *                      the image wanted
     * @param v             The y position of the top left corner of the section of
     *                      the image wanted
     * @param width         The width of the section
     * @param height        The height of the section
     * @param textureWidth  The width of the actual image.
     * @param textureHeight The height of the actual image.
     */
    @SideOnly(Side.CLIENT)
    public static void drawTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth,
	    int textureHeight) {

	float f = 1F / (float) textureWidth;
	float f1 = 1F / (float) textureHeight;

	// Essentially the same as getting the tessellator. For most code, you'll want
	// the tessellator AND the
	// vertexbuffer
	// stored in local variables.
	BufferBuilder buffer = net.minecraft.client.renderer.Tessellator.getInstance().getBuffer();
	// Equivalent of tessellator.startDrawingQuads()
	buffer.begin(org.lwjgl.opengl.GL11.GL_QUADS,
		net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX);
	// Equivalent of tessellator.addVertex()
	buffer.pos((double) (x), (double) (y + height), 0)
		.tex((double) ((float) (u) * f), (double) ((float) (v + height) * f1)).endVertex();
	buffer.pos((double) (x + width), (double) (y + height), 0)
		.tex((double) ((float) (u + width) * f), (double) ((float) (v + height) * f1)).endVertex();
	buffer.pos((double) (x + width), (double) (y), 0)
		.tex((double) ((float) (u + width) * f), (double) ((float) (v) * f1)).endVertex();
	buffer.pos((double) (x), (double) (y), 0).tex((double) ((float) (u) * f), (double) ((float) (v) * f1))
		.endVertex();
	// Exactly the same as before.
	net.minecraft.client.renderer.Tessellator.getInstance().draw();
    }

    /**
     * Shorthand for
     * {@link WizardryUtilities#drawTexturedRect(int, int, int, int, int, int, int, int)}
     * which draws the entire texture (u and v are set to 0 and textureWidth and
     * textureHeight are the same as width and height).
     */
    @SideOnly(Side.CLIENT)
    public static void drawTexturedRect(int x, int y, int width, int height) {
	drawTexturedRect(x, y, 0, 0, width, height, width, height);
    }

    // SECTION NBT and Data Storage
    // ===============================================================================================================

    /**
     * Verifies that the given string is a valid string representation of a UUID.
     * More specifically, returns true if and only if the given string is not null
     * and matches the regular expression:
     * <p>
     * <center><code>/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/<p></code></center>
     * which is the regex equivalent of the standard string representation of a UUID
     * as described in {@link UUID#toString()}. This method is intended to be used
     * as a check to prevent an {@link IllegalArgumentException} from occurring when
     * calling {@link UUID#fromString(String)}.
     * 
     * @param string The string to be checked
     * @return Whether the given string is a valid string representation of a UUID
     * @deprecated UUIDs can now be stored in NBT directly; use that in preference
     *             to storing them as strings.
     */
    @Deprecated
    public static boolean verifyUUIDString(String string) {
	return string != null
		&& string.matches("/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/");
    }

    /**
     * Generic method that stores any Map to an NBTTagList, given two functions that
     * convert the key and value types in that map to subclasses of NBTBase. For
     * what it's worth, there is very little point in using this unless you can use
     * something more concise than an anonymous class to do the conversion. A lambda
     * expression, or better, a method reference, would fit nicely. For example,
     * take ExtendedPlayer's use of this to store conjured item durations:
     * <p>
     * <code>properties.setTag("conjuredItems", WizardryUtilities.mapToNBT(this.conjuredItemDurations,
     * item -> new NBTTagInt(Item.getIdFromItem((Item)item)), NBTTagInt::new));</code>
     * <p>
     * This is a lot nicer than simply iterating through the map, because for that
     * you need to use the entry list, which introduces local variables that aren't
     * really necessary. Notice that, since the values V in the map are simply
     * Integer objects, a simple constructor reference to NBTTagInt::new can be used
     * instead of a lambda expression (the Integer is auto-unboxed to int).
     * 
     * @param               <K> The type of key stored in the given Map.
     * @param               <V> The type of value stored in the given Map.
     * @param               <L> The subtype of NBTBase that the keys (of type K)
     *                      will be converted to.
     * @param               <W> The subtype of NBTBase that the values (of type V)
     *                      will be converted to.
     * @param map           The Map to be stored.
     * @param keyFunction   A Function that converts the keys in the map to NBT
     *                      objects that can be stored.
     * @param valueFunction A Function that converts the values in the map to NBT
     *                      objects that can be stored.
     * @param keyTagName    The tag name to use for the key tags.
     * @param valueTagName  The tag name to use for the value tags.
     * @return An NBTTagList that represents the given Map.
     */
    public static <K, V, L extends NBTBase, W extends NBTBase> NBTTagList mapToNBT(Map<K, V> map,
	    Function<K, L> keyFunction, Function<V, W> valueFunction, String keyTagName, String valueTagName) {

	NBTTagList tagList = new NBTTagList();

	for (Entry<K, V> entry : map.entrySet()) {
	    NBTTagCompound mapping = new NBTTagCompound();
	    mapping.setTag(keyTagName, keyFunction.apply(entry.getKey()));
	    mapping.setTag(valueTagName, valueFunction.apply(entry.getValue()));
	    tagList.appendTag(mapping);
	}

	return tagList;
    }

    /**
     * See
     * {@link WizardryUtilities#mapToNBT(Map, Function, Function, String, String)};
     * this version is for when the names of the individual key/value tags are
     * unimportant (they default to "key" and "value" respectively).
     */
    public static <K, V, L extends NBTBase, W extends NBTBase> NBTTagList mapToNBT(Map<K, V> map,
	    Function<K, L> keyFunction, Function<V, W> valueFunction) {
	return mapToNBT(map, keyFunction, valueFunction, "key", "value");
    }

    /**
     * Generic method that stores any Collection to an NBTTagList, given a function
     * that converts the elements in that collection to subclasses of NBTBase. For
     * what it's worth, there is very little point in using this unless you can use
     * something more concise than an anonymous class to do the conversion. A lambda
     * expression, or better, a method reference, would fit nicely.
     * 
     * @param          <E> The type of element stored in the given Collection.
     * @param          <T> The NBT tag type that the elements will be converted to.
     * @param list     The Collection to be stored.
     * @param function A Function that converts the elements in the collection to
     *                 NBT objects that can be stored.
     * @return An NBTTagList that represents the given Collection.
     */
    public static <E, T extends NBTBase> NBTTagList listToNBT(Collection<E> list, Function<E, T> function) {

	NBTTagList tagList = new NBTTagList();
	// If the collection is ordered, it will preserve the order, even though we
	// don't know what type it is yet.
	for (E element : list) {
	    tagList.appendTag(function.apply(element));
	}

	return tagList;
    }

    /**
     * Removes the UUID with the given key from the given NBT tag, if any. Why this
     * doesn't exist in vanilla I have no idea.
     */
    public static void removeUniqueId(NBTTagCompound tag, String key) {
	tag.removeTag(key + "Most");
	tag.removeTag(key + "Least");
    }

    // TODO: Backport: It has recently become apparent that storing UUIDs as strings
    // is not good practice, so backport
    // these two
    // methods to 1.7.10 and replace tag.setUniqueId and tag.getUniqueId with their
    // respective contents from 1.10.2.

    /**
     * Returns an NBTTagCompound which contains only the given UUID, stored using
     * {@link NBTTagCompound#setUniqueId(String, UUID)}. Allows for neater storage
     * to NBTTagLists.
     */
    public static NBTTagCompound UUIDtoTagCompound(UUID id) {
	NBTTagCompound tag = new NBTTagCompound();
	tag.setUniqueId("uuid", id);
	return tag;
    }

    /**
     * Wrapper for {@link NBTTagCompound#getUniqueId(String)} which converts an
     * NBTTagCompound directly to a UUID. Intended to be used as the inverse of
     * {@link WizardryUtilities#UUIDtoTagCompound(UUID)}.
     */
    public static UUID tagCompoundToUUID(NBTTagCompound tag) {
	return tag.getUniqueId("uuid");
    }

}
