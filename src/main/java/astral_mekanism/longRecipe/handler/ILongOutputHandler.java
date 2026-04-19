package astral_mekanism.longRecipe.handler;

import astral_mekanism.longRecipe.LongOperationTracker;

public interface ILongOutputHandler<OUTPUT> {
    void handleOutput(OUTPUT toOutput, long operations);

    void calculateOperationsCanSupport(LongOperationTracker tracker, OUTPUT toOutput);
}
