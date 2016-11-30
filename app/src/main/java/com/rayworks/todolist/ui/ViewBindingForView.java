package com.rayworks.todolist.ui;

/**
 * Created by Sean on 11/30/16.
 */

import android.view.View;

import org.robobinding.annotation.ViewBinding;
import org.robobinding.customviewbinding.CustomViewBinding;

/**
 * Code taken from https://github.com/RoboBinding/RoboBinding-album-sample/blob/master/app/src/main/
 * java/org/robobinding/albumsample/activity/ViewBindingForView.java
 *
 * @author Cheng Wei
 * @version $Revision: 1.0 $
 * @since 1.0
 */
@ViewBinding(simpleOneWayProperties = {"enabled"})
public class ViewBindingForView extends CustomViewBinding<View> {
}
