package sample;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.List;

public class Controller implements Initializable {
    @FXML
    private ImageView view;

    @FXML
    private ImageView arrowView;

    @FXML
    private TextField iterationCount;

    @FXML
    private TextField initialX;

    @FXML
    private TextField initialY;

    @FXML
    private TextField initialAngle;

    @FXML
    private TextField rotateBy;

    @FXML
    private TextField initialCommand;

    @FXML
    private Label generationOptions;

    @FXML
    private Label rulesTitle;

    @FXML
    private Label presetsTitle;

    @FXML
    private Label initialValuesLabel;

    @FXML
    private Button generateButton;

    @FXML
    private Button addRule;

    @FXML
    private TextField fromField;

    @FXML
    private TextField toField;

    @FXML
    private ListView<ListItem> ruleList;

    @FXML
    private CheckBox shouldUseAntialiasing;

    @FXML
    private TextField scale;

    @FXML
    private MenuButton presets;

    private List<Rule> rules = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        view.setImage(new Image("file:resources/placeholder.png"));
        arrowView.setImage(new Image("file:resources/arrow.png"));

        generationOptions.getStyleClass().add("title");
        initialValuesLabel.getStyleClass().add("title");
        rulesTitle.getStyleClass().add("title");
        presetsTitle.getStyleClass().add("title");

        rules.add(new Rule("X", "F+[[X]-X]-F[-FX]+X"));
        rules.add(new Rule("F", "FF"));
        //rules.add(new Rule("X", "F[-X]+X"));
        //rules.add(new Rule("F", "F-F++F-F"));

        final MenuItem tree1 = new MenuItem("Tree1");
        final MenuItem tree2 = new MenuItem("Tree2");
        final MenuItem snowflake1 = new MenuItem("Snowflake1");

        presets.getItems().setAll(tree1, tree2, snowflake1);

        tree1.setOnAction(actionEvent -> {
            presets.setText("Tree1");
            initialX.setText("3000");
            initialY.setText("5000");
            initialAngle.setText("90");
            rotateBy.setText("25");
            initialCommand.setText("X");
            ruleList.getItems().clear();
            ruleList.getItems().addAll(new ListItem(ruleList, new Rule("X", "F+[[X]-X]-F[-FX]+X")),
                    new ListItem(ruleList, new Rule("F", "FF")));
            iterationCount.setText("9");
            shouldUseAntialiasing.setSelected(true);
            scale.setText("0.03");
        });

        tree2.setOnAction(actionEvent -> {
            presets.setText("Tree2");
            initialX.setText("100");
            initialY.setText("0");
            initialAngle.setText("90");
            rotateBy.setText("25");
            initialCommand.setText("X");
            ruleList.getItems().clear();
            ruleList.getItems().addAll(new ListItem(ruleList, new Rule("X", "F+[[X]-X]-F[-FX]+X")),
                    new ListItem(ruleList, new Rule("X", "F[-X]+X")),
                    new ListItem(ruleList, new Rule("F", "FF")));
            iterationCount.setText("4");
            shouldUseAntialiasing.setSelected(true);
            scale.setText("0.5");
        });

        snowflake1.setOnAction(actionEvent -> {
            presets.setText("Snowflake1");
            initialX.setText("-1050");
            initialY.setText("-2000");
            initialAngle.setText("0");
            rotateBy.setText("60");
            initialCommand.setText("F-F-F-F-F-F");
            ruleList.getItems().clear();
            ruleList.getItems().addAll(new ListItem(ruleList, new Rule("F", "F-F++F-F")));
            iterationCount.setText("5");
            shouldUseAntialiasing.setSelected(false);
            scale.setText("0.1");
        });

        for (var rule : rules)
            ruleList.getItems().add(new ListItem(ruleList, rule));

        addRule.setOnAction(actionEvent -> {
            ruleList.getItems().add(new ListItem(ruleList, new Rule(fromField.getText(), toField.getText())));
        });

        generateButton.setOnAction(actionEvent -> {
            System.out.println(Arrays.toString(ruleList.getItems().toArray()));
            final BufferedImage image = new BufferedImage((int) view.getFitWidth(), (int) view.getFitHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D gfx = image.createGraphics();
            if (shouldUseAntialiasing.isSelected())
                gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            gfx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            gfx.setColor(Color.decode("#4E6885"));
            gfx.fillRect(0, 0, 700, 900);

            gfx.translate(image.getWidth() / 2, image.getHeight());
            float scaleXY = Float.parseFloat(scale.getText());
            gfx.scale(scaleXY, scaleXY);

            final float lineLength = 10.3f;
            final int iterations = Integer.parseInt(iterationCount.getText());
            final double theta = Math.toRadians(Integer.parseInt(rotateBy.getText()));

            // getting initial position, rotation, command
            Point point = new Point(Integer.parseInt(initialX.getText()), Integer.parseInt(initialY.getText()), Math.toRadians(Integer.parseInt(initialAngle.getText())));
            String command = initialCommand.getText();

            Stack<Point> stack = new Stack<>();

            gfx.setColor(Color.decode("#E6B3CC"));

            for (int i = 0; i < iterations; i++)
                for (var rule : ruleList.getItems())
                    command = rule.getRule().apply(command);

            for (var symbol : command.toCharArray()) {
                switch (symbol) {
                    case '+':
                        point.rotate(-theta);
                        break;
                    case '-':
                        point.rotate(theta);
                        break;
                    case '[':
                        stack.push(new Point(point));
                        break;
                    case ']':
                        point = stack.pop();
                        break;
                    case 'F':
                        gfx.drawLine((int) point.getX(), (int) point.getY(), (int) (point.getX() + lineLength * Math.cos(point.getRotation())), (int) (point.getY() + -lineLength * Math.sin(point.getRotation())));
                        point.add((float) (lineLength * Math.cos(point.getRotation())), (float) (-lineLength * Math.sin(point.getRotation())));
                        break;
                    case 'X':
                }
            }

            gfx.dispose();
            try {
                ImageIO.write(image, "png", new File("resources/trees/temp.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            view.setImage(SwingFXUtils.toFXImage(image, null));
        });
    }
}
