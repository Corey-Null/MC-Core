package net.expvp.core.sign;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.expvp.api.interfaces.IMessages;
import net.expvp.api.interfaces.sign.ISign;
import net.expvp.api.interfaces.sign.ISignFunction;
import net.expvp.core.NullContainer;

/**
 * Class used for handling ISign integration
 * 
 * @author NullUser
 * @see ISign
 */
public abstract class AbstractSign implements ISign {

	protected final NullContainer container;
	protected ISignFunction function;

	public AbstractSign(NullContainer container) {
		this.container = container;
		container.getPlugin().getServer().getPluginManager().registerEvents(this, container.getPlugin());
	}

	/**
	 * @see ISign
	 */
	@EventHandler(ignoreCancelled = true)
	@Override
	public void onInteractEvent(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		Block block = event.getClickedBlock();
		if (block == null || !(block.getState() instanceof Sign)) {
			return;
		}
		Sign sign = (Sign) block.getState();
		if (isInstance(sign) && function != null) {
			function.apply(event.getPlayer(), sign);
		}
	}

	/**
	 * @see ISign
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	@Override
	public void onPlaceEvent(SignChangeEvent event) {
		Sign sign = (Sign) event.getBlock().getState();
		if (canBeSign(event.getLines())) {
			IMessages messages = container.getMessages();
			if (loadSign(event.getPlayer(), sign, event.getLines())) {
				messages.sendMessage(event.getPlayer(), null, "sign.success");
				event.setCancelled(true);
			} else {
				messages.sendMessage(event.getPlayer(), null, "sign.denied");
			}
		}
	}

	/**
	 * @see ISign
	 */
	@Override
	public ISignFunction getSignFunction() {
		return function;
	}

	/**
	 * @see ISign
	 */
	@Override
	public void applySignFunction(ISignFunction function) {
		this.function = function;
	}

}
