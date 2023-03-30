package com.forexapp.controller;
import com.forexapp.model.Currency;
import com.forexapp.model.ForwardOrder;
import com.forexapp.model.ForwardOrderCompleted;
import com.forexapp.service.CurrencyService;
import com.forexapp.service.ForwardOrderService;
import com.forexapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ForwardTradeOrderController {

	private UserService userService;

	private CurrencyService currencyService;

	private ForwardOrderService forwardOrderService;

    @Autowired
    public ForwardTradeOrderController(UserService userService, CurrencyService currencyService, ForwardOrderService forwardOrderService) {
        this.userService = userService;
        this.currencyService = currencyService;
        this.forwardOrderService = forwardOrderService;
    }

    @GetMapping("/newforwardorder")
    public String goToNewForwardOrderPagep(HttpSession session, Model model) {

        String userName = (String) session.getAttribute("username");
        long userId = userService.findByUsername(userName).getUserId();

        List<Currency> userAvailCurrency = currencyService.findAllByUserId(userId);
        List<Currency> allCurrency = currencyService.findAll();

        model.addAttribute("availableCurrency", userAvailCurrency);
        model.addAttribute("allCurrency", allCurrency);
        model.addAttribute("forwardOrder", new ForwardOrder());
        return "newforwardorder";
    }

    @PostMapping("/newforwardorder")
    public String makeNewForwardOrder(@ModelAttribute("forwardOrder") ForwardOrder forwardOrder, HttpSession session, Model model) {

        long userId = (long) session.getAttribute("userId");

        boolean successfulOrder = forwardOrderService.addForwardOrder(forwardOrder, userId);

        if (successfulOrder) {
            model.addAttribute("alert", "Forward order successfully placed.");
        } else {
            model.addAttribute("alert", "You do not have enough funds in your wallet.");
        }

        return "redirect:/pendingforwardtradesdashboard";
    }

    @GetMapping("/activeforwardtradesdashboard")
    public String goToActiveForwardTradesDashboard(Model model) {

        List<ForwardOrder> forwardOrders = forwardOrderService.getAllUnacceptedForwardOrders();
        model.addAttribute("forwardOrders", forwardOrders);

        return "activeforwardtradesdashboard";
    }


    @PostMapping("/activeforwardtradesdashboard")
    public String acceptForwardTrade(long forwardOrderId, HttpSession session) {

        ForwardOrder forwardOrderToAccept = forwardOrderService.findForwardOrderById(forwardOrderId);
        long userId = (long) session.getAttribute("userId");
        forwardOrderService.acceptForwardOrder(forwardOrderToAccept, userId);

        return "redirect:/activeforwardtradesdashboard";
    }

    @GetMapping("/pendingforwardtradesdashboard")
    public String goToPendingForwardTradesDashboard(Model model, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        List<ForwardOrder> forwardOrdersAccepted = userService.getForwardOrdersAcceptedGivenUserId(userId);
        model.addAttribute("forwardOrdersAccepted", forwardOrdersAccepted);

        List<ForwardOrder> unacceptedForwardOrdersInitiated = userService.getUnacceptedForwardOrdersInitiatedGivenUserId(userId);
        model.addAttribute("unacceptedForwardOrdersInitiated", unacceptedForwardOrdersInitiated);

        List<ForwardOrder> acceptedForwardOrdersInitiated = userService.getAcceptedForwardOrdersInitiatedGivenUserId(userId);
        model.addAttribute("acceptedForwardOrdersInitiated", acceptedForwardOrdersInitiated);

        return "pendingforwardtradesdashboard";
    }


    //Getmapping for getting history of forward trades
    @GetMapping("/forwardtradehistory")
    public String goToForwardTradeHistoryPage(HttpSession session) {

        String username = (String) session.getAttribute("username");
        List<ForwardOrderCompleted> completedForwardOrders = userService.getCompletedForwardOrdersGivenUsername(username);
        session.setAttribute("completedForwardOrders", completedForwardOrders);

        return "forwardtradehistory";
    }

}
