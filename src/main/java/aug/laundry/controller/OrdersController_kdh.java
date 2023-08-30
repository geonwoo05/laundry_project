package aug.laundry.controller;

import aug.laundry.dto.OrdersResponseDto;
import aug.laundry.service.OrdersService_kdh;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/orders")
public class OrdersController_kdh {

    private final OrdersService_kdh ordersServiceKdh;

    @GetMapping("/orders")
    public String orders(){


        return "";
    }

    @GetMapping("/{orderId}/payment")
    public String payOrder(@PathVariable Long orderId, Model model){

        OrdersResponseDto ordersResponseDto = ordersServiceKdh.findByOrdersId(orderId);

        model.addAttribute("order", ordersResponseDto);

        return "project_order_view";
    }

}

