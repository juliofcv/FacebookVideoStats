/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.artec.apps.facebook;

import edu.artec.apps.facebook.insights.VideoInsights;
import edu.artec.apps.facebook.reports.ExcelReport;
import edu.artec.apps.facebook.pojo.DataInsights;
import edu.artec.apps.facebook.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author imstu
 */
public class EjecutarVideo {
    
    
    private static final String ACCESS_TOKEN = "EAAellZBxk25cBAF1p3vDootYm5ZAQaZCm7UvdqM8z7hYZAdIRRTHDAa0leZBuSml8BUbs9R1U9jqoHfpoHJ111m3JgvMmeLeuZCpLv9O668ADwvtZBZBa9yYZBid0BwmDArbzS1nLdyIPopaJNy2j5F2RU1Wn1xdzh11ARg75McrRZCrjqYJmpW2lqdAtaf1BuWPoZD";
    private static final String API_VERSION = Config.API_VERSION;

    
    //https://developers.facebook.com/docs/graph-api/reference/v3.2/insights
    /**
     * 
     * @param args
     * @throws Exception 
     */
    public static void main(String... args) {
        //String idPage = "115026288528038";
        String vdo0 = "https://www.facebook.com/bralbeatsbeatbox/videos/1669572113151754/";
                    // https://www.facebook.com/bralbeatsbeatbox/videos/1762544720521159/
        List<String> metric = new ArrayList<>();
        
        metric.add("post_video_views_10s");
        
        
        String metrics = "";
        for(String m:metric)
            metrics = metrics +m+",";
        metrics = StrUtils.removeLastCharacterAt(metrics, ',');
        VideoInsights vi = new VideoInsights(API_VERSION, ACCESS_TOKEN, vdo0, metrics);
        
        List<DataInsights> dataVideo = vi.getData();
        List<Object[]> ExcelData = new ArrayList<>();
        for (DataInsights data: dataVideo) {
            List<String> values = data.getValues();
            for (String value: values) {
                if (data.getPeriod().equals("lifetime")) {
                    List<String> separing = StrUtils.separing(value);
                    Object[] dataS;
                    if(separing.size() > 1) {
                        String dat = separing.get(0);
                         dataS = new Object[]{data.getName(), dat.substring(1,dat.length()), separing.get(1), data.getDescription()};
                    } else{
                        dataS = new Object[]{data.getName(), separing.get(0), data.getDescription()};
                    }
                    ExcelData.add(dataS);
                }
            }
        }
        try {
            ExcelReport.create("D:\\SANCARLOS.xlsx","ADS GUATE REPORTE",ExcelData);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
}
