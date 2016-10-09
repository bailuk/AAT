package ch.bailu.aat.services;

import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.services.tracker.TrackerService;

public class OneService extends AbsService  {

    public TrackerService tracker;
    public BackgroundService background;
    public IconMapService iconMap;
    public CacheService   cache;
    public DirectoryService directory;
    public ElevationService elevation;
    public TileRemoverService tileRemover;

    private VirtualServiceLink scontext;

    @Override 
    public void onCreate() {
        super.onCreate();

        scontext = new VirtualServiceLink(this);

        tracker = new TrackerService(scontext);
        background = new BackgroundService(scontext);
        iconMap = new IconMapService(scontext);
        cache = new CacheService(scontext);
        directory = new DirectoryService(scontext);
        elevation = new ElevationService(scontext);
        tileRemover = new TileRemoverService(scontext);

    }


    @Override
    public void onDestroy() {
        tracker.close();        tracker=null;
        background.close();     background=null;
        iconMap.close();        iconMap=null;
        cache.close();          cache=null;
        directory.close();      directory=null;
        elevation.close();      elevation=null;
        tileRemover.close();    tileRemover=null;

        scontext=null;
        super.onDestroy();

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        cache.getSelf().onLowMemory();
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);
        appendStatusText(tracker, builder);
        appendStatusText(background, builder);
        appendStatusText(cache, builder);
        appendStatusText(iconMap, builder);
        appendStatusText(directory, builder);
        appendStatusText(elevation, builder);
    }

    public void appendStatusText(VirtualService service, StringBuilder builder) {
        builder.append("<h1>");
        builder.append(service.getClass().getSimpleName());
        builder.append("</h1>");

        builder.append("<p>");
        service.appendStatusText(builder);
        builder.append("</p>");
    }
}
