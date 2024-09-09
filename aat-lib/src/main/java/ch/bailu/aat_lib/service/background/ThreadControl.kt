package ch.bailu.aat_lib.service.background;

public interface ThreadControl {
    boolean canContinue();


    ThreadControl KEEP_ON = () -> true;
}
