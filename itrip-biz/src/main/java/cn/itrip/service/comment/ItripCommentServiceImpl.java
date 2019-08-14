package cn.itrip.service.comment;

import cn.itrip.beans.pojo.ItripComment;
import cn.itrip.beans.pojo.ItripImage;
import cn.itrip.beans.vo.comment.ItripListCommentVO;
import cn.itrip.beans.vo.comment.ItripScoreCommentVO;
import cn.itrip.common.Constants;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.Page;
import cn.itrip.dao.comment.ItripCommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ItripCommentServiceImpl implements ItripCommentService {
    @Resource
    private ItripCommentMapper itripCommentMapper;
    @Resource
    private ItripComment itripComment;
    @Resource
    private ItripScoreCommentVO itripScoreCommentVO;

    @Override
    public ItripComment getItripCommentById(Long id) throws Exception {
        return itripCommentMapper.getItripCommentById(id);
    }

    @Override
    public List<ItripListCommentVO> getItripCommentListByMap(Map<String, Object> param) throws Exception {
        return itripCommentMapper.getItripCommentListByMap(param);
    }

    @Override
    public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception {
        return itripCommentMapper.getItripCommentCountByMap(param);
    }

    @Override
    public boolean itriptxAddItripComment(ItripComment obj, List<ItripImage> itripImages) throws Exception {

        return false;
    }

    @Override
    public Integer itriptxModifyItripComment(ItripComment itripComment) throws Exception {
        itripComment.setModifyDate(new Date());
        return itripCommentMapper.updateItripComment(itripComment);
    }

    @Override
    public Integer itriptxDeleteItripCommentById(Long id) throws Exception {
        return itripCommentMapper.deleteItripCommentById(id);
    }

    @Override
    public Page<ItripListCommentVO> queryItripCommentPageByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        Integer total = itripCommentMapper.getItripCommentCountByMap(param);
        pageNo = EmptyUtils.isEmpty(pageNo) ? Constants.DEFAULT_PAGE_NO : pageNo;
        pageSize = EmptyUtils.isEmpty(pageSize) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        Page page = new Page(pageNo, pageSize, total);
        param.put("beginPos", page.getBeginPos());
        param.put("pageSize", page.getPageSize());
        List<ItripListCommentVO> itripCommentList = itripCommentMapper.getItripCommentListByMap(param);
        page.setRows(itripCommentList);
        return page;
    }

    @Override
    public ItripScoreCommentVO getAvgAndTotalScore(Long hotelId) throws Exception {
        return itripCommentMapper.getCommentAvgScore(hotelId);
    }
}
