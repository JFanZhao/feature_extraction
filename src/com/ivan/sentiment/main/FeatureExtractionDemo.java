package com.ivan.sentiment.main;

import com.ivan.sentiment.feature_extraction.IFeatureExtraction;
import com.ivan.sentiment.feature_extraction.impl.CHIFeatureExtractionImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: com.ivan.sentiment.main
 * @Auther: ivan zhao
 * @Description: 测试类
 * @Date: 2017/4/7 12:25
 */

public class FeatureExtractionDemo {
    public static void main(String[] args) {
        //创建特征提取对象，可以使用卡方校验或者信息增益两种方法
        IFeatureExtraction featureExtraction = new CHIFeatureExtractionImpl();
//		IFeatureExtraction featureExtraction = new IGFeatureExtractionImpl();
        //构造样本集 map的key是样本的标签，value 是对应的每个类别下的样本分词结果
        Map<String,List<List<String>>> datas = new HashMap<String, List<List<String>>>();
        //调用特征提取方法  第二个参数特征的个数，也可以不写，默认是300
        List<String> features = featureExtraction.featureExtraction(datas, 500);
        System.out.println(features);
    }
}
