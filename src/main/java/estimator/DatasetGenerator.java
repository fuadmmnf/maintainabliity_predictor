package estimator;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import models.DiscardedPackage;
import java.io.*;
import java.util.List;

public class DatasetGenerator {

    public void generateDatasetFromList(List<DiscardedPackage> discardedPackages) {
        try (FileWriter outputWriter = new FileWriter("/src/main/resources/dataset/MaintainabilityMetric.csv", true)) {
            CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
            for (DiscardedPackage discardedpackage : discardedPackages) {
                writer.writeRow(discardedpackage.toJSON());
            }
            writer.close();
        } catch (IOException e) {
            // handle exception
        }
    }
}
