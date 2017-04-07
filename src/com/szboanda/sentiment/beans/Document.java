package com.szboanda.sentiment.beans;

import java.util.Map;

/**
 *  
 * @ClassName: Document
 * @Description: 文档类
 * <p>Company: 深圳市博安达信息技术股份有限公司</p> 
 * @author zhaoyifan
 * @date 2017年3月3日 上午10:54:40
 */
public class Document {
	/**文档分词，以及词频*/
	private Map<String,Double> wordFrequency;
	/**文档所属类别*/
	private String className;
	/**文章总单词数量*/
	private int countOfWords;
	
	public int getCountOfWords() {
		return countOfWords;
	}
	public void setCountOfWords(int countOfWords) {
		this.countOfWords = countOfWords;
	}
	public Map<String, Double> getWordFrequency() {
		return wordFrequency;
	}
	public void setWordFrequency(Map<String, Double> wordFrequency) {
		this.wordFrequency = wordFrequency;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}  
	
	

}
