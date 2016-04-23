package eu.ha3.mc.haddon.implem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import eu.ha3.mc.haddon.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class HaddonClientImpl implements Client {
	
	protected Minecraft unsafe() {
		return Minecraft.getMinecraft();
	}

	@Override
	public EntityPlayer getPlayer() {
		return unsafe().thePlayer;
	}
	
	private int getDimensionIndex(int dimensionLevel) {
        if (dimensionLevel == -1) return 1;
        if (dimensionLevel == 1) return 2;
        return dimensionLevel;
    }
	
	@Override
	public List<EntityPlayer> getAllPlayers() {
		MinecraftServer server = MinecraftServer.getServer();
		if (server != null) {
			EntityPlayer player = getPlayer();
			if (player != null) {
				int dimension = getDimensionIndex(player.dimension);
				if (server.worldServers != null && dimension >= 0 && dimension < server.worldServers.length) {
					WorldServer world = server.worldServers[dimension];
					if (world != null) {
						return world.playerEntities; //Hosting/Singleplayer
					}
				}
				return unsafe().theWorld.playerEntities; //Remote
			}
		}
		return new ArrayList<EntityPlayer>();
		
	}

	@Override
	public FontRenderer getFontRenderer() {
		return unsafe().fontRendererObj;
	}

	@Override
	public void addKeyBinding(KeyBinding binding) {
		Minecraft mc = unsafe();
		mc.gameSettings.keyBindings = ArrayUtils.addAll(mc.gameSettings.keyBindings, binding);
		KeyBinding.resetKeyBindingArrayAndHash();
	}
	
	@Override
	public void removeKeyBinding(KeyBinding binding) {
		Minecraft mc = unsafe();
		List<KeyBinding> kept = new ArrayList<KeyBinding>();
		for (KeyBinding i : mc.gameSettings.keyBindings) {
			if (!i.equals(binding)) kept.add(i);
		}
		if (kept.size() != mc.gameSettings.keyBindings.length) {
			mc.gameSettings.keyBindings = kept.toArray(new KeyBinding[kept.size()]);
			KeyBinding.resetKeyBindingArrayAndHash();
		}
	}
	
	@Override
	public boolean registerReloadListener(IResourceManagerReloadListener reloadListener) {
		IResourceManager manager = unsafe().getResourceManager();
		if (manager instanceof IReloadableResourceManager) {
			((IReloadableResourceManager)manager).registerReloadListener(reloadListener);
			return true;
		}
		return false;
	}
}
