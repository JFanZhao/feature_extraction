package com.szboanda.sentiment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.szboanda.sentiment.config.ProjectConfig;
import com.szboanda.sentiment.feature_extraction.IFeatureExtraction;
import com.szboanda.sentiment.feature_extraction.impl.ChiSquareFeatureExtractionImpl;
import com.szboanda.sentiment.feature_extraction.impl.IGFeatureExtractionImpl;
import com.szboanda.sentiment.utils.CollectionUtils;
import com.szboanda.sentiment.utils.FileUtils;
import com.szboanda.sentiment.utils.StopWordsHandler;
import com.szboanda.sentiment.utils.TextProcess;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Map<String, Integer> myMap = new HashMap<String, Integer>();  
        myMap.put("1", 1);  
        myMap.put("2", 4);  
        myMap.put("3", 3);  
        myMap.put("4", 9);  
        myMap.put("5", 6);  
        myMap.put("6", 2);
//        ArrayList<Entry<String, Double>> arrayList = new ArrayList<Entry<String, Double>>(myMap.entrySet());
//        Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>() {
//
//			@Override
//			public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
//				// TODO Auto-generated method stub
//				return ((o2.getValue()-o1.getValue()==0) ? 0
//						: (o2.getValue()-o1.getValue()>0) ? 1
//						: -1);
//			}
//        	
//		});
        
//        List<Entry<String,Number>> arrayList = CollectionUtils.sortMapByValue(myMap, false);
        
//        for (Entry<String,Number> entry : arrayList) {  
//            System.out.println(entry.getKey() + "\t" + entry.getValue().doubleValue());  
//        }
    	String corpusPath=ProjectConfig.properties.getProperty("text.sentiment.datasets");
    	List<String> fileClassifications;//语料库中类别集合，主要有多少个类别
    	File corpusDirectory;//语料库存储的根目录
    	corpusDirectory= new File(corpusPath);
		if(!corpusDirectory.isDirectory()){
			throw new IllegalArgumentException("训练语料库搜索失败。");
		}
		fileClassifications=Arrays.asList(corpusDirectory.list());  //分类类别，文件夹的个数
		Map<String,List<String>> samples = new HashMap<String, List<String>>();
		for(String classification : fileClassifications){
			List<String> filePaths = FileUtils.getFileNames(corpusDirectory,classification);
			List<String> s = new ArrayList<String>();
			for(String filePath : filePaths){//遍历类别下的每一个文件
				//分词并提取特征
				String word = FileUtils.readAsString(filePath,"UTF-8");
				s.add(word.trim());
			}
			samples.put(classification, s);
		}
		System.out.println(samples.keySet());
		String customDicPath = ProjectConfig.properties.getProperty("text.sentiment.customDic");
		List<String> customDics = FileUtils.readLines(customDicPath, "UTF-8");
		TextProcess textProcess = new TextProcess();
		IFeatureExtraction featureExtraction = new ChiSquareFeatureExtractionImpl();
//		IFeatureExtraction featureExtraction = new IGFeatureExtractionImpl();

		Map<String,List<List<String>>> datas = new HashMap<String, List<List<String>>>();
		for (Entry<String, List<String>> entry : samples.entrySet()) {
			List<List<String>> arrayList2 = new ArrayList<List<String>>();
			for (String line : entry.getValue()) {
				List<String> words = textProcess.standardSegment2List(line);
				List<String> t = new ArrayList<String>();
				for (String word : words) {
					if(!StopWordsHandler.IsStopWord(word))
						t.add(word);
					else 
						break;
				}
				arrayList2.add(t);
				System.out.println(t);
				System.out.println("words:"+words);
			}
			datas.put(entry.getKey(), arrayList2);
		}
		
		List<String> features = featureExtraction.featureExtraction(datas, 500);
		System.out.println(features);
    }
}
