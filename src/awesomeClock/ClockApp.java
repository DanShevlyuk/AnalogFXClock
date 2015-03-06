package awesomeClock;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class ClockApp extends Application {

    private Timeline secondsTimeLine;
    private Timeline minutesTimeLine;
    private Timeline hoursTimeLine;

    private Group createGroupOfBaseCircles() {
        final Circle base = new Circle(100, 100, 120);
        base.setId("baseCircle");
        final Circle clockFace = new Circle(100, 100, 100);
        clockFace.setId("clockFace");

        return new Group(base, clockFace);
    }

    private Group createArrowsGroup() {
        final Line secondsArrow = new Line(0, 10, 0, -80);
        secondsArrow.setId("secondsArrow");
        final Line minutesArrow = new Line(0, 10, 0, -70);
        minutesArrow.setId("minutesArrow");
        final Line hoursArrow = new Line(0, 10, 0, -55);
        hoursArrow.setId("hoursArrow");


        Calendar calendar = GregorianCalendar.getInstance();
        final double secondsAngle  = calendar.get(Calendar.SECOND) * 6;
        final double minutesAngle = (calendar.get(Calendar.MINUTE) + secondsAngle / 360) * 6;
        final double hoursAngle = (calendar.get(Calendar.HOUR)   + minutesAngle / 360) * 30;

        final Rotate secondRotate = new Rotate(secondsAngle);
        secondsArrow.getTransforms().add(secondRotate);

        final Rotate minutesRotate = new Rotate(minutesAngle);
        minutesArrow.getTransforms().add(minutesRotate);

        final Rotate hoursRotate = new Rotate(hoursAngle);
        hoursArrow.getTransforms().add(hoursRotate);

        secondsTimeLine = new Timeline(
                new KeyFrame(
                        Duration.seconds(60),
                        new KeyValue(
                                secondRotate.angleProperty(),
                                360 + secondsAngle,
                                Interpolator.LINEAR
                        )
                )
        );

        minutesTimeLine = new Timeline(
                new KeyFrame(
                        Duration.minutes(60),
                        new KeyValue(
                                minutesRotate.angleProperty(),
                                360 + minutesAngle,
                                Interpolator.LINEAR
                        )
                )
        );

        hoursTimeLine = new Timeline(
                new KeyFrame(
                        Duration.hours(12),
                        new KeyValue(
                                hoursRotate.angleProperty(),
                                360 + hoursAngle,
                                Interpolator.LINEAR
                        )
                )
        );
        secondsTimeLine.setCycleCount(Animation.INDEFINITE);
        minutesTimeLine.setCycleCount(Animation.INDEFINITE);
        hoursTimeLine.setCycleCount(Animation.INDEFINITE);

        return new Group(hoursArrow, minutesArrow, secondsArrow);
    }

    private Group createGroupOfTicks() {
        Group ticks = new Group();
        ticks.setId("ticks");
        for (int i = 0; i < 4; i++) {
            Circle circleTick = new Circle(0, -87, 4);
            circleTick.setFill(Paint.valueOf("#60B281"));
            circleTick.setTranslateX(100);
            circleTick.setTranslateY(100);
            circleTick.getStyleClass().add("tick");
            circleTick.getTransforms().add(new Rotate(i * (360 / 4)));
            ticks.getChildren().add(circleTick);
        }

        return ticks;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group baseCircles = createGroupOfBaseCircles();
        Group arrows = createArrowsGroup();
        Group ticks = createGroupOfTicks();

        final Circle centerPoint = new Circle(100, 100, 4);
        centerPoint.setId("centerPoint");

        final Group clock = new Group(baseCircles, ticks, arrows, centerPoint);

        secondsTimeLine.play();
        minutesTimeLine.play();
        hoursTimeLine.play();

        final VBox layout = new VBox();
        layout.getChildren().addAll(clock);
        layout.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(layout, Color.TRANSPARENT);
        scene.getStylesheets().add("awesomeClock/clockStyle.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Clock");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
