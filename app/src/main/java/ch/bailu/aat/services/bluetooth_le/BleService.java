package ch.bailu.aat.services.bluetooth_le;

import android.os.Build;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class BleService extends VirtualService {
    private final Scanner scanner;


    public BleService(ServiceContext sc) {
        super(sc);

        if (Build.VERSION.SDK_INT >= 18) {
            scanner = new ScannerSDK18(sc.getContext());
        } else {
            scanner = new Scanner();
        }
    }

    @Override
    public void appendStatusText(StringBuilder builder) {

    }

    @Override
    public void close() {

    }

    public void scan() {
        scanner.scann();
    }

    @Override
    public String toString() {
        return scanner.toString();
    }
}
