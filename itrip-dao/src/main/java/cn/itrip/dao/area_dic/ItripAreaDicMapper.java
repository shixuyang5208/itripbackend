package cn.itrip.dao.area_dic;

import cn.itrip.beans.pojo.ItripAreaDic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItripAreaDicMapper {
    /**
     * 新增区域信息
     * @param itripAreaDic 区域实体
     * @return 受影响行数
     */
    public Integer insertItripAreaDic(ItripAreaDic itripAreaDic);

    /**
     * 删除
     * @param id 主键ID
     * @return 受影响行数
     */
    public Integer deleteItripAreaDicById(@Param(value = "id") Long id);

    /**
     * 修改区域信息
     * @param itripAreaDic 实体
     * @return 受影响行数
     */
    public Integer updateItripAreaDic(ItripAreaDic itripAreaDic);

    /**
     * 通过ID查询实体信息
     * @param id 主键
     * @return 实体
     */
    public ItripAreaDic getItripAreaDicById(@Param(value = "id") Long id);

    /**
     * 通过多个信息查询实体
     * @param param 查询所需参数集合
     * @return 实体集合
     */
    public List<ItripAreaDic> getItripAreaDicByMap(Map<String, Object> param);

    /**
     * 查询总数
     * @param param 参数集合
     * @return 总数
     */
    public Integer getItripAreaDicCountByMap(Map<String, Object> param);
}
