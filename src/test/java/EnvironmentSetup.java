import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentSetup extends TestWatcher {

    private Properties properties;

    @Override
    protected void starting(Description description) {
        properties = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/environment.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}

