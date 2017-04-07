package com.ivan.sentiment.feature_extraction;

import java.util.List;
import java.util.Map;

import com.ivan.sentiment.beans.FeatureDocument;

/**
 * * 
 * @ClassName: IFeatureExtraction 
 * @Description: 提取特征词接口
 * @author zhaoyifan
 * @date 2017年3月2日 上午10:19:18
 */
public interface IFeatureExtraction {
	
	public List<String> featureExtraction(Map<String,List<List<String>>> samples, int featureNum);

	/**
	 * 使用默认特征词个数，默认提取300个特征词
	 * @param samples  样本数据集
	 * @return 返回特征词集合
     */
	public List<String> featureExtraction(Map<String,List<List<String>>> samples);
	/**
	 * 重新填充文档，返回特征词向量组成的样本
	 * @author zhaoyifan
	 * @see ，如果该文档包含该特征词，则设置词频不变；否则将词频设置为0
	 * @return 新的样本集合
	 */
	public List<FeatureDocument> fillDocumentWithFeatures();
}
