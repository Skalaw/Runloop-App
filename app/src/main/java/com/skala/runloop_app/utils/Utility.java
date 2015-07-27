package com.skala.runloop_app.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;

import com.skala.runloop_app.AlphaSatColorMatrixEvaluator;
import com.skala.runloop_app.models.MemberModel;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * @author Skala
 */
public class Utility {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void animationLoadImage(final Drawable drawable, long time) {
        AlphaSatColorMatrixEvaluator evaluator = new AlphaSatColorMatrixEvaluator();
        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(evaluator.getColorMatrix());
        drawable.setColorFilter(filter);

        ObjectAnimator animator = ObjectAnimator.ofObject(filter, "colorMatrix", evaluator, evaluator.getColorMatrix());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawable.setColorFilter(filter);
            }
        });
        animator.setDuration(time);
        animator.start();
    }

    public static ArrayList<MemberModel> parseMemberList(Document doc) {
        ArrayList<MemberModel> memberList;

        Elements staffListContainer = doc.getElementsByClass("staff-list-container");
        if (staffListContainer.size() == 0) {
            return null;
        }

        Elements members = staffListContainer.get(0).getElementsByClass("figure");

        memberList = new ArrayList<>();
        int sizeMember = members.size();
        for (int i = 0; i < sizeMember; i++) {
            Element member = members.get(i);

            String imageUrl = getImageUrl(member);
            String description = getDescription(member);
            String fullName = "null";
            String position = "null";

            Elements title = member.getElementsByClass("figure-caption-title");
            if (title.size() != 0) {
                fullName = title.select("strong").text();
                position = title.select("span").text();
            }

            MemberModel memberModel = new MemberModel(imageUrl, fullName, position, description);
            memberList.add(memberModel);
        }

        return memberList;
    }

    private static String getImageUrl(Element member) {
        String imageUrl = "null";

        Elements elementsFigureImage = member.getElementsByClass("figure-image");
        if (elementsFigureImage.size() != 0) {
            Elements img = elementsFigureImage.get(0).select("img");
            if (img.size() != 0) {
                imageUrl = img.get(0).absUrl("src");
            }
        }

        return imageUrl;
    }

    private static String getDescription(Element member) {
        String description = "null";

        Elements elementsFigureDescription = member.getElementsByClass("figure-caption-description");
        if (elementsFigureDescription.size() != 0) {
            description = elementsFigureDescription.get(0).text();
        }

        return description;
    }
}
