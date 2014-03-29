package net.ivorius.psychedelicraft.blocks;

import net.ivorius.psychedelicraft.Psychedelicraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCannabisPlant extends Block implements IvBonemealCompatibleBlock, IvTilledFieldPlant
{
    public IIcon[] textures = new IIcon[3];

    public BlockCannabisPlant()
    {
        super(Material.plants);
        float var3 = 0.375F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 1.0F, 0.5F + var3);
        this.setTickRandomly(true);

        this.disableStats();
        setStepSound(Block.soundTypeGrass);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 && par5Random.nextFloat() < 0.12f)
            {
                if (this.canGrow(par1World, par2, par3, par4))
                {
                    this.growStep(par1World, par2, par3, par4, false);
                }
            }
        }
    }

    @Override
    public int getMaxMetadata(int position)
    {
        if (position > 2)
        {
            return -1;
        }

        return (position == 2) ? 11 : 15;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        Block var5 = par1World.getBlock(par2, par3 - 1, par4);
        return var5 == this ? true : (var5 != Blocks.grass && var5 != Blocks.dirt && var5 != Blocks.farmland ? false : true);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        this.checkBlockCoordValid(par1World, par2, par3, par4);
    }

    /**
     * Checks if current block pos is valid, if not, breaks the block as dropable item. Used for reed and cactus.
     */
    protected final void checkBlockCoordValid(World par1World, int par2, int par3, int par4)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), par1World.getBlockMetadata(par2, par3, par4));
            par1World.setBlock(par2, par3, par4, Blocks.air, 0, 3);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return this.canPlaceBlockAt(par1World, par2, par3, par4);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 1;
    }

    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            int countB = par1World.rand.nextInt(par5 / 6 + 1);
            for (int i = 0; i < countB; i++)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(Psychedelicraft.itemCannabisBuds, 1, 0));
            }

            int countL = par1World.rand.nextInt(par5 / 5 + 1) + par5 / 6;
            for (int i = 0; i < countL; i++)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(Psychedelicraft.itemCannabisLeaf, 1, 0));
            }

            int countS = par5 / 8;
            for (int i = 0; i < countS; i++)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(Psychedelicraft.itemCannabisSeeds, 1, 0));
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        super.registerBlockIcons(par1IconRegister);

        textures[0] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "cannabisPlant1");
        textures[1] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "cannabisPlant2");
        textures[2] = par1IconRegister.registerIcon(Psychedelicraft.textureBase + "cannabisPlant3");
    }

    @Override
    public IIcon getIcon(int par1, int par2)
    {
        if (par2 < 4)
        {
            return super.getIcon(par1, par2);
        }
        else if (par2 < 8)
        {
            return textures[0];
        }
        else if (par2 < 12)
        {
            return textures[1];
        }
        else if (par2 < 16)
        {
            return textures[2];
        }

        return super.getIcon(par1, par2);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (IvBonemealHelper.tryGrowing(par1World, par2, par3, par4, par5EntityPlayer, this))
        {
            return true;
        }

        return false;
    }

    @Override
    public void growStep(World par1World, int x, int y, int z, boolean bonemeal)
    {
        int number = bonemeal ? par1World.rand.nextInt(4) + 1 : 1;

        for (int i = 0; i < number; i++)
        {
            int var6;

            for (var6 = 1; par1World.getBlock(x, y - var6, z) == this; ++var6)
            {

            }

            int m = par1World.getBlockMetadata(x, y, z);
            boolean freeOver = par1World.isAirBlock(x, y + 1, z) && var6 < 3;

            if ((m < 15 && freeOver) || (!freeOver && m < 11))
            {
                par1World.setBlockMetadataWithNotify(x, y, z, m + 1, 3);
            }
            else if (par1World.isAirBlock(x, y + 1, z))
            {
                if (freeOver && m == 15)
                {
                    par1World.setBlock(x, y + 1, z, this, 0, 3);
                }
            }
        }
    }

    @Override
    public boolean canGrow(World par1World, int x, int y, int z)
    {
        int var6;

        for (var6 = 1; par1World.getBlock(x, y - var6, z) == this; ++var6)
        {

        }

        int m = par1World.getBlockMetadata(x, y, z);
        boolean freeOver = var6 < 3;

        if ((m < 15 && freeOver) || (!freeOver && m < 11))
        {
            return true;
        }
        else if (par1World.isAirBlock(x, y + 1, z))
        {
            if (freeOver && m == 15)
            {
                return true;
            }
        }

        return false;
    }
}