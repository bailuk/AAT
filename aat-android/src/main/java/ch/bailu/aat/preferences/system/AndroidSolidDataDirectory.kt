package ch.bailu.aat.preferences.system

import android.content.Context
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.foc_android.FocAndroidFactory

class AndroidSolidDataDirectory(c: Context) :
    SolidDataDirectory(AndroidSolidDataDirectoryDefault(c), FocAndroidFactory(c))
