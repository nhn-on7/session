package com.nhnacademy.marketgg.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.auth.entity.Member;
import com.nhnacademy.marketgg.auth.repository.MemberRepository;
import com.nhnacademy.marketgg.auth.session.Session;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;
    private final ObjectMapper mapper;

    @GetMapping("/")
    public String index(Session session, Model model) {
        if (Objects.nonNull(session.getAttribute(Session.MEMBER))) {
            Member member = mapper.convertValue(session.getAttribute(Session.MEMBER), Member.class);
            model.addAttribute("member", member);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login-form";
    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute Member member, Session session, HttpServletResponse response) {
        String sessionId = session.getId();
        Member loginMember = memberRepository.login(member.getUsername(), member.getPassword())
                                             .orElseThrow(() -> new IllegalArgumentException("로그인 실패!"));

        session.setAttribute(Session.MEMBER, loginMember);

        Cookie cookie = new Cookie(Session.SESSION_ID, sessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30);

        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(Session session) {
        session.removeAttribute(Session.MEMBER);
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String error(Exception e, RedirectAttributes redirectAttributes) {
        log.error("", e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/error";
    }

    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }

}
