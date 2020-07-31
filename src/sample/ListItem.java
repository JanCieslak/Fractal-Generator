package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListItem extends HBox {
    private Rule rule;

    public ListItem(ListView<ListItem> listView, Rule rule) {
        this.rule = rule;

        final Button deleteBtn = new Button("X");
        deleteBtn.setMinSize(20, 20);

        final Label fromLabel = new Label(rule.getFrom());
        final Label toLabel = new Label(rule.getTo());
        final ImageView imageView = new ImageView();
        final Image image = new Image("file:resources/listArrow.png");

        deleteBtn.setOnAction(actionEvent -> {
            listView.getItems().remove(this);
        });

        imageView.setImage(image);
        imageView.setFitWidth(10);
        imageView.setFitHeight(20);

        this.setSpacing(10);
        this.setPrefHeight(20);

        this.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().addAll(deleteBtn, fromLabel, imageView, toLabel);
    }

    public Rule getRule() {
        return rule;
    }
}
