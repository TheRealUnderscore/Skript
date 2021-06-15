/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Peter Güttinger
 */

@Name("Used Item")
@Description("The item used in a <a href='events.html#bow_shoot'>bow shoot event</a>, i.e. when you shoot a spectral arrow, this expression returns the spectral arrow itemstack.")
@Examples("on bow shoot:\n" +
    "\tif event-item is a bow:\n" +
    "\t\tsend \"You shot 1 of your %type of used item%<reset> arrows!\" to shooter")
@Since("INSERT VERSION")
@Events({ "bow_shoot" })

public class ExprUsedItem extends SimpleExpression<ItemStack> {
	static {
		Skript.registerExpression(ExprUsedItem.class, ItemStack.class, ExpressionType.SIMPLE, "[the] used item");
	}
	
	@Override
	public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		if (!getParser().isCurrentEvent(EntityShootBowEvent.class)) {
			Skript.error("Cannot use 'used item' outside of a bow shoot event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}
	
	@Override
	protected ItemStack[] get(final Event e) {
		return new ItemStack[] { getUsedItem(e) };
	}
	
	@Nullable
	private static ItemStack getUsedItem(final @Nullable Event e) {
		if (e == null)
			return null;
		final EntityShootBowEvent edbee = (EntityShootBowEvent) e;
		return edbee.getConsumable();
	}
	
	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		if (e == null)
			return "the used item";
		return Classes.getDebugMessage(getSingle(e));
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
}
