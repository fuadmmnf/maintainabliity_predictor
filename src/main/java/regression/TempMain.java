package regression;


import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;

public class TempMain {
    public static void main(String[] args) throws PythonExecutionException, IOException {
        double[] year = {1980.00, 1985.00, 1990.00, 1995.00, 2000.00};
        double[] population = {2.1, 2.9, 3.2, 4.1, 4.9};
        LinearRegression linearRegressionModel = new LinearRegression(year, population);
        linearRegressionModel.saveScatterPlot(year, population, "plot1.jpg");
//        System.out.println(linearRegressionModel.toString());
    }
}
