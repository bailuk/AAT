package ch.bailu.aat.services.background;

public interface ThreadControl {
	boolean canContinue();


	ThreadControl KEEP_ON = () -> true;
}
