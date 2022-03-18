package cn.itrip.dao.itripUserLinkUser;
import cn.itrip.pojo.ItripLabelDic;
import cn.itrip.pojo.ItripUserLinkUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripUserLinkUserMapper {

	public ItripUserLinkUser getItripUserLinkUserById(@Param(value = "id") Long id)throws Exception;
	//查询
	public List<ItripUserLinkUser>	selectuser()throws Exception;
	//添加
	public Integer insertuser(ItripUserLinkUser itripUserLinkUser)throws Exception;
	//删除
	public Integer deluser(@Param("id") Long id)throws Exception;
	//修改
	public Integer upduser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	public List<ItripUserLinkUser>	getItripUserLinkUserListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripUserLinkUserCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	public Integer updateItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	public Integer deleteItripUserLinkUserById(@Param(value = "id") Long id)throws Exception;
}
