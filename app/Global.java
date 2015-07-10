import org.apache.commons.io.FileUtils;
import play.Application;
import play.GlobalSettings;

import java.io.File;
import java.io.IOException;

/**
 * Created by anthony on 7/10/15.
 */
public class Global extends GlobalSettings {
    public void onStart(Application app) {
        try {
            FileUtils.deleteDirectory(new File("/tmp/box"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
