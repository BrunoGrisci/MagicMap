import android.location.Location;

/**
 * Created by bruno on 20/02/15.
 */
public class MyLocation {
    private Location local;
    private String name;

    public MyLocation (Location startLocation, String startName) {
        local.set(startLocation);
        name = startName;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return local;
    }

    public void setLocation(Location l) {
        local.set(l);
    }

    public void setName(String n) {
        name = n;
    }
}
