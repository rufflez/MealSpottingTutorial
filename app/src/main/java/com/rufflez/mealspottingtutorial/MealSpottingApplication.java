package com.rufflez.mealspottingtutorial;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by rufflez on 1/5/16.
 */
public class MealSpottingApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        /*
		 * In this tutorial, we'll subclass ParseObject for convenience to
		 * create and modify Meal objects
		 */
        ParseObject.registerSubclass(Meal.class);

		/*
		 * Fill in this section with your Parse credentials
		 */
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

		/*
		 * For more information on app security and Parse ACL:
		 * https://www.parse.com/docs/android_guide#security-recommendations
		 */
        ParseACL defaultACL = new ParseACL();

		/*
		 * If you would like all objects to be private by default, remove this
		 * line
		 */
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
