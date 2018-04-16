package tdt4140.gr1844.app.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Charts extends Application {

    private String patientName;

    public Charts(String patientName) {
        this.patientName = patientName;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Feeling Score");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days in recovery");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Patient Data for Doctor X");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Physical recovery of " + patientName);
        //populating the series with data
        int num = 40;
        for (int i = 4; i < 41; i++) {
            double relation = Math.ceil(8*i/num);
            series.getData().add(new XYChart.Data(i, (relation)));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }
}
