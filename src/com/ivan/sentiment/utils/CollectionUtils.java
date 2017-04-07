package com.ivan.sentiment.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * * 
 * @ClassName: CollectionUtils 
 * @Description: 集合工具类
 * @author zhaoyifan
 * @date 2017年3月3日 下午2:10:29
 */
public class CollectionUtils {
	
	/**
	 * 按照map的value 排序，value为double类型
	 * @author zhaoyifan
	 * @param oriMap 待排序map
	 * @param ascending true 为升序，false为降序
	 * @see 
	 * @return List<Map.Entry<String,Double>> 返回排序后的entry集合
	 */
	public static List<Map.Entry<String,Number>> sortMapByValue(Map<String,? extends Number> oriMap,final boolean ascending){
		if(oriMap == null || oriMap.size() ==0)
			return null;
		//对map的value值排序
		ArrayList<Entry<String, Number>> arrayList = new ArrayList<Entry<String, Number>>((Collection<? extends Entry<String, Number>>) oriMap.entrySet());
		//jdk 7sort有可能报错，  
        //加上这句话:System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");  
        //表示，使用以前版本的sort来排序  
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); 
		Collections.sort(arrayList, new Comparator<Entry<String, ? extends Number>>() {

			@Override
			public int compare(Entry<String, ? extends Number> o1, Entry<String, ? extends Number> o2) {
				// TODO Auto-generated method stub
				if(ascending)
					return ((o2.getValue().doubleValue()-o1.getValue().doubleValue())==0 ? 0
							:(o2.getValue().doubleValue()-o1.getValue().doubleValue()>0) ? -1
							: 1);
				else
					return ((o2.getValue().doubleValue()-o1.getValue().doubleValue())==0 ? 0
							:(o2.getValue().doubleValue()-o1.getValue().doubleValue()>0) ? 1
							: -1);
				
			}
		});
		
		return arrayList;
		
	}
	
}
