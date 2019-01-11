/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.artec.apps.facebook.insights;

import edu.artec.apps.facebook.utils.*;
import edu.artec.apps.facebook.pojo.DataInsights;
import static edu.artec.apps.facebook.utils.JSONUtils.readJSon;
import static edu.artec.apps.facebook.utils.StrUtils.csvLine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.*;

public class VideoInsights {
    

    //https://developers.facebook.com/docs/graph-api/reference/video/video_insights/
    //https://developers.facebook.com/docs/graph-api/reference/v3.2/insights
    private List<DataInsights> data;
    public List<DataInsights> getData() {
        return data;
    }

    public VideoInsights(String API_VERSION, String token, String urlVideo, String metrics) {
        urlVideo = URLUtils.removeDiagonal(urlVideo);
        List<String> urlDir  = StrUtils.urlDir(urlVideo);
        try {
            long idPost = Long.parseLong(urlDir.get(urlDir.size()-1));
            String userPage = urlDir.get(urlDir.size()-3);
            FacebookPageUtils fbPg = new FacebookPageUtils(userPage, token);
            String idPage = fbPg.getID();
            request(API_VERSION, token,idPage,String.valueOf(idPost), metrics);
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }

    public VideoInsights(String API_VERSION, String token, String idPage, String idPost, String metrics) throws Exception {
        request(API_VERSION, token,idPage,idPost, metrics);
    }
    
    private synchronized void request(String API_VERSION, String token, String idPage, String idPost, String metrics) throws Exception {
        String requestURLVideo = "https://graph.facebook.com/"+API_VERSION+"/"+idPage+"_"+idPost+"/insights/"+metrics+"?access_token="+token;
        System.out.println("REQ: "+requestURLVideo);
        org.json.simple.JSONObject json = readJSon(requestURLVideo);
        org.json.JSONObject obj = new org.json.JSONObject(json.toString());
        node(obj);
    }
    
    private synchronized void node(org.json.JSONObject obj) throws JSONException {
        JSONArray root = obj.getJSONArray("data");
        final Translator nameTemplate = new Translator(Translator.Language.EN, Translator.Language.ES, Translator.NAME_TEMPLATE);
        final Translator descriptionTemplate = new Translator(Translator.Language.EN, Translator.Language.ES, Translator.DESCRIPTION_TEMPLATE);
        this.data = new ArrayList<DataInsights>();
        for (int i = 0; i < root.length(); ++i) {
            DataInsights pojoData = new DataInsights();
            org.json.JSONObject data = root.getJSONObject(i);
            String dataTitle = data.get("title").toString();
            String dataName =  Arrays.toString(nameTemplate.translate((String) data.get("name")));
            dataName = dataName.substring(1, dataName.length()-1);
            String period = data.get("period").toString();
            String description = Arrays.toString(descriptionTemplate.translate((String) data.get("name")));
            description = description.substring(1, description.length()-1);
            pojoData.setTitle(dataTitle);
            pojoData.setPeriod(period);
            pojoData.setDescription(description);
            pojoData.setName(dataName);
            JSONArray values = data.getJSONArray("values");
            List<String> valuesPojo = new ArrayList<String>();
            for (int k = 0; k < values.length(); k++) {
                org.json.JSONObject jObjValue = values.getJSONObject(k);
                Object value = jObjValue.get("value");
                Object endTime = "";
                try {
                    endTime = jObjValue.get("end_time");
                } catch(Exception e) {}
                if(value.toString().charAt(0) == '{') {
                    String objectValue = value.toString();
                    objectValue = objectValue.substring(1, objectValue.length()-1);
                    List<String> finalValues = csvLine(objectValue);
                    for(String v : finalValues) {
                        valuesPojo.add(v);
                    }
                } else {
                   valuesPojo.add(value.toString());
                    try {
                        if (!endTime.toString().isEmpty()) {
                        valuesPojo.add(endTime.toString());
                        }
                    } catch(Exception e) {} 
                    
                }
                pojoData.setValues(valuesPojo);
            }
            this.data.add(pojoData);
        }
    }
    
    
    
    
    
}