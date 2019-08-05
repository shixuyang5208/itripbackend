package cn.itrip.dao;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrClient;


public class BaseQuery<T> {
    private HttpSolrClient httpSolrClient;
    static Logger logger = Logger.getLogger(BaseQuery.class);
}
