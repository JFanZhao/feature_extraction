package com.szboanda.sentiment.feature_extraction.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.szboanda.sentiment.beans.Document;
import com.szboanda.sentiment.beans.FeatureDocument;
import com.szboanda.sentiment.feature_extraction.AbstractFeatureExtractionImpl;
import com.szboanda.sentiment.feature_extraction.IFeatureExtraction;
import com.szboanda.sentiment.utils.CollectionUtils;
/**
 *  
 * @ClassName: IGFeatureExtractionImpl 
 * @Description: 使用信息增益方法提取TopN特征词
 * <p>Company: 深圳市博安达信息技术股份有限公司</p> 
 * @author zhaoyifan
 * @date 2017年3月3日 上午11:07:26
 */
public class IGFeatureExtractionImpl extends AbstractFeatureExtractionImpl{
	/**记录每个类别下的文档数量*/
	private Map<String, Integer> countOfClassifyDocument = new HashMap<String, Integer>();  
	/**
	 * 提取样本中的特征词
	 * @author 赵一帆
	 * @param samples 样本 
	 * 		  map的key是样本的labels（即分类标签，例如：position和negation）
	 * 		  value	是样本的集合以及各个样本分词后的集合
	 * @param featureNum 特征词个数
	 * @see 原理说明：
	 * 		分别计算每个单词在每个label类别下的信息增益值
	 * @return List<String> 返回特征词集合
	 */
	@Override
	public List<String> featureExtraction(Map<String, List<List<String>>> samples, int featureNum) {
		// TODO Auto-generated method stub
		//初始化变量
		initVariable(samples);
		//计算每个类别的文档数量
		for (Entry<String, List<List<String>>> entry : samples.entrySet()) {
			countOfClassifyDocument.put(entry.getKey(), entry.getValue().size());//设置类别下的文档总数
		}
		//计算每一个单词的信息增益
		calcValuesForWord();
		//获取TopN特征
		getTopNFeatures(featureNum);
		return features;
	}
	/**
	 * 计算每个单词的信息增益，得到所有单词信息增益集合
	 * @author 赵一帆
	 * @see 
	 * @return
	 */
	@Override
	public void calcValuesForWord() {
		//循环计算每个单词的信息增益值
		for(String word:allWords){
			double Pc_i;     //类别ci在样本中出现的概率
        	double Pt = 0.0; //样本中包含词条t的文档的概率（意思是包含t的文档的概率）
        	double nPt;      //样本中包含词条t的文档的概率
        	double Pc_i_t;   //文档中包含词条t，且文档属于ci类别的概率
        	double Pc_i_nt;  //文档中不包含词条t，且文档属于ci类别的概率
        	double countOfDocumentContainT = 0.0; //样本中包含词条t的文档总数
        	//分别临时存放信息增益计算公式中的三个代数式的和
        	double sum1 = 0.0;
        	double sum2 = 0.0;
        	double sum3 = 0.0;
        	
        	//按照公式计算词条信息增益
        	//计算信息增益公式中第一个代数式  即  -sum(Pc_i*log(Pc_i)) ci是类别，Pc_i是i类别文档的概率
        	for (String label : labels) {
        		Pc_i = (double)countOfClassifyDocument.get(label)/(double)totalDocumentCount; //计算计算类别i的概率
        		sum1 += (0 - Pc_i * Math.log(Pc_i));//求和
			}
        	
        	//计算后两个代数式，即 P(t)*(sum(Pc_i_t * log(Pc_i_t))) + nP(t)*(sum(nPc_i_t * log(nPc_i_t)))
        	
        	//计算包含词条t的样本文档总数
        	for (Document document : documents) {
				if(document.getWordFrequency().containsKey(word))
					countOfDocumentContainT += 1;
			}
        	
        	Pt = countOfDocumentContainT/totalDocumentCount; //样本中包含词条t的概率
        	nPt = 1 - Pt; //样本中不包含词条t的概率
        	
        	//计算属于类别i且包含词条t以及属于类别i但不含词条t的样本文档数
        	for (String label : labels) {
        		double inCount = 0.0;//属于类别i且包含词条t的样本文档数
        		double outCount = 0.0;//属于类别i，不包含词条t的样本文档数
        		for (Document document : documents) {
    				if(document.getClassName().equals(label)){
    					if(document.getWordFrequency().containsKey(word))
    						inCount += 1.0;
    					else
    						outCount += 1.0;
    				}
    			}
        		
        		Pc_i_t = inCount/countOfDocumentContainT; //包含词条t，且属于类别i的文档概率
        		Pc_i_nt = outCount/(totalDocumentCount-countOfDocumentContainT);//不包含词条t，且属于类别i的文档概率
        		sum2 = sum2 + Pc_i_t * Math.log(Pc_i_t);  //计算该类别下的包含词条t的公式值，并累加到和
        		sum3 = sum3 + Pc_i_nt * Math.log(Pc_i_nt);//计算该类别下的不包含词条t的公式值，并累加到和
			}
        	sum2 = sum2 * Pt;
        	sum3 = sum3 * nPt;
        	double result = sum1 + sum2 + sum3;  //信息增益三个代数式的和
        	featureValueMap.put(word, result);//计算完词条t的信息增益，加入到集合中
		}
	}
	@Override
	public List<FeatureDocument> fillDocumentWithFeatures() {
		// TODO Auto-generated method stub
		for (Document document : documents) {
			FeatureDocument featureDocument = new FeatureDocument();
			featureDocument.setClassName(document.getClassName());//类别
			//循环每一个特征词，如果该文档包含该特征词，则设置词频不变；否则将词频设置为0
			for (String feature : features) {
				if(document.getWordFrequency().containsKey(feature)){
					featureDocument.setWords(feature, document.getWordFrequency().get(feature));
				}else
					featureDocument.setWords(feature, 0.0);
			}
		}
		return featureDocuments;
	}
	

}
