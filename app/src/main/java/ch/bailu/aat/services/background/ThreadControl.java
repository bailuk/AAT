package ch.bailu.aat.services.background;

public interface ThreadControl {
	boolean canContinue();


	ThreadControl KEEP_ON = new ThreadControl() {
		@Override
		public boolean canContinue() {
			return true;
		}
	};
}
