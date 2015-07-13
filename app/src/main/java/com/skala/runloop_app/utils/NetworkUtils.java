package com.skala.runloop_app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.skala.runloop_app.models.MemberModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Skala
 */
public class NetworkUtils {

    public static Bitmap downloadImage(String stringURL) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(stringURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // download if ok
                InputStream in = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static ArrayList<MemberModel> downloadMemberList(String stringUrl) {
        ArrayList<MemberModel> memberList = null;
        try {
            Document doc = Jsoup.connect(stringUrl).get();
            Elements staffListContainer = doc.getElementsByClass("staff-list-container");
            Elements members = staffListContainer.get(0).getElementsByClass("figure");

            memberList = new ArrayList<>();
            int sizeMember = members.size();
            for (int i = 0; i < sizeMember; i++) {
                Element member = members.get(i);
                String imageUrl = member.getElementsByClass("figure-image").get(0).select("img").get(0).absUrl("src");
                String description = member.getElementsByClass("figure-caption-description").get(0).text();

                Element title = member.getElementsByClass("figure-caption-title").get(0);
                String fullName = title.select("strong").text();
                String position = title.select("span").text();

                MemberModel memberModel = new MemberModel(imageUrl, fullName, position, description);
                memberList.add(memberModel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return memberList;
    }
}
