package edu.handong.csee.isel.fcminer.gumtree.core.actions;

import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.*;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

import java.util.List;

public class ActionUtil {
    private ActionUtil() {}

    public static TreeContext apply(TreeContext context, List<Action> actions) {
        for (Action a: actions) {
            if (a instanceof Insert) {
                Insert action = ((Insert) a);
                action.getParent().insertChild(action.getNode(), action.getPosition());
            } else if (a instanceof Update) {
                Update action = ((Update) a);
                action.getNode().setLabel(action.getValue());
            } else if (a instanceof Move) {
                Move action = ((Move) a);
                action.getNode().getParent().getChildren().remove(action.getNode());
                action.getParent().insertChild(action.getNode(), action.getPosition());
            } else if (a instanceof Delete) {
                Delete action = ((Delete) a);
                action.getNode().getParent().getChildren().remove(action.getNode());
            } else throw new RuntimeException("No such action: " + a );
        }
        return context;
    }
}