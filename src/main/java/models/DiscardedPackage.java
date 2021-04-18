package models;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class DiscardedPackage {
    private final String packageName;
    private String packagePath;
    private String lifetime;
    private final List<Map<String, Double>> previousReleaseMetices;
    private int versionLength = 0;


    public DiscardedPackage(String packageName, String packagePath, String lifetime, List<Map<String, Double>> previousReleaseMetices) {
        this.packageName = packageName;
        this.packagePath = packagePath;
        this.lifetime = lifetime;
        this.previousReleaseMetices = previousReleaseMetices;
    }

    public int getVersionLength() {
        return versionLength;
    }

    public void setVersionLength(int versionLength) {
        this.versionLength = versionLength;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    public List<Map<String, Double>> getPreviousReleaseMetices() {
        return previousReleaseMetices;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("package_name", this.packageName);
        jsonObject.append("package_path", this.packagePath);
        jsonObject.append("lifetime", this.lifetime);
        jsonObject.append("metrics", this.previousReleaseMetices);
        jsonObject.append("length", this.versionLength);
        return jsonObject;
    }

    public boolean isAvailaleInRelease(String release) {
        String[] releaseterminals = this.lifetime.split("_");
        release = release.replaceFirst("v", "");
        String firstRelease = releaseterminals[0].replaceFirst("v", "");
        String lastRelease = releaseterminals[releaseterminals.length - 1].replaceFirst("v", "");

        return (release.charAt(0) >= firstRelease.charAt(0) && release.charAt(2) >= firstRelease.charAt(2) && release.charAt(4) >= firstRelease.charAt(4)) &&
                (release.charAt(0) <= lastRelease.charAt(0) && release.charAt(2) <= lastRelease.charAt(2) && release.charAt(4) <= lastRelease.charAt(4));
    }
}
