package com.cog.api.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.Utils;
import com.cog.api.model.LearningProgress;
import com.cog.api.model.Lesson;
import com.cog.api.model.Order;
import com.cog.api.model.Series;
import com.cog.api.security.JwtUser;
import com.cog.api.security.SecurityUtils;
import com.cog.api.wechat.WxService;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;

@RestController
@RequestMapping("/api/1.0/series")
public class SeriesController extends BaseController<Series> {
	@Autowired
	private WxService wxService;
	
	@Override
	protected Series innerCreate(Series s) {
		String path = this.findMediaPathById(s.getBannerId());
		s.setBannerPath(path);
		return s;
	}
	
	@Override
	protected Map<String, Object> innerUpdate(String id, Map<String, Object> updatedFields) {
		Map<String, Object> m  = new HashMap<String, Object>(updatedFields);
		String bannerId = (String)updatedFields.get("bannerId");
		if(!StringUtils.isEmpty(bannerId)) {
			m.put("bannerPath", this.findMediaPathById(bannerId));
		}
		return m;
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	public Series get(@PathVariable String id) {
		JwtUser juser = SecurityUtils.getCurrentUser();
		Series series = this.mongoTemplate.findById(new ObjectId(id), Series.class);
		LearningProgress lp = this.findLearningProgress(juser.getId(), id);
		series.setLearningProgress(lp);
		return series;
		
	}
	
	@GetMapping("/{id}/lessons")
	public List<Lesson> listLesson(@PathVariable String id) {
		JwtUser juser = SecurityUtils.getCurrentUser();
		Series series = this.mongoTemplate.findById(new ObjectId(id), Series.class);				
		Query q = new Query(Criteria.where("_id").in(series.getLessonList()));
		List<Lesson> lessons = this.mongoTemplate.find(q, Lesson.class);
		lessons.sort(new Comparator<Lesson>(){
			@Override
			public int compare(Lesson o1, Lesson o2) {
				return series.getLessonList().indexOf(o1.get_id()) 
							- series.getLessonList().indexOf(o2.get_id());
			}	
		});
		LearningProgress lp = this.findLearningProgress(juser.getId(), id);
		if(lp != null) {
			Integer curProgress = lp.getCurProgress();
			Date dateOfLastVisited = lp.getDateOfLastVisitLesson();
			if(dateOfLastVisited == null ||  !Utils.isSameDay(dateOfLastVisited, new Date())) {
				curProgress += 1;
			}			
			for(int i=curProgress; i<lessons.size(); i++) {
				Lesson lesson = lessons.get(i);
				lesson.setContent(null);
				lesson.setMediaId(null);
				lesson.setMediaPath(null);
				lesson.setMediaPath2(null);
				lesson.setMediaId2(null);
			}
			
		} else {
			List<String> freelessonIds = series.getFreeLessons();
			for(Lesson lesson:lessons) {
				if(!freelessonIds.contains(lesson.get_id())) {
					lesson.setContent(null);
					lesson.setMediaId(null);
					lesson.setMediaPath(null);
					lesson.setMediaId2(null);
					lesson.setMediaPath2(null);
				}
 			}
		}	
		return lessons;
	}
	
	private LearningProgress findLearningProgress(String userId, String seriesId) {
		return this.mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId).and("seriesId").is(seriesId)), LearningProgress.class);
	}
	
	@GetMapping("/{id}/lessons/{lessonId}")
	public Lesson getLesson(@PathVariable String id, @PathVariable String lessonId) {
		JwtUser juser = SecurityUtils.getCurrentUser();
		LearningProgress lp = this.findLearningProgress(juser.getId(), id);
		if(lp != null) {
			Lesson les = this.mongoTemplate.findById(new ObjectId(lessonId), Lesson.class);
			return les;
		} else {
			throw new AuthorizationServiceException("Not permit to access this lesson.");
		}
	}
	
	@PutMapping("/{id}/forward-learning-progress")
	public LearningProgress forwardLearningProgress(@PathVariable String id) {
		JwtUser juser = SecurityUtils.getCurrentUser();
		LearningProgress lp = this.findLearningProgress(juser.getId(), id);
		
		if(lp != null) {
			Date lastDate = lp.getDateOfLastVisitLesson();
			Date now = new Date();
			if(lastDate == null || !Utils.isSameDay(lastDate, now)) {
				Query query = Query.query(Criteria.where("_id").is(lp.get_id()));
				Update update = Update.update("curProgress", lp.getCurProgress()+1).set("dateOfLastVisitLesson", now);
				lp = this.mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), LearningProgress.class);
			}
			return lp;
		} else {
			throw new AuthorizationServiceException("This user haven't purchased it!");
		}
		
	}
	
	@GetMapping("/{id}/wxpay")
	public Map<String, String> wxpay(@PathVariable String id, HttpServletRequest httpServletRequest) throws WxPayException {
		JwtUser juser = SecurityUtils.getCurrentUser();
		Series s = this.mongoTemplate.findById(new ObjectId(id), Series.class);
		WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
		orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
		orderRequest.setBody(s.getTitle());
		String ourtTradeNo = UUID.randomUUID().toString();
		orderRequest.setOutTradeNo(ourtTradeNo);
		orderRequest.setTotalFee(Long.valueOf(Math.round(s.getPrice()*100)).intValue());
		orderRequest.setOpenid(juser.getUsername());
		orderRequest.setSpbillCreateIp(httpServletRequest.getRemoteAddr());
		  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
		orderRequest.setTimeStart(now.format(format));
		orderRequest.setTimeExpire(now.plusMinutes(30).format(format));
		orderRequest.setAttach(s.get_id());
		
		return wxService.getWxPayService().getPayInfo(orderRequest);		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}/wxpay_debug")
	public void wxPayDebug(@PathVariable String id) {
		JwtUser juser = SecurityUtils.getCurrentUser();
	    LearningProgress lp = new LearningProgress();
	    lp.setUserId(juser.getId());
	    lp.setSeriesId(id);
	    this.mongoTemplate.save(lp);  
	}
	
	@GetMapping("{id}/check_paystate")
	public Map<String, String> checkPayState(@RequestParam String outTradeNo) {
		Order order= this.mongoTemplate.findById(new ObjectId(outTradeNo), Order.class);
		Map<String, String> result = new HashMap<String, String>();
		if(order != null) {
			result.put("state", "prepay");
		} else {
			result.put("state", "success");
		}
		return result;
	}
}
