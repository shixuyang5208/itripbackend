package cn.itrip.service;

import cn.itrip.beans.vo.hotel.ItripHotelVO;
import cn.itrip.beans.vo.hotel.SearchHotelVO;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.common.PropertiesUtils;
import cn.itrip.dao.BaseQuery;
import org.apache.solr.client.solrj.SolrQuery;

import java.nio.Buffer;
import java.util.List;

/*
solr搜索酒店实现类
 */
public class SearchHotelServiceImpl implements SearchHotelService {

    public static String URL = PropertiesUtils.get("database.properties","baseUrl");
    private BaseQuery<ItripHotelVO> baseQuery = new BaseQuery<ItripHotelVO>(URL);

    /**
     *多条件分页查询酒店
     * @param searchHotelVO
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<ItripHotelVO> searchHotelPage(SearchHotelVO searchHotelVO, Integer pageNum, Integer pageSize) throws Exception {
        SolrQuery solrQuery = new SolrQuery("*:*");
        StringBuffer tempQuery = new StringBuffer();
        int tempFlag=0;
        if (EmptyUtils.isNotEmpty(searchHotelVO){
            //目的地
            if (EmptyUtils.isNotEmpty(searchHotelVO.getDestination())){
                tempQuery.append(" destination :"+ searchHotelVO.getDestination());
                tempFlag = 1;
            }
            //酒店等级
            if (EmptyUtils.isNotEmpty(searchHotelVO.getHotelLevel())){
                solrQuery.addField("hotelLevle:"+searchHotelVO.getHotelLevel()+"");
            }
            //关键词
            if (EmptyUtils.isNotEmpty(searchHotelVO.getKeywords())){
                if (tempFlag == 1){
                    tempQuery.append(" AND keyword :"+searchHotelVO.getKeywords());
                } else {
                    tempQuery.append(" keyword :"+searchHotelVO.getKeywords());
                }
            }

            //酒店特色
            if (EmptyUtils.isNotEmpty(searchHotelVO.getFeatureIds())){
                StringBuffer buffer = new StringBuffer("(");
                int flag = 0;
                String featureIdArray[] = searchHotelVO.getFeatureIds().split(",");
                for (String featureId : featureIdArray){
                    if (flag == 0){
                        buffer.append(" featureIds:"+"*,"+featureId+",*");
                    }else {
                        buffer.append(" OR featureIds:" + featureId + ",*");
                    }
                    flag++;
                }
                buffer.append(")");
                solrQuery.addFilterQuery(buffer.toString());
            }
        }
        return null;
    }

    @Override
    public List<ItripHotelVO> searchHotelByHotCity(Integer cityId, Integer pageSize) throws Exception {
        //jgj
        return null;
    }
}
