package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.XinlichanpinEntity;
import com.entity.view.XinlichanpinView;

import com.service.XinlichanpinService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 心理产品
 * 后端接口
 * @author 
 * @email 
 * @date 2021-05-15 09:44:55
 */
@RestController
@RequestMapping("/xinlichanpin")
public class XinlichanpinController {
    @Autowired
    private XinlichanpinService xinlichanpinService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,XinlichanpinEntity xinlichanpin, 
		HttpServletRequest request){

        EntityWrapper<XinlichanpinEntity> ew = new EntityWrapper<XinlichanpinEntity>();
		PageUtils page = xinlichanpinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xinlichanpin), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,XinlichanpinEntity xinlichanpin, 
		HttpServletRequest request){
        EntityWrapper<XinlichanpinEntity> ew = new EntityWrapper<XinlichanpinEntity>();
		PageUtils page = xinlichanpinService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xinlichanpin), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( XinlichanpinEntity xinlichanpin){
       	EntityWrapper<XinlichanpinEntity> ew = new EntityWrapper<XinlichanpinEntity>();
      	ew.allEq(MPUtil.allEQMapPre( xinlichanpin, "xinlichanpin")); 
        return R.ok().put("data", xinlichanpinService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(XinlichanpinEntity xinlichanpin){
        EntityWrapper< XinlichanpinEntity> ew = new EntityWrapper< XinlichanpinEntity>();
 		ew.allEq(MPUtil.allEQMapPre( xinlichanpin, "xinlichanpin")); 
		XinlichanpinView xinlichanpinView =  xinlichanpinService.selectView(ew);
		return R.ok("查询心理产品成功").put("data", xinlichanpinView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        XinlichanpinEntity xinlichanpin = xinlichanpinService.selectById(id);
        return R.ok().put("data", xinlichanpin);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        XinlichanpinEntity xinlichanpin = xinlichanpinService.selectById(id);
        return R.ok().put("data", xinlichanpin);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody XinlichanpinEntity xinlichanpin, HttpServletRequest request){
    	xinlichanpin.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(xinlichanpin);

        xinlichanpinService.insert(xinlichanpin);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody XinlichanpinEntity xinlichanpin, HttpServletRequest request){
    	xinlichanpin.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(xinlichanpin);

        xinlichanpinService.insert(xinlichanpin);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody XinlichanpinEntity xinlichanpin, HttpServletRequest request){
        //ValidatorUtils.validateEntity(xinlichanpin);
        xinlichanpinService.updateById(xinlichanpin);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        xinlichanpinService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<XinlichanpinEntity> wrapper = new EntityWrapper<XinlichanpinEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}


		int count = xinlichanpinService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
