package seedu.fast.ui;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import seedu.fast.commons.util.Colors;
import seedu.fast.commons.util.TagUtil;
import seedu.fast.model.tag.Tag;

/**
 * A HBox that contains the set of tags and the tag image.
 */
public class TagComponent extends HBox {
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView displayPicture;

    /**
     * An UI component that contains the set of tags and the icon image.
     * @param tagSet The Set of Tags.
     * @param img The image of the icon.
     */
    public TagComponent(Set<Tag> tagSet, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/TagComponent.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tagSet.stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(colorSelector(tag.tagName)));
        displayPicture.setImage(img);
    }


    /**
     * Selects the color for tags based on their String names.
     * @param tagName Name of the Tag.
     * @return Label with color.
     */
    public Label colorSelector(String tagName) {
        Label temp = new Label(tagName);

        switch (tagName.toUpperCase()) { //Todo: add more color tags & abstract out case names

        case TagUtil.FRIENDS:
            temp.setStyle(Colors.BLUE);
            break;

        case TagUtil.HIGH_PRIORITY:
            temp.setStyle(Colors.RED);
            break;

        case TagUtil.MEDIUM_PRIORITY:
            temp.setStyle(Colors.ORANGE);
            break;

        case TagUtil.LOW_PRIORITY:
            temp.setStyle(Colors.GREEN);
            break;

        default:
            temp.setStyle(Colors.GREY);
        }
        return temp;
    }
}