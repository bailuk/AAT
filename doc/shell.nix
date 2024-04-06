# Development environment with nix-shell
#  - copy this file to project root
#  - run `nix-shell` from project root
{ pkgs ? import <nixpkgs> {}
}:
pkgs.mkShell {
    name="java-gtk";
    buildInputs = [
        pkgs.git
        pkgs.jdk21_headless # Non headless is linked against gtk3 and does therefore not work
        pkgs.gtk4
        pkgs.geoclue2
        pkgs.sdkmanager     # A drop-in replacement for sdkmanager from the Android SDK written in Python
    ];
    shellHook = ''
        LD_LIBRARY_PATH=${pkgs.gtk4.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.glib.out.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.gdk-pixbuf.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.cairo.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.pango.out.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.libadwaita.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.geoclue2.outPath}/lib
        export LD_LIBRARY_PATH
	ANDROID_HOME=${builtins.getEnv "PWD"}/aat-android/sdk
        export ANDROID_HOME
        sdkmanager --licenses
        echo "./gradlew aat-gtk:build && ./gradlew aat-gtk:run"
        echo "./gradlew tasks"
    '';
}
