package com.szboanda.sentiment.beans;

import java.util.HashMap;
import java.util.Map;
/**
 * * 
 * @ClassName: FeatureDocument 
 * @Description: 特征文档类，提取完特征词后  重新以特征词填充的每一个文档
 * <p>Company: 深圳市博安达信息技术股份有限公司</p> 
 * @author zhaoyifan
 * @date 2017年3月6日 下午2:37:26
 */
public class FeatureDocument {
	private Map<String, Double> words ;
	private String className ;
	
	public FeatureDocument() {
		super();
		words = new HashMap<String, Double>();
	}
	public Map<String, Double> getWords() {
		return words;
	}
	public void setWords(String feature,Double value) {
		words.put(feature, value);
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	
}
