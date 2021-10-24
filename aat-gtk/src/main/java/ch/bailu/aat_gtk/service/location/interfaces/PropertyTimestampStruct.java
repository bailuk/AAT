package ch.bailu.aat_gtk.service.location.interfaces;

import org.freedesktop.dbus.Struct;
import org.freedesktop.dbus.annotations.Position;
import org.freedesktop.dbus.types.UInt64;

/**
 * Semi-auto-generated class.
 */
public class PropertyTimestampStruct extends Struct {
    @Position(0)
    private final UInt64 member0;
    @Position(1)
    private final UInt64 member1;

    public PropertyTimestampStruct(UInt64 member0, UInt64 member1) {
        this.member0 = member0;
        this.member1 = member1;
    }


    public UInt64 getMember0() {
        return member0;
    }
    public UInt64 getMember1() {
        return member1;
    }

}