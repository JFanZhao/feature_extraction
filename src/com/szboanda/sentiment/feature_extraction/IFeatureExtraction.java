package com.szboanda.sentiment.feature_extraction;

import java.util.List;
import java.util.Map;

import com.szboanda.sentiment.beans.FeatureDocument;

/**
 * * 
 * @ClassName: IFeatureExtraction 
 * @Description: 提取特征词接口
 * <p>Company: 深圳市博安达信息技术股份有限公司</p> 
 * @author zhaoyifan
 * @date 2017年3月2日 上午10:19:18
 */
public interface IFeatureExtraction {
	
	public List<String> featureExtraction(Map<String,List<List<String>>> samples, int featureNum);
	/**
	 * 重新填充文档，返回特征词向量组成的样本
	 * @author 赵一帆
	 * @see ，如果该文档包含该特征词，则设置词频不变；否则将词频设置为0
	 * @return 新的样本集合
	 */
	public List<FeatureDocument> fillDocumentWithFeatures();
}
