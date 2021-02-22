package com.xy.sql.parse.fragment;

import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.List;
import java.util.Map;

public interface IFragment {

	List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap);
}
