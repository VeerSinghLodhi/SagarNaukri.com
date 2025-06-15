package com.example.SagarNaukri.com.Jobs;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobsController {

    @GetMapping("/addjob")
    public String addjob(Model model ,HttpSession session){
        Jobs jobs=(Jobs)session.getAttribute("comdata");
        model.addAttribute("jobs" , new Jobs());
        return "JobsHtml/addjobs";
    }

    @GetMapping("/back")
    public String getBack(HttpSession session,Model model){

        return "CompaniesHTML/companydashboard";
    }

}
