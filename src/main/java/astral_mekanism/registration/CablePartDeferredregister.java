package astral_mekanism.registration;

import java.util.function.Function;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import mekanism.common.registration.impl.ItemDeferredRegister;
import net.minecraft.data.models.blockstates.PropertyDispatch.TriFunction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;

public class CablePartDeferredregister {
    private final String modid;
    public final ItemDeferredRegister itemRegister;

    public CablePartDeferredregister(String modid) {
        this.modid = modid;
        this.itemRegister = new ItemDeferredRegister(this.modid);
    }

    public <PART extends IPart, ITEM extends Item & IPartItem<PART>> CablePartRegistyObject<PART, ITEM> register(
            String id,
            Class<PART> partClass,
            TriFunction<Properties, Class<PART>, Function<IPartItem<PART>, PART>, ITEM> creator,
            Function<IPartItem<PART>, PART> factory) {
        return new CablePartRegistyObject<>(
                itemRegister.register(id, p -> creator.apply(p, partClass, factory)),
                partClass);
    }

    public void register(IEventBus bus) {
        itemRegister.register(bus);
    }

}
