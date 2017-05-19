package ch.bailu.aat.services.background;

public interface ThreadControl {
	public boolean canContinue();


	public static final ThreadControl KEEP_ON = new ThreadControl() {
		@Override
		public boolean canContinue() {
			return true;
		}
	};
}
