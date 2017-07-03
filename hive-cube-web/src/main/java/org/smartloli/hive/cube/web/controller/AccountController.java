/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartloli.hive.cube.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.smartloli.hive.cube.common.client.CommonClientConfigs.Login;
import org.smartloli.hive.cube.common.pojo.Signiner;
import org.smartloli.hive.cube.web.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Logon service logic control.
 * 
 * @author smartloli.
 *
 *         Created by Sep 6, 2016.
 */
@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	/** Login viewer. */
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView indexView(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/account/signin");
		return mav;
	}

	/** Login checked action. */
	@RequestMapping(value = "/signin/action/", method = RequestMethod.POST)
	public String login(HttpSession session, HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String refUrl = request.getParameter("ref_url");
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(true);
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			return "redirect:" + refUrl.replaceAll("/mf", "");
		} else {
			subject.getSession().setAttribute(Login.ERROR_LOGIN, "<div class='alert alert-danger'>Account or password is error.</div>");
		}
		token.clear();
		return "/account/signin";
	}

	/** Reset password by current account. */
	@RequestMapping(value = "/reset/", method = RequestMethod.POST)
	public String reset(HttpSession session, HttpServletRequest request) {
		String password = request.getParameter("mf_new_password_name");
		Signiner signin = (Signiner) SecurityUtils.getSubject().getSession().getAttribute(Login.SESSION_USER);
		signin.setPassword(password);
		int code = accountService.reset(signin);
		if (code > 0) {
			return "redirect:/account/signout";
		} else {
			return "redirect:/errors/500";
		}

	}

	/** Logout system. */
	@RequestMapping(value = "/signout", method = RequestMethod.GET)
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.getSession().removeAttribute(Login.SESSION_USER);
			subject.getSession().removeAttribute(Login.ERROR_LOGIN);
		}
		return "redirect:/account/signin";
	}

}
