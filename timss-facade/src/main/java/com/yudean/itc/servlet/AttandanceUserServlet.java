package com.yudean.itc.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.yudean.itc.SecurityBeanHelper;
import com.yudean.itc.code.StatusCode;
import com.yudean.itc.dto.Page;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.ISecurityMaintenanceManager;
import com.yudean.itc.util.Constant;

public class AttandanceUserServlet  extends BaseServlet {
	private static final long serialVersionUID = -1154238778492470100L;
	private ISecurityMaintenanceManager secManager;
	private static Logger logger = Logger.getLogger(AttandanceUserServlet.class);
	
	public void init() throws ServletException {             
		super.init();     
		secManager = getMtManager();
	} 
	
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if (method == null) {
			return;
		}
		HttpSession session = request.getSession();
		if (method.equals("getusers")) {
			getUsers(request, response);
		} else if (method.equals("getuser")) {
			getUser(request, response, session);
		} else if (method.equals("edit")) {
			editUser(request, response);
		}		
	}
	
	private void getUsers(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String filter = trimStrToNull(request.getParameter("filter"));
		String filterType = trimStrToNull(request.getParameter("filterType"));
		// 分页器
		Page<SecureUser> page = getPager(request);
		SecureUser operator = (SecureUser) request.getSession().getAttribute(
				Constant.secUser);
		Page<SecureUser> qResult = null;
		
		//强制过滤被禁用的用户
		page.setParameter("userStatus", StatusCode.YES);
		
		if(filter!=null&&filterType!=null){
			if(filterType.equals("org")){
				//按组织过滤
				page.setParameter("orgCode", filter);
			}
			else if(filterType.equals("person")){
				//按姓名搜索
				filter = URLDecoder.decode(filter, "UTF-8");
				page.setParameter("searchBy", filter);
			}
		}
		qResult = secManager.retrieveUniqueUsers(page, operator);
		
		// JSON输出结果
		HashMap<String, Object> result = wrapResultWithPage(qResult);
		// 取出查询结果的一部分显示在表格中
		List<SecureUser> objResult = qResult.getResults();
		ArrayList<HashMap<String,Object>> rows = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < objResult.size(); i++) {
			SecureUser o = objResult.get(i);
			HashMap<String,Object> row = new HashMap<String, Object>();
			row.put("uid", o.getId());
			row.put("name", o.getName());
			row.put("org", o.getCurrOrgName());
			row.put("ruzhi", o.getArrivalDateAsLong());
			row.put("lizhi", o.getResignDateAsLong());
			rows.add(row);
		}
		result.put("rows", rows);
		outputJson(response, result);
	}
	
	private void getUser(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		String uid = request.getParameter("uid");
		String mode = request.getParameter("mode");
		if (uid == null && !mode.equals("create")) {
			return;
		}
		request.setAttribute("mode", mode);
		if (mode.equals("edit") || mode.equals("view")) {
			// 只有编辑和浏览模式下才需要读取被编辑用户的信息
			SecureUser user = secManager.retrieveUserWithDetails(uid,
					(SecureUser) session.getAttribute(Constant.secUser));
			// 用户基本信息
			HashMap<String,Object> g = new HashMap<String, Object>(); 
			g.put("uid", user.getId());
			g.put("email", user.getEmail());
			g.put("name", user.getName());
			g.put("title", user.getTitle());
			g.put("mobile", user.getMobile());
			g.put("microtel", user.getMicroTel());
			g.put("officetel", user.getOfficeTel());
			g.put("job", user.getJob());
			g.put("status", user.getActive());
			g.put("type", user.getSyncInd());
			g.put("arrdate",user.getArrivalDateAsLong());
			g.put("resdate", user.getResignDateAsLong());	
			g.put("officeaddr", user.getOfficeAddr());
			request.setAttribute("g", JSONObject.fromObject(g).toString());
		}
		RequestDispatcher dispatcher = request
				.getRequestDispatcher(Constant.jspPath + "/edit_attuser.jsp");
		dispatcher.forward(request, response);
	}
	
	private SecureUser getUserData(HttpServletRequest request) {
		SecureUser user = null;
		// 基础信息
		String uid = trimStrToNull(request.getParameter("uid"));
		String method = (String) request.getParameter("method");
		ISecurityMaintenanceManager manager = null;
		if (uid != null && !method.equals("create")) {
			manager = getMtManager();
			user = manager.retrieveUserWithDetails(uid, (SecureUser) request
					.getSession().getAttribute(Constant.secUser));
			if (user == null) {
				return null;
			}
		} else {
			user = new SecureUser();
		}
		user.setId(uid);
		user.setName(request.getParameter("name"));
		user.setTitle(request.getParameter("title"));
		user.setEmail(request.getParameter("email"));
		user.setMobile(request.getParameter("mobile"));
		user.setOfficeTel(request.getParameter("officetel"));
		user.setMicroTel(request.getParameter("microtel"));
		user.setJob(request.getParameter("job"));
		user.setArrivalDate(trimStrToNull(request.getParameter("arrdate")));
		user.setResignDate(trimStrToNull(request.getParameter("resdate")));
		user.setOfficeAddr(request.getParameter("officeaddr"));
		return user;
	}
	
	private void editUser(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String act = request.getParameter("method");
		if (!act.equals("create") && !act.equals("edit")) {
			return;
		}
		SecureUser user = getUserData(request);
		if (user == null) {
			return;
		}
		SecurityBeanHelper helper = SecurityBeanHelper.getInstance();
		ISecurityMaintenanceManager manager = (ISecurityMaintenanceManager) helper
				.getBean(ISecurityMaintenanceManager.class);
		SecureUser operator = (SecureUser) request.getSession().getAttribute(
				Constant.secUser);
		if (act.equals("edit")) {
			try {
				manager.updateUserWithDetails(user, operator);
			} catch (Exception e) {
				logger.error("在更新用户详细信息时出现异常", e);
				outputStatus(response, -1, "用户资料修改失败");
				return;
			}
		}
		if (act.equals("edit")) {
			outputStatus(response, 1, "编辑用户成功");
		}
	}
}
