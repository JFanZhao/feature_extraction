package com.ivan.sentiment.feature_extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ivan.sentiment.beans.Document;
import com.ivan.sentiment.beans.FeatureDocument;
import com.ivan.sentiment.utils.CollectionUtils;

/**
 * *
 * @ClassName: AbstractFeatureExtractionImpl
 * @Description: 提取特征词抽象类，包含公用的属性值
 * @author zhaoyifan
 * @date 2017年3月2日 上午10:19:18
 */
public abstract class AbstractFeatureExtractionImpl implements IFeatureExtraction {

	/**存储文档*/
	protected List<Document> documents = new ArrayList<Document>();
	/**记录所有词的信息增益值，按信息增益值排序后提取TopN作为特征词*/
	protected Map<String, Double> featureValueMap = new HashMap<String, Double>();    
	/**存储样本中所有不重复的单词*/
	protected Set<String> allWords = new HashSet<String>();                   
	/**提取TopN后的特征词*/
	protected List<String> features = new ArrayList<String>();
	/**提取的特征词数量*/
	protected  int featureNum = 300;
	/**样本（文档）总数*/
	protected int totalDocumentCount = 0;
	/**样本标签*/
	protected Set<String> labels = new HashSet<String>();
	/**存储文档以特征词填充后的特征向量文档*/
	protected List<FeatureDocument> featureDocuments = new ArrayList<FeatureDocument>();
	
	@Override
	public abstract List<String> featureExtraction(Map<String, List<List<String>>> samples, int featureNum);
	public abstract void calcValuesForWord();
	@Override
	public abstract List<FeatureDocument> fillDocumentWithFeatures();
	
	/**
	 * 初始化文档词频、类别文档数、样本总数以及不重复的单词
	 * @author 赵一帆
	 * @param samples 样本 
	 * @see 
	 * @return
	 */
	protected void initVariable(Map<String, List<List<String>>> samples) {
		//循环样本数据
		for (Entry<String, List<List<String>>> entry : samples.entrySet()) {
			List<List<String>> classes = entry.getValue();//当前类别下文档集合
			String label = entry.getKey();
			totalDocumentCount += classes.size(); //总文档数
			labels.add(label);//设置样本类别标签
			//循环此类别下每个文档，统计每个文档的词频，并创建Document对象，
			for(List<String> doc : classes){
				Document document = new Document();
				document.setClassName(label);//设置文档类标
				document.setCountOfWords(doc.size());//设置文档的总词数
				Map<String,Double> wordFrequency = new HashMap<String, Double>();
				//统计文档每个单词的词频
				for (String word : doc) {
					if(wordFrequency.containsKey(word))
						wordFrequency.put(word, wordFrequency.get(word) + 1);
					else
						wordFrequency.put(word, 1.0);
					//添加到不重复的set中
					allWords.add(word);
				}
				document.setWordFrequency(wordFrequency);//设置文档词频
				documents.add(document);//添加到documents中
				
			}
		}
	}
	/**
	 * 得到topN 特征词
	 * @author 赵一帆
	 * @see 
	 * @return
	 */
	protected void getTopNFeatures(){
		//排序，得到TopN的特征词
		List<Entry<String, Number>> sortedFeatures = CollectionUtils.sortMapByValue(featureValueMap, false);
		//获取TopN特征词
		int index = 0;
		for (Entry<String, Number> entry : sortedFeatures) {
			features.add(entry.getKey());
			index ++ ;
			if(index == featureNum)
				break ;
		}
	}

}
