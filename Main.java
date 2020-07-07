package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        AnimationTimer[] timers = new AnimationTimer[2];

        BorderPane root = new BorderPane();
        root.setPrefSize(400,600);
        root.setStyle("-fx-background-color: pink;");

        Pane pane = new AnchorPane();
        pane.setPrefSize(400.,500);
        double height = pane.getPrefHeight(),
                width = pane.getPrefWidth();

        Circle outer = new Circle(width/2,height/2,150);
        outer.setFill(Color.YELLOW);
        outer.setStrokeWidth(5);
        outer.setStroke(Color.GREEN);

        Circle smallOuter = new Circle(width/2,(height/2)-outer.getRadius(),30);
        smallOuter.setFill(Color.RED);
        smallOuter.setStrokeWidth(5);
        smallOuter.setStroke(Color.GREEN);

        Circle inner = new Circle(width/2, height/2,75);
        inner.setFill(Color.WHITE);
        inner.setStrokeWidth(3);
        inner.setStroke(Color.RED);

        Circle smallInner = new Circle(width/2,height/2-inner.getRadius(), 15);
        smallInner.setFill(Color.GREEN);
        smallInner.setStrokeWidth(3);
        smallInner.setStroke(Color.RED);

        Slider slider = new Slider(0, 1, 0.5);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.25);
        slider.setPrefWidth(400);

        for (int i = 0; i < timers.length; i++) {
            if(i == 0) {
                LongProperty tBefore = new SimpleLongProperty();
                DoubleProperty alpha = new SimpleDoubleProperty(270*Math.PI/180);
                DoubleProperty dist  = new SimpleDoubleProperty();

                timers[i] = new AnimationTimer() {
                    double xZero = outer.getCenterX();
                    double yZero = outer.getCenterY();
                    double R = outer.getRadius();

                    @Override
                    public void handle(long l) {
                        DoubleProperty time = new SimpleDoubleProperty(0.5);
                        time.bind(new SimpleDoubleProperty(slider.getValue()));
                        double t = time.getValue();

                        if(tBefore.getValue() > 0) {
                            double dt = (l - tBefore.getValue()) * 1e-9 / 200 * t;
                            dist.set(2*Math.PI*R*dt);
                            double X = Math.round(R * Math.cos(alpha.getValue())) + xZero;
                            double Y = Math.round(R * Math.sin(alpha.getValue())) + yZero;

                            smallOuter.setCenterY(Y);
                            smallOuter.setCenterX(X);
                        }

                        tBefore.set(l);
                        alpha.set(alpha.getValue()+dist.getValue());
                    }
                };
            } else {
                LongProperty tBefore = new SimpleLongProperty();
                DoubleProperty alpha = new SimpleDoubleProperty(270*Math.PI/180);
                DoubleProperty dist  = new SimpleDoubleProperty();

                timers[i] = new AnimationTimer() {
                    int counter = 0;
                    double xZero = inner.getCenterX();
                    double yZero = inner.getCenterY();
                    double R = inner.getRadius();

                    @Override
                    public void handle(long l) {
                        DoubleProperty time = new SimpleDoubleProperty(0.5);
                        time.bind(new SimpleDoubleProperty(slider.getValue()));
                        double t = time.getValue();

                        if(tBefore.getValue() > 0) {
                            double dt = (l - tBefore.getValue()) * 1e-9 / 100 * (1-t);
                            dist.set(2*Math.PI*R*dt);
                            double X = Math.round(R * Math.cos(alpha.getValue())) + xZero;
                            double Y = Math.round(R * Math.sin(alpha.getValue())) + yZero;

                            smallInner.setCenterY(Y);
                            smallInner.setCenterX(X);
                        }

                        tBefore.set(l);
                        alpha.set(alpha.getValue()-dist.getValue());
                    }
                };
            }
        }

        pane.getChildren().addAll(outer,inner, smallOuter, smallInner);

        HBox bottomBox = new HBox();
        bottomBox.getChildren().add(slider);

        root.setBottom(bottomBox);
        root.setCenter(pane);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AvocadoPride");
        primaryStage.show();
        for (int i = 0; i < timers.length; i++) {
            timers[i].start();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
