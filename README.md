### feature_extraction  文本特征提取算法实现-java
使用java，实现了卡方校验（chi-square）和信息增益算法提取文本特征算法  
### 包说明  
### 1. com.ivan.sentiment.beans  
特征提取中使用到的java bean，对文档对象的封装  
### 2. com.ivan.sentiment.feature_extraction  
核心算法包，包含特征提取接口、抽象类以及特征提取实现类  
### 3. com.ivan.sentiment.utils  
工具类  
### 4. com.ivan.sentiment.main  
测试类  
### 使用说明  
    //创建特征提取对象，可以使用卡方校验或者信息增益两种方法
    IFeatureExtraction featureExtraction = new CHIFeatureExtractionImpl();
    //IFeatureExtraction featureExtraction = new IGFeatureExtractionImpl();
    //构造样本集 map的key是样本的标签，value 是对应的每个类别下的样本分词结果
    Map<String,List<List<String>>> datas = new HashMap<String, List<List<String>>>();
    //调用特征提取方法  第二个参数特征的个数，也可以不写，默认是300
    List<String> features = featureExtraction.featureExtraction(datas, 500);  

### 原理参考  
[文本特征词提取算法](http://www.cnblogs.com/chenying99/p/5018196.html)  
[文本分类入门（十一）特征选择方法之信息增益](http://www.blogjava.net/zhenandaci/archive/2016/08/11/261701.html#431534)
