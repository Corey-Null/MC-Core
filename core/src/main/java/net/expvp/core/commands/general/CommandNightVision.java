package net.expvp.core.commands.general;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class used for handling command /nightvision
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandNightVision extends AccountCommand {

	public CommandNightVision(NullContainer container) {
		super(container, "nightvision", "core.nightvision", "/nightvision", 0);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		Player player = account.getPlayer();
		if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
			player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		} else {
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
		}
	}

}
