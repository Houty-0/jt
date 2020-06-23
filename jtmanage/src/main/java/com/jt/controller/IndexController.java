package com.jt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class IndexController {
	
	@RequestMapping("/page/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
	}
}
