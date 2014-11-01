/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraft.entities;

import net.minecraft.entity.EntityLivingBase;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;

/**
 * Created by lukas on 01.11.14.
 */
public interface DrugFactory
{
    void createDrugs(EntityLivingBase entity, List<Pair<String, Drug>> drugs);
}
