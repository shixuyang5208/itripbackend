package cn.itrip.dao;

import cn.itrip.Constant;
import cn.itrip.StreamTool;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.util.List;


public class BaseQuery<T> {
    private HttpSolrClient httpSolrClient;
    static Logger logger = Logger.getLogger(BaseQuery.class);

    /**
     * 初始化httpsolrclient
     * @param url
     */
    public BaseQuery(String url){
        httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser());
        httpSolrClient.setConnectionTimeout(500);
    }

    /**
     * 酒店分页SolrQuery
     * @param pageSize
     * @param pageNum
     * @param query
     * @param classs
     * @return
     */
    public Page<T> queryPage(SolrQuery query,Integer pageSize, Integer pageNum,Class classs)throws Exception{

        Integer row = EmptyUtils.isEmpty(pageSize)? Constants.DEFAULT_PAGE_SIZE :pageSize;
        Integer currentPage = EmptyUtils.isEmpty(pageNum)? (Constants.DEFAULT_PAGE_NO -1):pageNum-1;
        Integer startRow = currentPage*row;
        //页起始行数
        query.setStart(startRow);
        //页容
        query.setRows(row);
        //使用query条件执行查询
        QueryResponse queryResponse =  httpSolrClient.query(query);
        //查询到的数据放入结果集
        SolrDocumentList solrDocumentList =queryResponse.getResults();
        //给分页工具传入构造参数
        Page<T> page = new Page<T>(currentPage+1,query.getRows(),new Long(solrDocumentList.getNumFound()).intValue());
        //自动将值封装到查询的实体类
        List<T> list = queryResponse.getBeans(classs);
        //使用page工具进行分页
        page.setRows(list);
        return page;
    }

    public List<T> queryList(SolrQuery query,Integer pageSize,Class classs)throws Exception {
        query.setStart(0);
        Integer row = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        //页容
        query.setRows(row);
        //使用query条件执行查询
        QueryResponse queryResponse = httpSolrClient.query(query);
        //查询到的数据放入结果集
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        //自动将值封装到查询的实体类
        List<T> list = queryResponse.getBeans(classs);

        return list;
    }
}
