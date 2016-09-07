package ccc;

import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Subscriber {
    static List<String> readFile(String filename) {

    	
        List<String> lines = new ArrayList<String>(); 
        String line; 
        try {
            InputStream fis = new FileInputStream(filename); 
        	// System.out.println("error out " + filename + "  " +  System.getProperty("user.dir")    ); 
            InputStreamReader isr = new InputStreamReader(fis); 
            BufferedReader br = new BufferedReader(isr); 

           
            
            while ((line = br.readLine()) != null) {

                lines.add(line);  
            }
            br.close();
            
        } catch (Exception e) {

            e.printStackTrace(); 
        } 

        return lines; 
    }
    
    static List<Element> getVideo(String url) {
    	List<Element> videoLinks = new ArrayList<Element>(); 
    	try {
    		Document doc = Jsoup.connect(url).get();
    		Element videoList = doc.getElementById("channels-browse-content-grid");
    		Elements links = videoList.getElementsByTag("a");
    		
    		for (Element e: links) {
    			if (e.attr("title") != "") videoLinks.add(e); 
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace(); 
    	}
    	return videoLinks; 
    }
    
    static Map<String, List<Element>> readVideos(List<String> channelUrls) {
    	Map<String, List<Element> > creator = new HashMap<String, List<Element> >(); 
    	
    	for (String s: channelUrls) {
    		String name = s.split("/")[s.split("/").length - 2];
    		creator.put(name, getVideo(s));
    		try { Thread.sleep(10 * 1000); } catch (InterruptedException e) {assert false; }
    	}
    	return creator; 
    	
    }
    
    public static void main(String[] args) {	
    	Map<String, List<Element> > vids = readVideos(readFile(args[0])); 	
    	List<String> videoNames = new ArrayList<String>(); 
    	
    	for (String s: vids.keySet()) {
    		for (Element e: vids.get(s)) {videoNames.add(e.attr("title"));}
    	}
    	
    	JList videos = new JList(videoNames.toArray()); 

    	JFrame frame = new JFrame("YT Subscriber");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setPreferredSize(new Dimension(800, 300));
    	
    	frame.getContentPane().add(new JScrollPane(videos)); 	
    	
    	frame.pack();
    	frame.setVisible(true);
    	
    }
    
    
}
