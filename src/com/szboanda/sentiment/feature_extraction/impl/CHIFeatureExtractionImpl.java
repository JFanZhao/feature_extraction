package com.szboanda.sentiment.feature_extraction.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.szboanda.sentiment.beans.Document;
import com.szboanda.sentiment.beans.FeatureDocument;
import com.szboanda.sentiment.feature_extraction.AbstractFeatureExtractionImpl;

public class CHIFeatureExtractionImpl extends AbstractFeatureExtractionImpl {
	/**好汉每个词条的文档的个数*/
	Map<String, Double> countOfDocsContainWord = new HashMap<String, Double>();
	/**
	 * 提取样本中的特征词
	 * @author 赵一帆
	 * @param samples 样本 
	 * 		  map的key是样本的labels（即分类标签，例如：position和negation）
	 * 		  value	是样本的集合以及各个样本分词后的集合
	 * @param featureNum 特征词个数
	 * @see 原理说明：
	 * 		分别计算每个单词在每个label类别下的卡方值，最后取最大值作为该词条的卡方值
	 * @return List<String> 返回特征词集合
	 */
	@Override
	public List<String> featureExtraction(Map<String, List<List<String>>> samples, int featureNum) {
		// TODO Auto-generated method stub
		initVariable(samples);
		//计算卡方值
		calcValuesForWord();
		//获取TopN特征词
		getTopNFeatures(featureNum);
		return features;
	}

	/**
	 * 计算每一个单词在所有类别中的卡方值，取最大的值作为该词条最后的卡方值
	 * @author 赵一帆
	 * @see 
	 * @return
	 */
	@Override
	public void calcValuesForWord() {
		//计算每一个单词在所有类别中的卡方值，取最大的值作为该词条最后的卡方值
		for(String word : allWords){
			//公式  χ2i=max((A_i*D_i−B_i*C_i)2/((A_i+B_i)(C_i+D_i)))
        	double maxCHI = 0.0;//词条word在所有类别中最大的卡方值
        	for (String label : labels) {
        		double A = 0;//属于类别label，且包含词条word的文档数量
        		double B = 0;//包含词条word，不属于类别label的文档数量
        		double C = 0;//不包含词条word，属于类别label的文档数量
        		double D = 0;//不包含词条word，不属于类别label的文档数量
        		
        		//循环每一个文档，计算，A、B、C、D的值
        		for (Document document : documents) {
					
        			if(document.getClassName().equals(label)){//属于类别label的文档
        				if(document.getWordFrequency().containsKey(word))//当前文档属于类别label，且文档包含词条word
        					A += 1;
        				else//当前文档属于类别label，但不包含词条word
        					C += 1;
        			}else{//该文档不属于类别label
        				if(document.getWordFrequency().containsKey(word))//当前文档不属于类别label，但包含词条word
        					B += 1;
        				else//当前文档不属于类别label，且不包含词条word
        					D += 1;
        			}
				}
        		//计算卡方值
        		//公式  χ2i=max((A_i*D_i−B_i*C_i)2/((A_i+B_i)(C_i+D_i)))
        		double tempCHI = ((A*D-B*C)*(A*D-B*C))/((A+B)*(C+D));
        		if(tempCHI > maxCHI)
        			maxCHI = tempCHI;
			}
        	//存储计算完成的卡方值
        	featureValueMap.put(word, maxCHI);
		}
	}


	@Override
	public List<FeatureDocument> fillDocumentWithFeatures() {
		//计算每个词条在每个类别下的总数
		for (String word : allWords) {
			double count = 0.0;
			for (Document document : documents) {
				if(document.getWordFrequency().containsKey(word))
					count += 1;
			}
			countOfDocsContainWord.put(word, count);
		}
		//重新设置样本
		for (Document document : documents) {
			FeatureDocument featureDocument = new FeatureDocument();
			featureDocument.setClassName(document.getClassName());//类别
			//循环每一个特征词，如果该文档包含该特征词，则设置词频不变；否则将词频设置为0
			for (String feature : features) {
				if(document.getWordFrequency().containsKey(feature)){
					featureDocument.setWords(feature, (document.getWordFrequency().get(feature)/document.getCountOfWords())*countOfDocsContainWord.get(feature));
				}else
					featureDocument.setWords(feature, 0.0);
			}
		}
		return featureDocuments;
	}

	
	
}
