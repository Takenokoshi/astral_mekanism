package astral_mekanism.block.blockentity.elements.aekey;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import mekanism.api.IContentsListener;
import mekanism.common.inventory.container.MekanismContainer;
import net.minecraft.nbt.CompoundTag;

public abstract class AEKeySlot<AEKEY extends AEKey> {
    private static final String KEY_SLOT = "keySlot";
    public static final Predicate<AEKey> alwaysTrue = k -> true;// for recipe output slot
    @Nullable
    protected AEKEY key;
    public final Supplier<MEStorage> storageSupplier;
    private final Predicate<? super AEKEY> keyPredicate;
    protected final IContentsListener listener;
    private final Function<CompoundTag, AEKEY> reader;
    private final Function<AEKEY, CompoundTag> saver;
    private final Function<AEKey, AEKEY> caster;
    public final int slotIndex;

    public AEKeySlot(Supplier<MEStorage> storageSupplier, Predicate<? super AEKEY> keyPredicate,
            IContentsListener listener,
            Function<CompoundTag, AEKEY> reader, Function<AEKEY, CompoundTag> saver, Function<AEKey, AEKEY> caster, int slotIndex) {
        this.storageSupplier = storageSupplier;
        this.keyPredicate = keyPredicate;
        this.listener = listener;
        this.reader = reader;
        this.saver = saver;
        this.caster = caster;
        this.slotIndex = slotIndex;
    }

    @Nullable
    public AEKEY getKey() {
        return key;
    }

    public void setKey(AEKey aeKey) {
        AEKEY key = caster.apply(aeKey);
        if (key == null || keyPredicate.test(key) || !Objects.equals(this.key, key)) {
            this.key = key;
            listener.onContentsChanged();
        }
    }

    public boolean accept(AEKey aeKey) {
        AEKEY key = caster.apply(aeKey);
        return key != null ? keyPredicate.test(key) : false;
    }

    public CompoundTag saveToTag() {
        CompoundTag tag = new CompoundTag();
        if (key != null) {
            tag.put(KEY_SLOT, saver.apply(key));
        }
        return tag;
    }

    public void readFromTag(CompoundTag tag) {
        try {
            setKey(reader.apply(tag.getCompound(KEY_SLOT)));
        } catch (Exception e) {
            key = null;
        }
    }

    public long getAmount() {
        MEStorage storage = storageSupplier.get();
        if (storage == null || key == null) {
            return 0;
        }
        return storage.getAvailableStacks().get(key);
    }

    public abstract void trackContainer(MekanismContainer container);
}
