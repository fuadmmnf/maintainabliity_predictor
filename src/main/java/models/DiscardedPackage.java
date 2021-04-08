package models;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class DiscardedPackage {
    private final String packageName;
    private final String lifetime;
    private final List<Map<String, Double>> previousReleaseMetices;

    public DiscardedPackage(String packageName, String lifetime, List<Map<String, Double>> previousReleaseMetices) {
        this.packageName = packageName;
        this.lifetime = lifetime;
        this.previousReleaseMetices = previousReleaseMetices;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getLifetime() {
        return lifetime;
    }

    public List<Map<String, Double>> getPreviousReleaseMetices() {
        return previousReleaseMetices;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("package_name", this.packageName);
        jsonObject.append("lifetime", this.lifetime);
        jsonObject.append("metrics", this.previousReleaseMetices);
        return jsonObject;
    }

}
